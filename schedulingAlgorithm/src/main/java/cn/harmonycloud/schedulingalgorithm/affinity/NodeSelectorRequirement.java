package cn.harmonycloud.schedulingalgorithm.affinity;

public class NodeSelectorRequirement {
    private String key;
    private NodeSelectorOperator operator;
    private String[] values;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public NodeSelectorOperator getOperator() {
        return operator;
    }

    public void setOperator(NodeSelectorOperator operator) {
        this.operator = operator;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    /**
     * unused properpy
     */
    private Object additionalProperties;

    public Object getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Object additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
