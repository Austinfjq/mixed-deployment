package cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm;

import cn.harmonycloud.schedulingalgorithm.algorithm.NodeLister;
import cn.harmonycloud.schedulingalgorithm.common.HostPriority;
import cn.harmonycloud.schedulingalgorithm.common.Node;
import cn.harmonycloud.schedulingalgorithm.common.Pod;

import java.util.List;

public interface GreedyAlgorithm {
    List<Pod> presort(List<Pod> pods);
    List<Node> predicates(Pod pod, List<Node> nodes);
    List<HostPriority> priorities(Pod pod, List<Node> nodes);
    NodeLister getNodeLister();
    List<HostPriority> selectHost(List<HostPriority> hostPriorityList);
}
