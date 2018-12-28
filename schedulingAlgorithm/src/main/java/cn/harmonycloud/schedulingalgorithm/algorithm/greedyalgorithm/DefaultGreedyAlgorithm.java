package cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.CheckNodeDiskPressurePredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.CheckNodeMemoryPressurePredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.MatchInterPodAffinityPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.PodFitsHostPortsPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.PodFitsResourcesPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.PodMatchNodeSelectorPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.PodToleratesNodeTaintsPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.delete.PodExistingOnNodePredicate;
import cn.harmonycloud.schedulingalgorithm.presort.PresortRule;
import cn.harmonycloud.schedulingalgorithm.presort.impl.DecreasingSortRule;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRuleConfig;
import cn.harmonycloud.schedulingalgorithm.priority.impl.BalancedResourceAllocationPriority;
import cn.harmonycloud.schedulingalgorithm.priority.impl.RequestedPriority;
import cn.harmonycloud.schedulingalgorithm.priority.impl.SelectorSpreadPriority;
import cn.harmonycloud.schedulingalgorithm.selecthost.SelectHostRule;
import cn.harmonycloud.schedulingalgorithm.selecthost.impl.RoundRobinSelectHighest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultGreedyAlgorithm implements GreedyAlgorithm {
    private PresortRule presortRule;

    private List<PredicateRule> predicateRules;
    private List<PredicateRule> predicateRulesOnDelete;

    private List<PriorityRuleConfig> priorityRuleConfigs;
    private List<PriorityRuleConfig> priorityRuleConfigsOnDelete;

    private SelectHostRule selectHostRule;

    public DefaultGreedyAlgorithm() {
        presortRule = new DecreasingSortRule();

        predicateRules = new ArrayList<>();
        predicateRules.add(new PodFitsResourcesPredicate());
        predicateRules.add(new PodFitsHostPortsPredicate());
        predicateRules.add(new PodMatchNodeSelectorPredicate());
        predicateRules.add(new PodToleratesNodeTaintsPredicate());
        predicateRules.add(new CheckNodeMemoryPressurePredicate());
        predicateRules.add(new CheckNodeDiskPressurePredicate());
        predicateRules.add(new MatchInterPodAffinityPredicate());

        predicateRulesOnDelete = new ArrayList<>();
        predicateRulesOnDelete.add(new PodExistingOnNodePredicate());

        priorityRuleConfigs = new ArrayList<>();
        priorityRuleConfigs.add(new PriorityRuleConfig(new RequestedPriority(Constants.OPERATION_ADD), 1));
        priorityRuleConfigs.add(new PriorityRuleConfig(new BalancedResourceAllocationPriority(Constants.OPERATION_ADD), 1));
//        priorityRuleConfigs.add(new PriorityRuleConfig(new NodePreferAvoidPodsPriority(), 10000));
//        priorityRuleConfigs.add(new PriorityRuleConfig(new NodeAffinityPriority(), 1));
//        priorityRuleConfigs.add(new PriorityRuleConfig(new TaintTolerationPriority(), 1));
//        priorityRuleConfigs.add(new PriorityRuleConfig(new ImageLocalityPriority(), 1));
        priorityRuleConfigs.add(new PriorityRuleConfig(new SelectorSpreadPriority(Constants.OPERATION_ADD), 1));
//        priorityRuleConfigs.add(new PriorityRuleConfig(new InterPodAffinityPriority(), 1));
//        priorityRuleConfigs.add(new PriorityRuleConfig(new NodeLoadForecastPriority(), 1));

        priorityRuleConfigsOnDelete = new ArrayList<>();
        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new RequestedPriority(Constants.OPERATION_DELETE), 1));
        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new BalancedResourceAllocationPriority(Constants.OPERATION_DELETE), 1));
//        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new NodePreferAvoidPodsPriority(), 10000));
//        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new NodeAffinityPriority(), 1));
//        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new TaintTolerationPriority(), 1));
//        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new ImageLocalityPriority(), 1));
        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new SelectorSpreadPriority(Constants.OPERATION_DELETE), 1));
