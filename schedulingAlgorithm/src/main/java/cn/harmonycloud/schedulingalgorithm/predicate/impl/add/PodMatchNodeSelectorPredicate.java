package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.affinity.Affinity;
import cn.harmonycloud.schedulingalgorithm.affinity.InternalSelector;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeAffinity;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelectorTerm;
import cn.harmonycloud.schedulingalgorithm.affinity.Selector;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 没有测试过，逻辑比较复杂，bug应该很多
 */
public class PodMatchNodeSelectorPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // 1. node selector
        Map<String, String> podNodeSelectorMap = pod.getNodeSelector();
        Map<String, String> labels = node.getLabels();
        if (podNodeSelectorMap != null && !podNodeSelectorMap.isEmpty()) {
            Selector selector = new InternalSelector(podNodeSelectorMap);
            if (!selector.matches(labels)) {
                return false;
            }
        }
        // 2. affinity terms
        Affinity affinity = pod.getAffinityObject();
        if (affinity != null && affinity.getNodeAffinity() != null) {
            NodeAffinity nodeAffinity = affinity.getNodeAffinity();
            if (nodeAffinity.getRequiredDuringSchedulingIgnoredDuringExecution() == null) {
                return true;
            } else {
                List<NodeSelectorTerm> nodeSelectorTerms = Arrays.asList(nodeAffinity.getRequiredDuringSchedulingIgnoredDuringExecution().getNodeSelectorTerms());
                return nodeMatchesNodeSelectorTerms(node, nodeSelectorTerms);
            }
        }
        return true;
    }

    private boolean nodeMatchesNodeSelectorTerms(Node node, List<NodeSelectorTerm> nodeSelectorTerms) {
        Map<String, String> nodeFields = new HashMap<>();
        nodeFields.put("metadata.name", node.getNodeName());
        Map<String, String> nodeLabels = node.getLabels();
        return RuleUtil.matchNodeSelectorTerms(nodeSelectorTerms, nodeLabels, nodeFields);
    }
}
