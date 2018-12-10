package cn.harmonycloud.schedulingalgorithm.predicate;

import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

public interface PredicateRule {
    boolean predicate(Pod pod, Node node);
}