//        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new InterPodAffinityPriority(), 1));
//        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new NodeLoadForecastPriority(), 1));

        selectHostRule = new RoundRobinSelectHighest();
    }

    public DefaultGreedyAlgorithm(PresortRule presortRule,
                                  List<PredicateRule> predicateRules,
                                  List<PredicateRule> predicateRulesOnDelete,
                                  List<PriorityRuleConfig> priorityRuleConfigs,
                                  List<PriorityRuleConfig> priorityRuleConfigsOnDelete,
                                  SelectHostRule selectHostRule) {
        this.presortRule = presortRule;
        this.predicateRules = predicateRules;
        this.predicateRulesOnDelete = predicateRulesOnDelete;
        this.priorityRuleConfigs = priorityRuleConfigs;
        this.priorityRuleConfigsOnDelete = priorityRuleConfigsOnDelete;
        this.selectHostRule = selectHostRule;
    }

    @Override
    public List<Pod> presort(List<Pod> pods, Cache cache) {
        return presortRule.sort(pods, cache);
    }

    @Override
    public List<Node> predicates(Pod pod, Cache cache) {
        // 分别处理各个节点 TODO: parallelStream or ExecutorCompletionService
        long enough = Long.MAX_VALUE;
        return cache.getNodeList().stream()
                .filter(node -> runAllPredicates(pod, node, cache))
                .limit(enough)
                .collect(Collectors.toList());
    }

    private boolean runAllPredicates(Pod pod, Node node, Cache cache) {
        // 分别处理各个预选规则，预选规则大概只有10个，规则判断不会很耗时
        List<PredicateRule> rules = pod.getOperation().equals(Constants.OPERATION_DELETE) ? predicateRulesOnDelete : predicateRules;
        return rules.stream().allMatch(rule -> rule.predicate(pod, node, cache));
    }

    @Override
    public List<HostPriority> priorities(Pod pod, List<Node> nodes, Cache cache) {
        List<HostPriority> result = new ArrayList<>();
        // 分别处理各个优选规则，优选规则大概只有10个
        List<PriorityRuleConfig> configs = pod.getOperation().equals(Constants.OPERATION_DELETE) ? priorityRuleConfigsOnDelete : priorityRuleConfigs;
        Optional<List<Integer>> optional = configs.stream()
                .filter(config -> !config.getWeight().equals(0))
                .map(config -> markAllNodes(pod, config, cache))
                .reduce(this::mergeScoreList);
        List<Integer> scores = optional.orElse(new ArrayList<>());
        for (int i = 0; i < nodes.size(); i++) {
            Integer score = i < scores.size() ? scores.get(i) : 0;
            result.add(new HostPriority(nodes.get(i).getNodeName(), score));
        }
        return result;
    }

    private List<Integer> markAllNodes(Pod pod, PriorityRuleConfig config, Cache cache) {
        // 优选规则内部处理各个node，可以自己决定是否对各个node的评分并发处理
        List<Integer> list = config.getPriorityRule().priority(pod, cache.getNodeList(), cache);
        // 计算权重后的得分
        int weight = config.getWeight();
        return list.stream().map(score -> weight * score).collect(Collectors.toList());
    }

    private List<Integer> mergeScoreList(List<Integer> list1, List<Integer> list2) {
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

    private void completeList(List<Integer> list1, List<Integer> list2) {
        if (list1.size() < list2.size()) {
            List<Integer> temp = list1;
            list1 = list2;
            list2 = temp;
        }
        int num = list1.size() - list2.size();
        for (int i = 0; i < num; i++) {
            list2.add(0);
        }
    }

    /**
     * 从打分列表中选取一个节点
     */
    @Override
    public HostPriority selectHost(List<HostPriority> hostPriorityList, Cache cache) {
        return selectHostRule.selectHost(hostPriorityList);
    }
}
