package cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm;

import cn.harmonycloud.schedulingalgorithm.priority.PriorityRuleConfig;
import cn.harmonycloud.schedulingalgorithm.priority.impl.BalancedResourceAllocationPriority;
import cn.harmonycloud.schedulingalgorithm.priority.impl.RequestedPriority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GreedyAlgorithmOnlyResourcePriority extends DefaultGreedyAlgorithm {
    protected static List<Class> resourcePriorityClasses = Arrays.asList(new Class[]{RequestedPriority.class, BalancedResourceAllocationPriority.class});

    public GreedyAlgorithmOnlyResourcePriority() {
        super();
        deleteIgnoredPriorities(priorityRuleConfigs, true);
        deleteIgnoredPriorities(priorityRuleConfigsOnDelete, false);
    }

    private void deleteIgnoredPriorities(List<PriorityRuleConfig> configs, boolean needAdd) {
        configs.removeIf(config ->
                resourcePriorityClasses.stream().noneMatch(c -> c.isInstance(config.getPriorityRule()))
        );
    }
}
