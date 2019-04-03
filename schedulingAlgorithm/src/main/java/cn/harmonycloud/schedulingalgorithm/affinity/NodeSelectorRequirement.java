package cn.harmonycloud.schedulingalgorithm.affinity;

public class NodeSelectorRequirement {
    private String key;
    private String operator;
    private NodeSelectorOperator operatorObject;
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

    public NodeSelectorOperator getOperatorObject() {
        // deal with: String operator
        if (operatorObject == null && operator !=null) {
            operatorObject = NodeSelectorOperator.getOperatorObject(operator);
        }
        return operatorObject;
    }

    public void setOperatorObject(NodeSelectorOperator operatorObject) {
        this.operatorObject = operatorObject;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
