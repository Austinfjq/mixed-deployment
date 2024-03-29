package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.NodeCondition;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;

public class CheckNodeConditionPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        if (node == null) {
            return false;
        }
        NodeCondition cond = node.getNodeConditions();
        if (cond != null) {
//            for (NodeCondition cond : nodeConditions) {
            if (NodeReady.equals(cond.getType()) && !ConditionTrue.equals(cond.getStatus())) {
                return false;
            }
            if (NodeNetworkUnavailable.equals(cond.getType()) && !ConditionFalse.equals(cond.getStatus())) {
                return false;
            }
//            }
        }
        if (node.getUnschedulable() != null && node.getUnschedulable()) {
            return false;
        }
        return true;
    }

    private static final String NodeReady = "Ready";
    private static final String ConditionTrue = "True";
    private static final String ConditionFalse = "False";
    private static final String NodeNetworkUnavailable = "NetworkUnavailable";
}
