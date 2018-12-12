package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;

import java.util.List;

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
        // 1. 更新缓存数据
        greedyAlgorithm.getCache().fetchCacheData();

        // 2. 获取pod详细信息
        List<Pod> pods = getPodsDetail(schedulingRequests);
        // 3. 预排序
        List<Pod> sortedPods = greedyAlgorithm.presort(pods);

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

    private List<Pod> getPodsDetail(List<Pod> pods) {
        for (Pod pod : pods) {
            Service service = greedyAlgorithm.getCache().getServiceMap().get(pod.getNamespace() + "-" + pod.getServiceName());
            // TODO
        }
        // TODO
        return null;
    }
}
