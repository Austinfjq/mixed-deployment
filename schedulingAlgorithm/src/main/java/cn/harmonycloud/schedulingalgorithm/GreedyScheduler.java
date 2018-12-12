package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import cn.harmonycloud.schedulingalgorithm.utils.HttpUtil;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GreedyScheduler implements Scheduler {
    private GreedyAlgorithm greedyAlgorithm;

    GreedyScheduler() {
        super();
        greedyAlgorithm = new DefaultGreedyAlgorithm();
    }

    /**
     * 每轮调度从待调度队列取出调度请求列表，在此执行调度
     *
     * @param schedulingRequests pod调度请求列表
     */
    @Override
    public void schedule(List<Pod> schedulingRequests) {
        // 1. 更新缓存监控数据
        greedyAlgorithm.getCache().fetchCacheData();

        // 2. 获取应用画像信息
        getPortrait(schedulingRequests);
        // 3. 预排序
        List<Pod> sortedPods = greedyAlgorithm.presort(schedulingRequests);

        // 4. 逐个处理待调度pod
        for (Pod Pod : sortedPods) {
            scheduleOne(Pod);
        }
    }

    private void scheduleOne(Pod pod) {
        // 预选
        List<Node> predicatedNodes = greedyAlgorithm.predicates(pod);
        // 优选
        List<HostPriority> hostPriorityList = greedyAlgorithm.priorities(pod, predicatedNodes);
        // 挑选节点
        List<HostPriority> hostPriority = greedyAlgorithm.selectHost(hostPriorityList);
        // TODO 调用调度执行器，先按只发送一个结果
    }

    private void getPortrait(List<Pod> pods) {
        List<String> serviceFullNames = pods.stream().map(DOUtils::getServiceFullName).distinct().collect(Collectors.toList());
        Map<String, Service> serviceMap = greedyAlgorithm.getCache().getServiceMap();
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
    }
}
