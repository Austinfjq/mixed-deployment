package cn.harmonycloud.schedulingalgorithm.algorithm;

import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.presort.PresortRule;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRuleConfig;
import cn.harmonycloud.schedulingalgorithm.selecthost.SelectHostRule;

import java.util.List;

public class AlgorithmTest {
    private PresortRule presortRule;

    private List<PredicateRule> predicateRules;
    private List<PredicateRule> predicateRulesOnDelete;

    private List<PriorityRuleConfig> priorityRuleConfigs;
    private List<PriorityRuleConfig> priorityRuleConfigsOnDelete;

    private SelectHostRule selectHostRule;

    public AlgorithmTest() {
    }
}
