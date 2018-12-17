package cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.List;

public interface GreedyAlgorithm {
    List<Pod> presort(List<Pod> pods, Cache cache);
    List<Node> predicates(Pod pod, Cache cache);
    List<HostPriority> priorities(Pod pod, List<Node> nodes, Cache cache);
    HostPriority selectHost(List<HostPriority> hostPriorityList, Cache cache);
}
