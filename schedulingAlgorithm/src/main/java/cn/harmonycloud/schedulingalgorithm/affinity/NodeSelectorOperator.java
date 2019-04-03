package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.Arrays;

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

    public static NodeSelectorOperator getOperatorObject(String o) {
        if (NodeSelectorOpIn.operator.equals(o)) {
            return NodeSelectorOpIn;
        } else if (NodeSelectorOpNotIn.operator.equals(o)) {
            return NodeSelectorOpNotIn;
        } else if (NodeSelectorOpExists.operator.equals(o)) {
            return NodeSelectorOpExists;
        } else if (NodeSelectorOpDoesNotExist.operator.equals(o)) {
            return NodeSelectorOpDoesNotExist;
        } else if (NodeSelectorOpGt.operator.equals(o)) {
            return NodeSelectorOpGt;
        } else if (NodeSelectorOpLt.operator.equals(o)) {
            return NodeSelectorOpLt;
        } else {
            return null;
        }
    }
}
