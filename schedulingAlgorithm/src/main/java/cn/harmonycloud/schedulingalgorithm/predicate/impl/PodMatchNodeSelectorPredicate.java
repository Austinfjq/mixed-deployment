package cn.harmonycloud.schedulingalgorithm.predicate.impl;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelector;

import java.util.HashMap;
import java.util.Map;

public class PodMatchNodeSelectorPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // 1. node selector
        // TODO 亲和性信息 如何拿到pod的node selector
        Map<String, String> podNodeSelectorMap = new HashMap<>();
        // TODO 亲和性信息 如何拿到node的labels
        Map<String, String> labels = new HashMap<>();
        if (!podNodeSelectorMap.isEmpty()) {
            NodeSelector selector = new NodeSelector(podNodeSelectorMap);
            if (!selector.matches(labels)) {
                return false;
            }
        }
        // 2. affinity terms
        boolean nodeAffinityMatches = true;

        // TODO: node affinity
//        if nodeAffinity.RequiredDuringSchedulingIgnoredDuringExecution != nil {
//            nodeSelectorTerms := nodeAffinity.RequiredDuringSchedulingIgnoredDuringExecution.NodeSelectorTerms
//            klog.V(10).Infof("Match for RequiredDuringSchedulingIgnoredDuringExecution node selector terms %+v", nodeSelectorTerms)
//            nodeAffinityMatches = nodeAffinityMatches && nodeMatchesNodeSelectorTerms(node, nodeSelectorTerms)
//        }

        return nodeAffinityMatches;
    }
}
