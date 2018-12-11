package cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm;

import cn.harmonycloud.schedulingalgorithm.algorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.PodFitsResources;
import cn.harmonycloud.schedulingalgorithm.presort.PresortRule;
import cn.harmonycloud.schedulingalgorithm.presort.impl.DecreasingSortRule;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRuleConfig;
import cn.harmonycloud.schedulingalgorithm.priority.impl.LeastRequestedPriority;
import cn.harmonycloud.schedulingalgorithm.selecthost.SelectHostRule;
import cn.harmonycloud.schedulingalgorithm.selecthost.impl.RoundRobinSelectHighest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultGreedyAlgorithm implements GreedyAlgorithm {
    private Cache cache;
    private PresortRule presortRule;
    private List<PredicateRule> predicateRules;
    private List<PriorityRuleConfig> priorityRuleConfigs;
    private SelectHostRule selectHostRule;

    public DefaultGreedyAlgorithm() {
        cache = new Cache();
        presortRule = new DecreasingSortRule();
        predicateRules = new ArrayList<>();
        predicateRules.add(new PodFitsResources());
        //TODO more predicateRules
        priorityRuleConfigs = new ArrayList<>();
        priorityRuleConfigs.add(new PriorityRuleConfig(new LeastRequestedPriority(), 1.0));
        //TODO more priorityRuleConfigs
        selectHostRule = new RoundRobinSelectHighest();
    }

    @Override
    public List<Pod> presort(List<Pod> pods) {
        return presortRule.sort(pods, getCache());
    }

    @Override
    public List<Node> predicates(Pod pod) {
        // 分别处理各个节点 TODO: parallelStream or ExecutorCompletionService
        long enough = Long.MAX_VALUE;
        return cache.getNodeList().stream()
                .filter(node -> runAllPredicates(pod, node))
                .limit(enough)
                .collect(Collectors.toList());
    }

    private boolean runAllPredicates(Pod pod, Node node) {
        // 分别处理各个预选规则，预选规则大概只有10个，规则判断不会很耗时
        Cache cache = getCache();
        return predicateRules.stream().allMatch(rule -> rule.predicate(pod, node));
    }

    @Override
    public List<HostPriority> priorities(Pod pod, List<Node> nodes) {
        List<HostPriority> result = new ArrayList<>();
        // 分别处理各个优选规则，优选规则大概只有10个
        Optional<List<Double>> optional = priorityRuleConfigs.stream()
                .filter(config -> !config.getWeight().equals(0D))
                .map(config -> markAllNodes(pod, config))
                .reduce(this::mergeScoreList);
        List<Double> scores = optional.orElse(new ArrayList<>());
        for (int i = 0; i < nodes.size(); i++) {
            Double score = i < scores.size() ? scores.get(i) : 0D;
            result.add(new HostPriority(nodes.get(i).getNodeName(), score));
        }
        return result;
    }

    private List<Double> markAllNodes(Pod pod, PriorityRuleConfig config) {
        // 优选规则内部处理各个node，可以自己决定是否对各个node的评分并发处理
        List<Integer> list = config.getPriorityRule().priority(pod, cache.getNodeList());
        // 计算权重后的得分
        double weight = config.getWeight();
        return list.stream().map(score -> weight * score).collect(Collectors.toList());
    }

    private List<Double> mergeScoreList(List<Double> list1, List<Double> list2) {
        if (list1.size() != list2.size()) {
            // 不足的补0，逻辑上不会走到这里
            completeList(list1, list2);
        }
        int length = list2.size();
        for (int i = 0; i < length; i++) {
            list2.set(i, list1.get(i) + list2.get(i));
        }
        return list2;
    }

    private void completeList(List<Double> list1, List<Double> list2) {
        if (list1.size() < list2.size()) {
            List<Double> temp = list1;
            list1 = list2;
            list2 = temp;
        }
        int num = list1.size() - list2.size();
        for (int i = 0; i < num; i++) {
            list2.add(0D);
        }
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
