package cn.harmonycloud.schedulingalgorithm.affinity;

public class LabelSelectorRequirement {
    private String key;
    private String operator;
    private LabelSelectorOperator operatorObject;
    private String[] values;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public LabelSelectorOperator getOperatorObject() {
        if (operatorObject == null && operator != null) {
            operatorObject = LabelSelectorOperator.getOperatorObject(operator);
        }
        return operatorObject;
    }

    public void setOperatorObject(LabelSelectorOperator operatorObject) {
        this.operatorObject = operatorObject;
    }
}
