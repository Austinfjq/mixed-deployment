package cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm;

import cn.harmonycloud.schedulingalgorithm.algorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.PodFitsResources;
import cn.harmonycloud.schedulingalgorithm.presort.PresortRule;
import cn.harmonycloud.schedulingalgorithm.presort.impl.DecreasingSortRule;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRule;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRuleConfig;
import cn.harmonycloud.schedulingalgorithm.selecthost.SelectHostRule;
import cn.harmonycloud.schedulingalgorithm.selecthost.impl.RoundRobinSelectHighest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultGreedyAlgorithm implements GreedyAlgorithm {
    private Cache cache;
    private PresortRule presortRule;
    private List<PredicateRule> predicateRules;
    private List<PriorityRuleConfig> priorityRuleConfigs;
    private SelectHostRule selectHostRule;

    public DefaultGreedyAlgorithm() {
        presortRule = new DecreasingSortRule();
        predicateRules = new ArrayList<>();
        predicateRules.add(new PodFitsResources());
        //TODO
        priorityRuleConfigs = new ArrayList<>();
        //TODO
        cache = new Cache();
        selectHostRule = new RoundRobinSelectHighest();
    }

    @Override
    public List<Pod> presort(List<Pod> pods) {
        return presortRule.sort(pods);
    }

    @Override
    public List<Node> predicates(Pod pod, List<Node> nodes) {
        List<Node> result = new ArrayList<>();
        // TODO 可并行化
        for (Node node : nodes) {
            boolean fit = true;
            for (PredicateRule predicateRule : predicateRules) {
                fit = predicateRule.predicate(pod, node);
                if (!fit) {
                    break;
                }
            }
            if (fit) {
                result.add(node);
            }
//            if (enough number node) break;
        }
        return result;
    }

    @Override
    public List<HostPriority> priorities(Pod pod, List<Node> nodes) {
        List<HostPriority> result = new ArrayList<>();
        Map<String, Double> nodeCombinedScoresMap = new HashMap<>();
        // TODO 可并行化
        for (PriorityRuleConfig priorityRuleConfig : priorityRuleConfigs) {
            Double weight = priorityRuleConfig.getWeight();
            PriorityRule priorityRule = priorityRuleConfig.getPriorityRule();
            // TODO if weight == 0, ...
            List<HostPriority> hostPriorityListOfOneRule = priorityRule.priority(pod, nodes);
            for (HostPriority hostPriority : hostPriorityListOfOneRule) {
                String nodeName = hostPriority.getHost();
                Double oldScore = nodeCombinedScoresMap.getOrDefault(nodeName, 0D);
                nodeCombinedScoresMap.put(nodeName, oldScore + hostPriority.getScore());
            }
        }
        return result;
    }

    @Override
    public Cache getCache() {
        return cache;
    }

    @Override
    public List<HostPriority> selectHost(List<HostPriority> hostPriorityList) {
        return selectHostRule.selectHost(hostPriorityList);
    }
}
