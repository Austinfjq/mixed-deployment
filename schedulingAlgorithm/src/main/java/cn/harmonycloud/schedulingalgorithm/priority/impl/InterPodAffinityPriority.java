package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.affinity.Affinity;
import cn.harmonycloud.schedulingalgorithm.affinity.PodAffinityTerm;
import cn.harmonycloud.schedulingalgorithm.affinity.Selector;
import cn.harmonycloud.schedulingalgorithm.affinity.WeightedPodAffinityTerm;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRule;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class InterPodAffinityPriority implements PriorityRule {
    class Context {
        Map<String, Double> counts = new HashMap<>();
        ReentrantLock lock = new ReentrantLock();
        Pod pod;
        Affinity affinity;
        boolean hasAffinityConstraints;
        boolean hasAntiAffinityConstraints;
        Cache cache;
    }

    @Override
    public List<Integer> priority(Pod pod, List<Node> nodes, Cache cache) {
        return calculateInterPodAffinityPriority(pod, nodes, cache);
    }

    private List<Integer> calculateInterPodAffinityPriority(Pod pod, List<Node> nodes, Cache cache) {
        Affinity affinity = pod.getAffinityObject();
        boolean hasAffinityConstraints = affinity != null && affinity.getPodAffinity() != null;
        boolean hasAntiAffinityConstraints = affinity != null && affinity.getPodAntiAffinity() != null;

        Context context = new Context();
        context.hasAffinityConstraints = hasAffinityConstraints;
        context.hasAntiAffinityConstraints = hasAntiAffinityConstraints;
        context.pod = pod;
        context.affinity = affinity;
        context.cache = cache;

        (GlobalSetting.PARALLEL ? nodes.parallelStream() : nodes.stream()).forEach(n -> {
            context.counts.put(n.getNodeName(), 0D);
            processNode(context, n);
        });

        double maxCount = -Double.MAX_VALUE;
        double minCount = Double.MAX_VALUE;
        for (Node node : nodes) {
            if (context.counts.get(node.getNodeName()) > maxCount) {
                maxCount = context.counts.get(node.getNodeName());
            }
            if (context.counts.get(node.getNodeName()) < minCount) {
                minCount = context.counts.get(node.getNodeName());
            }
        }

        List<Integer> result = new ArrayList<>();
        for (Node node : nodes) {
            double fScore = 0;
            if (maxCount - minCount > 0) {
                fScore = (double) (GlobalSetting.PRIORITY_MAX_SCORE) * ((context.counts.get(node.getNodeName()) - minCount) / (maxCount - minCount));
            }
            result.add((int) fScore);
        }
        return result;
    }

    private void processNode(Context context, Node node) {
        List<Pod> nodePods = context.cache.getNodeMapPodList().get(node.getNodeName());
        if (context.hasAffinityConstraints || context.hasAntiAffinityConstraints) {
            for (Pod existingPod : nodePods) {
                processPod(context, existingPod);
            }
        } else {
            nodePods = nodePods.stream().filter(p -> p.getAffinity() != null && (p.getAffinity().getPodAffinity() != null || p.getAffinity().getPodAntiAffinity() != null)).collect(Collectors.toList());
            for (Pod existingPod : nodePods) {
                processPod(context, existingPod);
            }
        }
    }

    private void processPod(Context context, Pod existingPod) {
        Node existingPodNode = context.cache.getNodeMap().get(existingPod.getNodeName());
        Affinity existingPodAffinity = existingPod.getAffinityObject();
        boolean existingHasAffinityConstraints = existingPodAffinity != null && existingPodAffinity.getPodAffinity() != null;
        boolean existingHasAntiAffinityConstraints = existingPodAffinity != null && existingPodAffinity.getPodAntiAffinity() != null;
        if (context.hasAffinityConstraints) {
            List<WeightedPodAffinityTerm> terms = Arrays.asList(context.affinity.getPodAffinity().getPreferredDuringSchedulingIgnoredDuringExecution());
            processTerms(context, terms, context.pod, existingPod, existingPodNode, 1);
        }
        if (context.hasAntiAffinityConstraints) {
            List<WeightedPodAffinityTerm> terms = Arrays.asList(context.affinity.getPodAntiAffinity().getPreferredDuringSchedulingIgnoredDuringExecution());
            processTerms(context, terms, context.pod, existingPod, existingPodNode, -1);
        }
        if (existingHasAffinityConstraints) {
            int hardPodAffinityWeight = 1; // DefaultHardPodAffinitySymmetricWeight = 1, but no idea about where to find modified value
            if (hardPodAffinityWeight > 0) {
                PodAffinityTerm[] terms = existingPodAffinity.getPodAffinity().getRequiredDuringSchedulingIgnoredDuringExecution();
                for (PodAffinityTerm term : terms) {
                    processTerm(context, term, existingPod, context.pod, existingPodNode, (double) hardPodAffinityWeight);
                }
            }
            List<WeightedPodAffinityTerm> terms = Arrays.asList(existingPodAffinity.getPodAffinity().getPreferredDuringSchedulingIgnoredDuringExecution());
            processTerms(context, terms, existingPod, context.pod, existingPodNode, 1);
        }
        if (existingHasAntiAffinityConstraints) {
            List<WeightedPodAffinityTerm> terms = Arrays.asList(existingPodAffinity.getPodAntiAffinity().getPreferredDuringSchedulingIgnoredDuringExecution());
            processTerms(context, terms, existingPod, context.pod, existingPodNode, -1);
        }
    }

    private void processTerms(Context context, List<WeightedPodAffinityTerm> terms, Pod podDefiningAffinityTerm, Pod podToCheck, Node fixedNode, int multiplier) {
        for (WeightedPodAffinityTerm term : terms) {
            processTerm(context, term.getPodAffinityTerm(), podDefiningAffinityTerm, podToCheck, fixedNode, (double) (term.getWeight() * multiplier));
        }
    }

    private void processTerm(Context context, PodAffinityTerm term, Pod podDefiningAffinityTerm, Pod podToCheck, Node fixedNode, double weight) {
        Set<String> namespaces = RuleUtil.getNamespacesFromPodAffinityTerm(podDefiningAffinityTerm, term);
        Selector selector = RuleUtil.labelSelectorAsSelector(term.getLabelSelector());
        boolean match = RuleUtil.podMatchesTermsNamespaceAndSelector(podToCheck, namespaces, selector);
        if (match) {
            context.lock.lock();
            try {
                for (Node node : context.cache.getNodeList()) {
                    if (RuleUtil.nodesHaveSameTopologyKey(node, fixedNode, term.getTopologyKey())) {
                        double old = context.counts.get(node.getNodeName());
                        context.counts.put(node.getNodeName(), old + weight);
                    }
                }
            } finally {
                context.lock.unlock();
            }
        }
    }
}
