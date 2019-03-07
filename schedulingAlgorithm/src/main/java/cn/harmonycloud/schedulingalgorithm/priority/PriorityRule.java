package cn.harmonycloud.schedulingalgorithm.priority;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.List;

public interface PriorityRule {
    List<Integer> priority(Pod pod, List<Node> nodes, Cache cache);
}
