package cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.CheckNodeConditionPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.CheckNodeDiskPressurePredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.CheckNodeMemoryPressurePredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.CheckNodePIDPressurePredicate;
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
import cn.harmonycloud.schedulingalgorithm.priority.impl.InterPodAffinityPriority;
import cn.harmonycloud.schedulingalgorithm.priority.impl.NodeAffinityPriority;
import cn.harmonycloud.schedulingalgorithm.priority.impl.NodeLoadForecastPriority;
import cn.harmonycloud.schedulingalgorithm.priority.impl.RequestedPriority;
import cn.harmonycloud.schedulingalgorithm.priority.impl.SelectorSpreadPriority;
import cn.harmonycloud.schedulingalgorithm.priority.impl.TaintTolerationPriority;
import cn.harmonycloud.schedulingalgorithm.selecthost.SelectHostRule;
import cn.harmonycloud.schedulingalgorithm.selecthost.impl.RoundRobinSelectHighest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultGreedyAlgorithm implements GreedyAlgorithm {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultGreedyAlgorithm.class);

    private PresortRule presortRule;

    private List<PredicateRule> predicateRules;
    private List<PredicateRule> predicateRulesOnDelete;

    private List<PriorityRuleConfig> priorityRuleConfigs;
    private List<PriorityRuleConfig> priorityRuleConfigsOnDelete;

    private SelectHostRule selectHostRule;

    public DefaultGreedyAlgorithm() {
        /*
        预处理策略
         */
        presortRule = new DecreasingSortRule();

        /*
        预选策略 推荐顺序
        The order is based on the restrictiveness & complexity of predicates.
        */
        predicateRules = new ArrayList<>();
        predicateRules.add(new CheckNodeConditionPredicate());
        predicateRules.add(new PodFitsResourcesPredicate());
        predicateRules.add(new PodFitsHostPortsPredicate());
        predicateRules.add(new PodMatchNodeSelectorPredicate());
        predicateRules.add(new PodToleratesNodeTaintsPredicate());
//        弃用predicateRules.add(new CheckVolumeBindingPredicate());
        predicateRules.add(new CheckNodeMemoryPressurePredicate());
        predicateRules.add(new CheckNodePIDPressurePredicate());
        predicateRules.add(new CheckNodeDiskPressurePredicate());
        predicateRules.add(new MatchInterPodAffinityPredicate());

        predicateRulesOnDelete = new ArrayList<>();
        predicateRulesOnDelete.add(new PodExistingOnNodePredicate());

        /*
        优选策略 没有顺序
        */
        priorityRuleConfigs = new ArrayList<>();
        priorityRuleConfigs.add(new PriorityRuleConfig(new RequestedPriority(Constants.OPERATION_ADD), 1));
        priorityRuleConfigs.add(new PriorityRuleConfig(new BalancedResourceAllocationPriority(Constants.OPERATION_ADD), 1));
//        弃用priorityRuleConfigs.add(new PriorityRuleConfig(new NodePreferAvoidPodsPriority(Constants.OPERATION_ADD), 10000));
        priorityRuleConfigs.add(new PriorityRuleConfig(new NodeAffinityPriority(), 1));
        priorityRuleConfigs.add(new PriorityRuleConfig(new TaintTolerationPriority(), 1));
//        弃用priorityRuleConfigs.add(new PriorityRuleConfig(new ImageLocalityPriority(), 1));
        priorityRuleConfigs.add(new PriorityRuleConfig(new SelectorSpreadPriority(Constants.OPERATION_ADD), 1));
        priorityRuleConfigs.add(new PriorityRuleConfig(new InterPodAffinityPriority(), 1));
        // comment out when debugging
        priorityRuleConfigs.add(new PriorityRuleConfig(new NodeLoadForecastPriority(Constants.OPERATION_ADD), 1));

        priorityRuleConfigsOnDelete = new ArrayList<>();
        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new RequestedPriority(Constants.OPERATION_DELETE), 1));
        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new BalancedResourceAllocationPriority(Constants.OPERATION_DELETE), 1));
//        弃用priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new NodePreferAvoidPodsPriority(Constants.OPERATION_DELETE), 10000));
        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new SelectorSpreadPriority(Constants.OPERATION_DELETE), 1));
        priorityRuleConfigsOnDelete.add(new PriorityRuleConfig(new NodeLoadForecastPriority(Constants.OPERATION_DELETE), 1));

        /*
        host选择策略
        */
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
        LOGGER.info("start presort!");
        try {
            return presortRule.sort(pods, cache);
        } catch (Exception e) {
            LOGGER.debug("presort error:" + presortRule);
            e.printStackTrace();
            return pods;
        }
    }

    @Override
    public List<Node> predicates(Pod pod, Cache cache) {
        // 分别处理各个节点
        long enough = Long.MAX_VALUE;
        return cache.getNodeList().stream()
                .filter(node -> runAllPredicates(pod, node, cache))
                .limit(enough)
                .collect(Collectors.toList());
    }

    private boolean runAllPredicates(Pod pod, Node node, Cache cache) {
        // 分别处理各个预选规则，预选规则大概只有10个，规则判断不会很耗时
        List<PredicateRule> rules = pod.getOperation().equals(Constants.OPERATION_DELETE) ? predicateRulesOnDelete : predicateRules;
        return rules.stream().allMatch(rule -> {
            try {
                return rule.predicate(pod, node, cache);
            } catch (Exception e) {
                LOGGER.debug("predicate rule error:" + rule);
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public List<HostPriority> priorities(Pod pod, List<Node> nodes, Cache cache) {
        List<HostPriority> result = new ArrayList<>();
        // 分别处理各个优选规则，优选规则大概只有10个
        List<PriorityRuleConfig> configs = pod.getOperation().equals(Constants.OPERATION_DELETE) ? priorityRuleConfigsOnDelete : priorityRuleConfigs;
        Optional<List<Integer>> optional = configs.stream()
                .filter(config -> !config.getWeight().equals(0))
                .map(config -> markFilteredNodes(pod, nodes, config, cache))
                .reduce(this::mergeScoreList);
        List<Integer> scores = optional.orElse(new ArrayList<>());
        for (int i = 0; i < nodes.size(); i++) {
            Integer score = i < scores.size() ? scores.get(i) : 0;
            result.add(new HostPriority(nodes.get(i).getNodeName(), score));
        }
        return result;
    }

    private List<Integer> markFilteredNodes(Pod pod, List<Node> nodes, PriorityRuleConfig config, Cache cache) {
        try {
            // 优选规则内部处理各个node，可以自己决定是否对各个node的评分并发处理
            List<Integer> list = config.getPriorityRule().priority(pod, nodes, cache);
            // 计算权重后的得分
            int weight = config.getWeight();
            return list.stream().map(score -> weight * score).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.debug("priority rule error:" + config.getPriorityRule());
            e.printStackTrace();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < cache.getNodeList().size(); i++) {
                list.add(0);
            }
            return list;
        }
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
