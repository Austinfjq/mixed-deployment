package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import cn.harmonycloud.schedulingalgorithm.utils.ExecuteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FakeSingleClusterGreedyScheduler implements Scheduler {
    private final static Logger LOGGER = LoggerFactory.getLogger(FakeSingleClusterGreedyScheduler.class);

    private GreedyAlgorithm greedyAlgorithm = new DefaultGreedyAlgorithm();
    Cache cache = new FakeCache();
    long[] time = new long[100];

    /**
     * 每轮调度从待调度队列取出调度请求列表，在此执行调度
     *
     * @param schedulingRequests pod调度请求列表 必须非null非空没有null元素
     */
    @Override
    public void schedule(List<Pod> schedulingRequests) {
        GlobalSetting.LOG_DETAIL = false;
        GlobalSetting.PARALLEL = true;
        try {
            cache.fetchSingleCacheData();
            // 2. 获取应用画像信息
            cache.getPortrait(schedulingRequests);
            LOGGER.info("start schedule! node number=" + cache.getNodeList().size());
            time[0] = System.currentTimeMillis();
            // 3. 预排序
            List<Pod> sortedPods = greedyAlgorithm.presort(schedulingRequests, cache);
            time[1] = System.currentTimeMillis();
            // 4. 逐个处理待调度pod
            for (int i = 0; i < sortedPods.size(); i++) {
                HostPriority host;
                // 调度本轮最后一个pod后，不需再更新缓存
                if (i == sortedPods.size() - 1) {
                    host = scheduleOne(sortedPods.get(i), false);
                } else {
                    host = scheduleOne(sortedPods.get(i), true);
                }
                // 调用调度执行器，只发送一个host
                if (GlobalSetting.LOG_DETAIL) {
                    LOGGER.info("start scheduleExecute: " + (sortedPods.get(i).getOperation() == Constants.OPERATION_ADD ? "add" : "delete") + ": Service=" + DOUtils.getServiceFullName(sortedPods.get(i)) + ", host=" + (host == null ? "null" : host.getHostname()));
                }
            }
            LOGGER.info("请求数量=" + schedulingRequests.size());
            LOGGER.info("节点数量=" + cache.getNodeList().size());
            LOGGER.info("预排序耗时=" + (time[1] - time[0]));
            LOGGER.info("预选耗时=" + (time[2] - time[1]));
            LOGGER.info("优选耗时=" + (time[3] - time[2]));
            LOGGER.info("选择耗时=" + (time[4] - time[3]));
            LOGGER.info("总耗时=" + (time[4] - time[0]));
        } catch (Exception e) {
            LOGGER.debug("schedule Exception:");
            e.printStackTrace();
        }
    }

    public HostPriority scheduleOne(Pod pod, boolean ifUpdateCache) {
        if (GlobalSetting.LOG_DETAIL) {
            LOGGER.info("start scheduleOne! operation=" + pod.getOperation() + ", servicename=" + pod.getServiceName(), ", namespace=" + pod.getNamespace());
        }
        // 1. 预选
        List<Node> predicatedNodes = greedyAlgorithm.predicates(pod, cache);
        if (GlobalSetting.LOG_DETAIL) {
            if (predicatedNodes.isEmpty()) {
                LOGGER.warn("Cannot find any node to schedule this pod. " + "serviceName=" + pod.getServiceName() + ",namespace=" + pod.getNamespace());
                return null;
            }
        }
        time[2] = System.currentTimeMillis();
        // 2. 优选
        List<HostPriority> hostPriorityList = greedyAlgorithm.priorities(pod, predicatedNodes, cache);
        time[3] = System.currentTimeMillis();
        // 3. 挑选节点
        HostPriority selectedHost = greedyAlgorithm.selectHost(hostPriorityList, cache);
        time[4] = System.currentTimeMillis();
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
