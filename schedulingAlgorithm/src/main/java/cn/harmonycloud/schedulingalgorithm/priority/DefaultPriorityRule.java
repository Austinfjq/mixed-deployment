package cn.harmonycloud.schedulingalgorithm.priority;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface DefaultPriorityRule extends PriorityRule {
    @Override
    default List<Integer> priority(Pod pod, List<Node> nodes, Cache cache) {
        return (GlobalSetting.PARALLEL ? nodes.parallelStream() : nodes.stream())
                .map(node -> priority(pod, node, cache))
                .collect(Collectors.toList());
    }

    Integer priority(Pod pod, Node node, Cache cache);
}
