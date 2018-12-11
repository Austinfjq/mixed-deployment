package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRule;

import java.util.List;

public class LeastRequestedPriority implements PriorityRule {
    @Override
    public List<Integer> priority(Pod pod, List<Node> node) {
        //TODO
        return null;
    }
}
