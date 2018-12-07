package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.common.HostPriority;
import cn.harmonycloud.schedulingalgorithm.common.Node;
import cn.harmonycloud.schedulingalgorithm.common.Pod;

import java.util.List;

public class GreedyScheduler implements Scheduler {
    private static GreedyAlgorithm greedyAlgorithm;

    public GreedyScheduler() {
        super();
        greedyAlgorithm = new DefaultGreedyAlgorithm();
    }

    /**
     * 每轮调度从待调度队列取出调度请求列表，在此执行调度
     * @param schedulingRequests pod调度请求列表
     */
    @Override
    public void schedule(List<Pod> schedulingRequests) {
        // TODO 可并行化 1 与 (2,3)

        // 1. 更新节点数据
        greedyAlgorithm.getNodeLister().fetchNodes();

        // 2. 获取pod详细信息
        List<Pod> Pods = Utils.getPodsDetail(schedulingRequests);
        // 3. 预排序
        List<Pod> sortedPods = greedyAlgorithm.presort(Pods);

        // 4. 逐个处理待调度pod
        for (Pod Pod : sortedPods) {
            scheduleOne(Pod);
        }
    }

    private void scheduleOne(Pod Pod) {
        // 预选
        List<Node> predicatedNodes = greedyAlgorithm.predicates(Pod, greedyAlgorithm.getNodeLister().listNodes());
        // 优选
        List<HostPriority> hostPriorityList = greedyAlgorithm.priorities(Pod, predicatedNodes);
        // 挑选节点
        List<HostPriority> hostPriority = greedyAlgorithm.selectHost(hostPriorityList);
        // TODO 调用调度执行器，先按只发送一个结果
    }
}
