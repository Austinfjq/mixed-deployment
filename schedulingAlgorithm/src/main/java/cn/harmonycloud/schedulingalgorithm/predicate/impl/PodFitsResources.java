package cn.harmonycloud.schedulingalgorithm.predicate.impl;

import cn.harmonycloud.schedulingalgorithm.common.Node;
import cn.harmonycloud.schedulingalgorithm.common.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;

import java.util.List;

public class PodFitsResources implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node) {
        // TODO
        return true;
    }
}
