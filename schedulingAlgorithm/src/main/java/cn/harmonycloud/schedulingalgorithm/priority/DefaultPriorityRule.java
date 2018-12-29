package cn.harmonycloud.schedulingalgorithm.priority;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.List;
import java.util.stream.Collectors;

public interface DefaultPriorityRule extends PriorityRule {
    @Override
    default List<Integer> priority(Pod pod, List<Node> nodes, Cache cache) {
        // TODO: use parallelStream
        return nodes.stream().map(node -> priority(pod, node, cache)).collect(Collectors.toList());
    }

    Integer priority(Pod pod, Node node, Cache cache);
}