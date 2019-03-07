package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import cn.harmonycloud.schedulingalgorithm.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GreedyScheduler implements Scheduler {
    private final static Logger LOGGER = LoggerFactory.getLogger(GreedyScheduler.class);

    private GreedyAlgorithm greedyAlgorithm;
    private Cache cache;

    public GreedyScheduler() {
        super();
        greedyAlgorithm = new DefaultGreedyAlgorithm();
        cache = new Cache();
    }


    /**
     * 每轮调度从待调度队列取出调度请求列表，在此执行调度
     *
     * @param schedulingRequests pod调度请求列表 必须非null非空没有null元素
     */
    @Override
    public void schedule(List<Pod> schedulingRequests) {
        LOGGER.info("start schedule!");
        try {
            // 1. 更新缓存监控数据
            cache.fetchCacheData();
            // 2. 获取应用画像信息
            cache.getPortrait(schedulingRequests);
            // 3. 预排序
            // uncomment when debugging
            // List<Pod> sortedPods = schedulingRequests;
            // comment out when debugging
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
        } catch (Exception e) {
            LOGGER.debug("schedule Exception:");
            e.printStackTrace();
        }
    }

    private void scheduleOne(Pod pod, boolean ifUpdateCache) {
        LOGGER.info("start scheduleOne!");
        // 预选
        List<Node> predicatedNodes = greedyAlgorithm.predicates(pod, cache);
        if (predicatedNodes.isEmpty()) {
            LOGGER.warn("Cannot find any node to schedule this pod. " + "serviceName=" + pod.getServiceName() + ",namespace=" + pod.getNamespace());
            return;
        }
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

    private void scheduleExecute(Pod pod, String host, Cache cache) {
        LOGGER.info("start scheduleExecute!" + DOUtils.getPodFullName(pod) + ", " + host);
        try {
            // uncomment when debugging
//            System.out.println((pod.getOperation() == Constants.OPERATION_ADD ? "add" : "delete") + ":Service=" + DOUtils.getServiceFullName(pod) + ",host=" + host);
            // comment below when debugging
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
        } catch (Exception e) {
            LOGGER.debug("scheduleExecute fail");
            e.printStackTrace();
        }
    }
}
