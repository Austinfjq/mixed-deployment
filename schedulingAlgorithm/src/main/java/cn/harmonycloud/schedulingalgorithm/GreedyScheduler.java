package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.ContainerPort;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import cn.harmonycloud.schedulingalgorithm.utils.HttpUtil;
import com.google.gson.Gson;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GreedyScheduler implements Scheduler {
    private GreedyAlgorithm greedyAlgorithm;
    private Cache cache;

    GreedyScheduler() {
        super();
        greedyAlgorithm = new DefaultGreedyAlgorithm();
        cache = new Cache();
    }

    private static final Gson gson = new Gson();

    /**
     * 每轮调度从待调度队列取出调度请求列表，在此执行调度
     *
     * @param schedulingRequests pod调度请求列表 必须非null非空没有null元素
     */
    @Override
    public void schedule(List<Pod> schedulingRequests) {
        // 1. 更新缓存监控数据
        cache.fetchCacheData();
        // 2. 获取应用画像信息
        getPortrait(schedulingRequests);
        // 3. 预排序
        List<Pod> sortedPods = greedyAlgorithm.presort(schedulingRequests, cache);
        // 4. 逐个处理待调度pod
        for (int i = 0; i < sortedPods.size(); i++) {
            // 调度本轮最后一个pod后，不需再更新缓存
            if (i == sortedPods.size() - 1) {
                scheduleOne(sortedPods.get(i), false);
            } else {
                scheduleOne(sortedPods.get(i), true);
            }
        }
    }

    private void scheduleOne(Pod pod, boolean ifUpdateCache) {
        // 预选
        List<Node> predicatedNodes = greedyAlgorithm.predicates(pod, cache);
        // 优选
        List<HostPriority> hostPriorityList = greedyAlgorithm.priorities(pod, predicatedNodes, cache);
        // 挑选节点
        HostPriority selectedHost = greedyAlgorithm.selectHost(hostPriorityList, cache);
        // 调用调度执行器，只发送一个host
        scheduleExecute(pod, selectedHost.getHost(), cache);
        // 修改缓存
        if (ifUpdateCache) {
            cache.updateCache(pod, selectedHost.getHost());
        }
    }

    private void getPortrait(List<Pod> pods) {
        List<String> serviceFullNames = pods.stream().map(DOUtils::getServiceFullName).distinct().collect(Collectors.toList());
        Map<String, Service> serviceMap = cache.getServiceMap();
        for (String serviceFullName : serviceFullNames) {
            // 获取预计占用资源信息
            Map<String, String> parameters = new HashMap<>();
            String[] split = serviceFullName.split(DOUtils.NAME_SPLIT);
            parameters.put("namespace", split[0]);
            parameters.put("serviceName", split[1]);
            String result = HttpUtil.post(Constants.URI_GET_POD_CONSUME, parameters);
            JSONObject jsonObject = JSONObject.fromObject(result);
            Service service = serviceMap.get(serviceFullName);
            service.setCpuCosume(jsonObject.optString("cpuCosume"));
            service.setMemCosume(jsonObject.optString("memCosume"));
            service.setDownNetIOCosume(jsonObject.optString("DownNetIOCosume"));
            service.setUPNetIOCosume(jsonObject.optString("UPNetIOCosume"));
            // 获取对应的service的资源密集类型
            result = HttpUtil.post(Constants.URI_GET_SERVICE_TYPE, parameters);
            jsonObject = JSONObject.fromObject(result);
            service.setIntensiveType(jsonObject.optInt("serviceType"));
        }

        //查找同service下的pod，填写新pod的属性
        pods.forEach(p -> {
            Service service = cache.getServiceMap().get(DOUtils.getServiceFullName(p));
            Pod sp = cache.getPodMap().get(DOUtils.getPodFullName(service.getPodList().get(0), p.getNamespace()));
            p.setCpuRequest(sp.getCpuRequest());
            p.setMemRequest(sp.getMemRequest());
            p.setNodeSelector(sp.getNodeSelector());
            p.setAffinity(sp.getAffinity());
            p.setContainers(sp.getContainers());
            p.setToleration(sp.getToleration());
            // setWantPorts
            String ports = sp.getContainers().getPorts();
            ContainerPort[] wantPorts = gson.fromJson(ports, ContainerPort[].class);
            p.setWantPorts(wantPorts);
        });
    }

    private void scheduleExecute(Pod pod, String host, Cache cache) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("namespace", pod.getNamespace());
        String uri;
        if (pod.getOperation() == Constants.OPERATION_ADD) {
            parameters.put("serviceName", pod.getServiceName());
            parameters.put("nodeName", host);
            uri = Constants.URI_EXECUTE_ADD;
        } else {
            Optional<String> op = cache.getNodeMapPodList().get(host).stream().filter(p -> DOUtils.getServiceFullName(pod).equals(DOUtils.getServiceFullName(p))).map(DOUtils::getPodFullName).findFirst();
            String podName = op.orElse(null);
            parameters.put("podName", podName);
            uri = Constants.URI_EXECUTE_REMOVE;
        }
        HttpUtil.post(uri, parameters);
    }
}
