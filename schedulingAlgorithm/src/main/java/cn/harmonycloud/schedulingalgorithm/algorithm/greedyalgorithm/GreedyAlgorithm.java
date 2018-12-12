package cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm;

import cn.harmonycloud.schedulingalgorithm.algorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.List;

public interface GreedyAlgorithm {
    Cache getCache();
    List<Pod> presort(List<Pod> pods);
    List<Node> predicates(Pod pod);
    List<HostPriority> priorities(Pod pod, List<Node> nodes);
    List<HostPriority> selectHost(List<HostPriority> hostPriorityList);
}
