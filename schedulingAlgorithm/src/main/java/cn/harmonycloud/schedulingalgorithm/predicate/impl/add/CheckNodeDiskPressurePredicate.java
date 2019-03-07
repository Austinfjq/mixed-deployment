package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

public class CheckNodeDiskPressurePredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // CheckNodeDiskPressure: Check if a pod can be scheduled on a node reporting disk pressure condition.
        // Currently, no pods should be placed on a node under disk pressure as it gets automatically evicted by kubelet.

        return RuleUtil.checkNodeCondition(node.getNodeConditions(), types, statuses);
    }

    private static final String[] types = {"diskPressure"};
    private static final String[] statuses = {"true"};
}
