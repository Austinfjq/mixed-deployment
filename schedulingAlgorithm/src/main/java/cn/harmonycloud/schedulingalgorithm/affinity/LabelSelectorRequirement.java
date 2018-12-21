package cn.harmonycloud.schedulingalgorithm.affinity;

public class LabelSelectorRequirement {
    private String key;
    private LabelSelectorOperator operator;
    private String[] values;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LabelSelectorOperator getOperator() {
        return operator;
    }

    public void setOperator(LabelSelectorOperator operator) {
        this.operator = operator;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
