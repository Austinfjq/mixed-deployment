package cn.harmonycloud.schedulingalgorithm.predicate.impl.delete;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;

public class PodExistingOnNodePredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        return cache.getPodMap().values().stream()
                .filter(p -> DOUtils.getServiceFullName(p).equals(DOUtils.getServiceFullName(pod)))
                .map(Pod::getNodeName)
                .anyMatch(nodeName -> nodeName.equals(node.getNodeName()));
    }
}
