package cn.harmonycloud.schedulingalgorithm.priority;

public class PriorityRuleConfig {
    private PriorityRule priorityRule;
    private Integer weight;

    public PriorityRuleConfig(PriorityRule priorityRule, Integer weight) {
        this.priorityRule = priorityRule;
        this.weight = weight;
    }

    public PriorityRule getPriorityRule() {
        return priorityRule;
    }

    public void setPriorityRule(PriorityRule priorityRule) {
        this.priorityRule = priorityRule;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
