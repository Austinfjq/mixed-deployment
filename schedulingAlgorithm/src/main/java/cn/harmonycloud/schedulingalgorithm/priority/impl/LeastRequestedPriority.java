package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.common.HostPriority;
import cn.harmonycloud.schedulingalgorithm.common.Node;
import cn.harmonycloud.schedulingalgorithm.common.Pod;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRule;

import java.util.List;

public class LeastRequestedPriority implements PriorityRule {
    @Override
    public List<HostPriority> priority(Pod pod, List<Node> node) {
        //TODO
        return null;
    }
}
