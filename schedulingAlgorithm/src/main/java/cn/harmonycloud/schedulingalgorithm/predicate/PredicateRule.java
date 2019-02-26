package cn.harmonycloud.schedulingalgorithm.predicate;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

public interface PredicateRule {
    /**
     * 必须线程安全
     */
    boolean predicate(Pod pod, Node node, Cache cache);
}
