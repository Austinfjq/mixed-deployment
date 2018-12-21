package cn.harmonycloud.schedulingalgorithm.predicate.impl;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.affinity.Affinity;
import cn.harmonycloud.schedulingalgorithm.affinity.AffinityTermProperties;
import cn.harmonycloud.schedulingalgorithm.affinity.InternalSelector;
import cn.harmonycloud.schedulingalgorithm.affinity.LabelSelector;
import cn.harmonycloud.schedulingalgorithm.affinity.LabelSelectorRequirement;
import cn.harmonycloud.schedulingalgorithm.affinity.PodAffinity;
import cn.harmonycloud.schedulingalgorithm.affinity.PodAffinityTerm;
import cn.harmonycloud.schedulingalgorithm.affinity.PodAntiAffinity;
import cn.harmonycloud.schedulingalgorithm.affinity.Requirement;
import cn.harmonycloud.schedulingalgorithm.affinity.SelectOperation;
import cn.harmonycloud.schedulingalgorithm.affinity.Selector;
import cn.harmonycloud.schedulingalgorithm.affinity.TopologyPair;
import cn.harmonycloud.schedulingalgorithm.affinity.TopologyPairsMaps;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MatchInterPodAffinityPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // InterPodAffinityMatches
        // 1. satisfiesExistingPodsAntiAffinity
        if (!satisfiesExistingPodsAntiAffinity(pod, node, cache)) {
            return false;
        }
        // 2. satisfiesPodsAffinityAntiAffinity
        // Now check if <pod> requirements will be satisfied on this node.
        Affinity affinity = new Affinity(); // TODO get	affinity := pod.Spec.Affinity
        if (!satisfiesPodsAffinityAntiAffinity(pod, node, affinity, cache)) {
            return false;
        }
        return true;
    }

    private boolean[] podMatchesPodAffinityTerms(Pod pod, Pod targetPod, Node node, List<PodAffinityTerm> terms, Cache cache) {
        if (terms == null || terms.isEmpty()) {
            return new boolean[]{false, false};
        }
        List<AffinityTermProperties> props;
        try {
            props = getAffinityTermProperties(pod, terms);
        } catch (Exception e) {
            return new boolean[]{false, false};
        }
        if (!podMatchesAllAffinityTermProperties(targetPod, props)) {
            return new boolean[]{false, false};
        }
        Node targetPodNode = cache.getNodeMap().get(targetPod.getNodeName());
        if (targetPodNode == null) {
            return new boolean[]{false, false};
        }
        for (PodAffinityTerm term : terms) {
            if (term.getTopologyKey() == null || term.getTopologyKey().isEmpty()) {
                return new boolean[]{false, false};
            }
            if (!nodesHaveSameTopologyKey(node, targetPodNode, term.getTopologyKey())) {
                return new boolean[]{false, true};
            }
        }
        return new boolean[]{true, true};
    }

    private boolean nodesHaveSameTopologyKey(Node nodeA, Node nodeB, String topologyKey) {
        // TODO get labels of node
        Map<String, String> nodeALabels = new HashMap<>();
        Map<String, String> nodeBLabels = new HashMap<>();
        if (topologyKey == null || topologyKey.isEmpty() || nodeALabels == null || nodeBLabels == null) {
            return false;
        }
        String nodeALabel = nodeALabels.get(topologyKey);
        String nodeBLabel = nodeBLabels.get(topologyKey);
        if (nodeALabel != null && nodeBLabel != null) {
            return nodeALabel.equals(nodeBLabel);
        }
        return false;
    }

    private boolean podMatchesAllAffinityTermProperties(Pod pod, List<AffinityTermProperties> properties) {
        if (properties == null || properties.isEmpty()) {
            return false;
        }
        for (AffinityTermProperties property : properties) {
            if (!podMatchesTermsNamespaceAndSelector(pod, property.getNamespaces(), property.getSelector())) {
                return false;
            }
        }
        return true;
    }

    private List<AffinityTermProperties> getAffinityTermProperties(Pod pod, List<PodAffinityTerm> terms) {
        List<AffinityTermProperties> properties = new ArrayList<>();
        if (terms == null) {
            return properties;
        }
        for (PodAffinityTerm term : terms) {
            Set<String> namespaces = getNamespacesFromPodAffinityTerm(pod, term);
            Selector selector = labelSelectorAsSelector(term.getLabelSelector());
            properties.add(new AffinityTermProperties(namespaces, selector));
        }
        return properties;
    }

    private boolean satisfiesPodsAffinityAntiAffinity(Pod pod, Node node, Affinity affinity, Cache cache) {
        // k8s 1.13中这里的pods除去了“nodeName等于当前node但不在nodeInfo中显示的pod”，是抢占机制造成的这种pod。这里忽略抢占。
        Map<String, Pod> existingPods = cache.getPodMap();
        List<PodAffinityTerm> affinityTerms = getPodAffinityTerms(affinity.getPodAffinity());
        List<PodAffinityTerm> antiAffinityTerms = getPodAntiAffinityTerms(affinity.getPodAntiAffinity());
        boolean matchFound = false, termsSelectorMatchFound = false;
        for (Pod targetPod : existingPods.values()) {
            // Check all affinity terms.
            if (!matchFound && !affinityTerms.isEmpty()) {
                boolean[] res = podMatchesPodAffinityTerms(pod, targetPod, node, affinityTerms, cache);
                boolean affTermsMatch = res[0], termsSelectorMatch = res[1];
                if (termsSelectorMatch) {
                    termsSelectorMatchFound = true;
                }
                if (affTermsMatch) {
                    matchFound = true;
                }
            }
            if (antiAffinityTerms != null && !antiAffinityTerms.isEmpty()) {
                boolean antiAffTermsMatch;
                try {
                    antiAffTermsMatch = podMatchesPodAffinityTerms(pod, targetPod, node, antiAffinityTerms, cache)[0];
                } catch (Exception e) {
                    antiAffTermsMatch = true;
                }
                if (antiAffTermsMatch) {
                    return false;
                }
            }
        }
        if (!matchFound && affinityTerms != null && !affinityTerms.isEmpty()) {
            // We have not been able to find any matches for the pod's affinity terms.
            // This pod may be the first pod in a series that have affinity to themselves. In order
            // to not leave such pods in pending state forever, we check that if no other pod
            // in the cluster matches the namespace and selector of this pod and the pod matches
            // its own terms, then we allow the pod to pass the affinity check.
            if (termsSelectorMatchFound) {
                return false;
            }
            // Check if pod matches its own affinity properties (namespace and label selector).
            if (!targetPodMatchesAffinityOfPod(pod, pod)) {
                return false;
            }
        }
        return true;
    }

    private boolean targetPodMatchesAffinityOfPod(Pod pod, Pod targetPod) {
        //TODO get affinity of pod
        Affinity affinity = new Affinity();
        if (affinity == null || affinity.getPodAffinity() == null) {
            return false;
        }
        List<AffinityTermProperties> affinityProperties = getAffinityTermProperties(pod, getPodAffinityTerms(affinity.getPodAffinity()));
        return podMatchesAllAffinityTermProperties(targetPod, affinityProperties);
    }

    private List<PodAffinityTerm> getPodAffinityTerms(PodAffinity podAffinity) {
        if (podAffinity != null) {
            List<PodAffinityTerm> terms = podAffinity.getRequiredDuringSchedulingIgnoredDuringExecution();
            if (terms != null && !terms.isEmpty()) {
                return terms;
            }
        }
        return new ArrayList<>();
    }

    private List<PodAffinityTerm> getPodAntiAffinityTerms(PodAntiAffinity podAntiAffinity) {
        if (podAntiAffinity != null) {
            List<PodAffinityTerm> terms = podAntiAffinity.getRequiredDuringSchedulingIgnoredDuringExecution();
            if (terms != null && !terms.isEmpty()) {
                return terms;
            }
        }
        return new ArrayList<>();
    }

    private boolean satisfiesExistingPodsAntiAffinity(Pod pod, Node node, Cache cache) {
        // k8s 1.13中这里的pods除去了“nodeName等于当前node但不在nodeInfo中显示的pod”，是抢占机制造成的这种pod。这里忽略抢占。
        Map<String, Pod> existingPods = cache.getPodMap();
        TopologyPairsMaps topologyMaps = getMatchingAntiAffinityTopologyPairsOfPods(pod, existingPods, cache);
        // Iterate over topology pairs to get any of the pods being affected by
        // the scheduled pod anti-affinity terms
        Map<String, String> nodeLabels = new HashMap<>();
        for (Map.Entry<String, String> entry : nodeLabels.entrySet()) {
            TopologyPair pair = new TopologyPair(entry.getKey(), entry.getValue());
            if (topologyMaps.getTopologyPairToPods().containsKey(pair)) {
                return false;
            }
        }
        return true;
    }

    private TopologyPairsMaps getMatchingAntiAffinityTopologyPairsOfPods(Pod pod, Map<String, Pod> existingPods, Cache cache) {
        TopologyPairsMaps topologyPairsMaps = new TopologyPairsMaps();
        for (Pod existingPod : existingPods.values()) {
            Node existingPodNode = cache.getNodeMap().get(existingPod.getNodeName());
            TopologyPairsMaps existingPodTopologyMaps = getMatchingAntiAffinityTopologyPairsOfPod(pod, existingPod, existingPodNode, cache);
            topologyPairsMaps.appendMaps(existingPodTopologyMaps);
        }
        return topologyPairsMaps;
    }

    private TopologyPairsMaps getMatchingAntiAffinityTopologyPairsOfPod(Pod newPod, Pod existingPod, Node node, Cache cache) {
        TopologyPairsMaps topologyMaps = new TopologyPairsMaps();
        Affinity affinity = new Affinity(); //TODO = existingPods.getAffinity();
        if (affinity == null || affinity.getPodAffinity() == null) {
            return null;
        }
        List<PodAffinityTerm> terms = affinity.getPodAffinity().getRequiredDuringSchedulingIgnoredDuringExecution();
        if (terms != null && !terms.isEmpty()) {
            for (PodAffinityTerm term : terms) {
                Set<String> namespaces = getNamespacesFromPodAffinityTerm(existingPod, term);
                Selector selector = labelSelectorAsSelector(term.getLabelSelector());
                if (podMatchesTermsNamespaceAndSelector(newPod, namespaces, selector)) {
                    Map<String, String> nodeLabels = new HashMap<>(); // todo get node labels
                    if (nodeLabels.containsKey(term.getTopologyKey())) {
                        TopologyPair pair = new TopologyPair(term.getTopologyKey(), nodeLabels.get(term.getTopologyKey()));
                        topologyMaps.addTopologyPair(pair, existingPod);
                    }
                }
            }
        }
        return topologyMaps;
    }

    private boolean podMatchesTermsNamespaceAndSelector(Pod pod, Set<String> namespaces, Selector selector) {
        if (!namespaces.contains(pod.getNamespace())) {
            return false;
        }
        //TODO get pod labels
        Map<String, String> podLabels = new HashMap<>();
        if (!selector.matches(podLabels)) {
            return false;
        }
        return true;
    }

    private Set<String> getNamespacesFromPodAffinityTerm(Pod pod, PodAffinityTerm term) {
        Set<String> names = new HashSet<>();
        if (term.getNamespaces().length == 0) {
            names.add(pod.getNamespace());
        } else {
            names.addAll(Arrays.asList(term.getNamespaces()));
        }
        return names;
    }

    class NothingSelector implements Selector {
        public boolean matches(Map<String, String> labels) {
            return false;
        }

        public boolean isEmpty() {
            return false;
        }

        public boolean add(Requirement... r) {
            return false;
        }

        public List<Requirement> Requirements() {
            return null;
        }
    }

    private Selector labelSelectorAsSelector(LabelSelector ps) {
        if (ps == null) {
            return new NothingSelector();
        }
        if (ps.getMatchLabels().isEmpty() && ps.getMatchExpressions().isEmpty()) {
            return new InternalSelector();
        }
        Selector selector = new InternalSelector();
        for (Map.Entry<String, String> entry : ps.getMatchLabels().entrySet()) {
            String k = entry.getKey();
            String[] vs = new String[1];
            vs[0] = entry.getValue();
            Requirement r = PodMatchNodeSelectorPredicate.newRequirement(k, SelectOperation.Equals, vs);
            selector.add(r);
        }
        for (LabelSelectorRequirement expr : ps.getMatchExpressions()) {
            SelectOperation op;
            switch (expr.getOperator()) {
                case LabelSelectorOpIn:
                    op = SelectOperation.In;
                    break;
                case LabelSelectorOpNotIn:
                    op = SelectOperation.NotIn;
                    break;
                case LabelSelectorOpExists:
                    op = SelectOperation.Exists;
                    break;
                case LabelSelectorOpDoesNotExist:
                    op = SelectOperation.DoesNotExist;
                    break;
                default:
                    return null;
            }
            Requirement r = PodMatchNodeSelectorPredicate.newRequirement(expr.getKey(), op, expr.getValues());
            selector.add(r);
        }
        return selector;
    }
}
