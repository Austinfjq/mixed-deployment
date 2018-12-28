package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.affinity.Affinity;
import cn.harmonycloud.schedulingalgorithm.affinity.PreferredSchedulingTerm;
import cn.harmonycloud.schedulingalgorithm.affinity.Selector;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRule;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;
import cn.harmonycloud.schedulingalgorithm.utils.SelectorUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NodeAffinityPriority implements PriorityRule {
    @Override
    public List<Integer> priority(Pod pod, List<Node> nodes, Cache cache) {
        // map TODO parallel
        List<Integer> mapResult = nodes.stream()
                .map(node -> calculateNodeAffinityPriorityMap(pod, node, cache))
                .collect(Collectors.toList());
        // reduce
        return calculateNodeAffinityPriorityReduce(pod, nodes, cache, mapResult);
    }

    private int calculateNodeAffinityPriorityMap(Pod pod, Node node, Cache cache) {
        if (node == null) {
            return 0;
        }
        Affinity affinity = new Affinity(); //TODO get affinity of pod
        int count = 0;
        // A nil element of PreferredDuringSchedulingIgnoredDuringExecution matches no objects.
        // An element of PreferredDuringSchedulingIgnoredDuringExecution that refers to an
        // empty PreferredSchedulingTerm matches all objects.
        if (affinity != null && affinity.getNodeAffinity() != null && affinity.getNodeAffinity().getPreferredDuringSchedulingIgnoredDuringExecution() != null) {
            // Match PreferredDuringSchedulingIgnoredDuringExecution term by term.
            List<PreferredSchedulingTerm> terms = affinity.getNodeAffinity().getPreferredDuringSchedulingIgnoredDuringExecution();
            for (int i = 0; i < terms.size(); i++) {
                PreferredSchedulingTerm preferredSchedulingTerm = terms.get(i);
                if (preferredSchedulingTerm.getWeight() == 0) {
                    continue;
                }

                Selector nodeSelector = SelectorUtil.nodeSelectorRequirementsAsSelector(preferredSchedulingTerm.getPreference().getMatchExpressions());
                if (nodeSelector == null) {
                    return 0;
                }
                Map<String, String> nodeLabels = new HashMap<>(); // TODO get labels of node
                if (nodeSelector.matches(nodeLabels)) {
                    count += preferredSchedulingTerm.getWeight();
                }
            }
        }
        return count;
    }

    private List<Integer> calculateNodeAffinityPriorityReduce(Pod pod, List<Node> nodes, Cache cache, List<Integer> mapResult) {
        return RuleUtil.normalizeReduce(pod, nodes, cache, mapResult, Constants.PRIORITY_MAX_SCORE, false);
    }
}