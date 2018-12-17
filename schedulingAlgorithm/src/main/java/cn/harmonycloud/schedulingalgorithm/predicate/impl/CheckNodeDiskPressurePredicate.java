package cn.harmonycloud.schedulingalgorithm.predicate.impl;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;

public class CheckNodeDiskPressurePredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // CheckNodeDiskPressure: Check if a pod can be scheduled on a node reporting disk pressure condition.
        // Currently, no pods should be placed on a node under disk pressure as it gets automatically evicted by kubelet.
        String condition = node.getCondition();

        boolean diskPressure = false; // TODO how to parse condition
        if (diskPressure) {
            return false;
        }
        return true;
    }
}
