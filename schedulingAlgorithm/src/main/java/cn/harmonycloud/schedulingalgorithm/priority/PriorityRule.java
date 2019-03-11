package cn.harmonycloud.schedulingalgorithm.priority;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.List;

public interface PriorityRule {
    /**
     * for the greedy scheduler
     */
    List<Integer> priority(Pod pod, List<Node> nodes, Cache cache);

    /**
     * for other optimized schedulers
     * use operation of Pod instead of Rule
     * do not need efficiency
     */
    Integer multiPriority(List<Pod> pods, List<String> hosts, Cache cache);

    /**
     * for other optimized schedulers
     * use operation of Pod instead of Rule
     * need efficiency
     */
    Integer deltaMultiPriority(List<Pod> pods, List<String> oldHosts, List<String> newHosts, Cache cache);
}
