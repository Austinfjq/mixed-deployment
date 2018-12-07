package cn.harmonycloud.schedulingalgorithm.priority;

import cn.harmonycloud.schedulingalgorithm.common.HostPriority;
import cn.harmonycloud.schedulingalgorithm.common.Node;
import cn.harmonycloud.schedulingalgorithm.common.Pod;

import java.util.List;

public interface PriorityRule {
    List<HostPriority> priority(Pod pod, List<Node> node);
}
