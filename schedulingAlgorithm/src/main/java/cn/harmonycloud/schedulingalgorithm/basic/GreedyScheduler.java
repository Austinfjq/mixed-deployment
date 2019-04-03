package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import cn.harmonycloud.schedulingalgorithm.utils.ExecuteUtil;
import cn.harmonycloud.schedulingalgorithm.utils.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GreedyScheduler implements Scheduler {
    private final static Logger LOGGER = LoggerFactory.getLogger(GreedyScheduler.class);

    private GreedyAlgorithm greedyAlgorithm = new DefaultGreedyAlgorithm();
    MultiClusterCache multiClusterCache = new MultiClusterCache();
    Cache cache;

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
            multiClusterCache.fetchMultiCacheData();
            // 2. 调度请求pod按不同的集群分组处理
            Map<String, List<Pod>> group = schedulingRequests.stream().collect(Collectors.groupingBy(Pod::getClusterMasterIP));
            group.forEach((clusterMasterIP, requests) -> {
                // 3. 取出对应的集群缓存
                cache = multiClusterCache.get(clusterMasterIP);
                // 4. 获取应用画像信息
                cache.getPortrait(requests);
                // 5. 预排序
                List<Pod> sortedPods = greedyAlgorithm.presort(requests, cache);
                // 6. 逐个处理待调度pod
                for (int i = 0; i < sortedPods.size(); i++) {
                    HostPriority host;
                    // 7. 调度。本轮最后一个pod后，不需再更新缓存
                    if (i == sortedPods.size() - 1) {
                        host = scheduleOne(sortedPods.get(i), false);
                    } else {
                        host = scheduleOne(sortedPods.get(i), true);
                    }
                    // 9. 调用调度执行器，只发送一个host
                    ExecuteUtil.scheduleExecute(sortedPods.get(i), host, cache);
                }
            });
        } catch (Exception e) {
            LOGGER.debug("schedule Exception:");
            e.printStackTrace();
        }
    }

    public HostPriority scheduleOne(Pod pod, boolean ifUpdateCache) {
        LOGGER.info("start scheduleOne! operation=" + pod.getOperation() + ", servicename=" + pod.getServiceName(), ", namespace=" + pod.getNamespace());
        // 1. 预选
        List<Node> predicatedNodes = greedyAlgorithm.predicates(pod, cache);
        if (predicatedNodes.isEmpty()) {
            LOGGER.warn("Cannot find any node to schedule this pod. " + "serviceName=" + pod.getServiceName() + ",namespace=" + pod.getNamespace());
            return null;
        }
        // 2. 优选
        List<HostPriority> hostPriorityList = greedyAlgorithm.priorities(pod, predicatedNodes, cache);
        // 3. 挑选节点
        HostPriority selectedHost = greedyAlgorithm.selectHost(hostPriorityList, cache);
        // 4. 修改缓存
        if (ifUpdateCache) {
            cache.updateCache(pod, selectedHost.getHostname());
        }
        return selectedHost;
    }

    public GreedyAlgorithm getGreedyAlgorithm() {
        return greedyAlgorithm;
    }

    public void setGreedyAlgorithm(GreedyAlgorithm greedyAlgorithm) {
        this.greedyAlgorithm = greedyAlgorithm;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
