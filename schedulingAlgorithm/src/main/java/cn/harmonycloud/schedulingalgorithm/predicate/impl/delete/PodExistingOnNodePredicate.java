package cn.harmonycloud.schedulingalgorithm.predicate.impl.delete;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;

public class PodExistingOnNodePredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        return cache.getNodeMapPodList().get(node.getNodeName()).stream()
                .anyMatch(ep ->
                        DOUtils.getServiceFullName(ep).equals(DOUtils.getServiceFullName(pod))
                );
    }
}
