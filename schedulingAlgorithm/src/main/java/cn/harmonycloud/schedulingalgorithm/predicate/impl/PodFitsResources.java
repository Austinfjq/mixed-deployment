package cn.harmonycloud.schedulingalgorithm.predicate.impl;

import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;

public class PodFitsResources implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node) {
        // TODO
        return true;
    }
}
