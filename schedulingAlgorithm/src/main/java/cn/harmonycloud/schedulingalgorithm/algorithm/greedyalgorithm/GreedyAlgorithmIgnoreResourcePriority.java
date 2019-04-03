package cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm;

import cn.harmonycloud.schedulingalgorithm.priority.PriorityRuleConfig;
import cn.harmonycloud.schedulingalgorithm.priority.impl.BalancedResourceAllocationPriority;
import cn.harmonycloud.schedulingalgorithm.priority.impl.RequestedPriority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GreedyAlgorithmIgnoreResourcePriority extends DefaultGreedyAlgorithm {
    protected static List<Class> ignorePriorityClasses = Arrays.asList(new Class[]{RequestedPriority.class, BalancedResourceAllocationPriority.class});

    protected List<PriorityRuleConfig> ignoredPriorityRuleConfigs = new ArrayList<>();

    public GreedyAlgorithmIgnoreResourcePriority() {
        super();
        deleteIgnoredPriorities(priorityRuleConfigs, true);
        deleteIgnoredPriorities(priorityRuleConfigsOnDelete, false);
    }

    private void deleteIgnoredPriorities(List<PriorityRuleConfig> configs, boolean needAdd) {
        configs.removeIf(config -> {
            boolean match = ignorePriorityClasses.stream().anyMatch(c -> c.isInstance(config.getPriorityRule()));
            if (match & needAdd) {
                ignoredPriorityRuleConfigs.add(config);
            }
            return match;
        });
    }

    public List<PriorityRuleConfig> getIgnoredPriorityRuleConfigs() {
        return ignoredPriorityRuleConfigs;
    }
}
