package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

public class CheckNodePIDPressurePredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        return RuleUtil.checkNodeCondition(node.getNodeConditions(), types, statuses);
    }

    private static final String[] types = {"PIDPressure"};
    private static final String[] statuses = {"true"};
}
