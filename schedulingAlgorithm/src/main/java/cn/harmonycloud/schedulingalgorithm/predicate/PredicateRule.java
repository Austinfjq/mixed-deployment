package cn.harmonycloud.schedulingalgorithm.predicate;

import cn.harmonycloud.schedulingalgorithm.common.Node;
import cn.harmonycloud.schedulingalgorithm.common.Pod;

public interface PredicateRule {
    boolean predicate(Pod pod, Node node);
}
