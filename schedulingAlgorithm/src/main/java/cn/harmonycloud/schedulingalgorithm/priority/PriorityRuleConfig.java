package cn.harmonycloud.schedulingalgorithm.priority;

public class PriorityRuleConfig {
    private PriorityRule priorityRule;
    private Double weight;

    public PriorityRule getPriorityRule() {
        return priorityRule;
    }

    public void setPriorityRule(PriorityRule priorityRule) {
        this.priorityRule = priorityRule;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
