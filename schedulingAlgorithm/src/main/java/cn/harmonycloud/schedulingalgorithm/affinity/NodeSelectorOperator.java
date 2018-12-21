package cn.harmonycloud.schedulingalgorithm.affinity;

public enum NodeSelectorOperator {
    NodeSelectorOpIn("In"),
    NodeSelectorOpNotIn("NotIn"),
    NodeSelectorOpExists("Exists"),
    NodeSelectorOpDoesNotExist("DoesNotExist"),
    NodeSelectorOpGt("Gt"),
    NodeSelectorOpLt("Lt");

    private final String operator;
    NodeSelectorOperator(final String o) {
        this.operator = o;
    }

    @Override
    public String toString() {
        return operator;
    }
}
