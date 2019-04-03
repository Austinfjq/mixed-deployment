package cn.harmonycloud.schedulingalgorithm.affinity;

public enum LabelSelectorOperator {
    LabelSelectorOpIn("In"),
    LabelSelectorOpNotIn("NotIn"),
    LabelSelectorOpExists("Exists"),
    LabelSelectorOpDoesNotExist("DoesNotExist");

    private final String operator;
    LabelSelectorOperator(final String o) {
        this.operator = o;
    }

    @Override
    public String toString() {
        return operator;
    }

    public static LabelSelectorOperator getOperatorObject(String o) {
        if (LabelSelectorOpIn.operator.equals(o)) {
            return LabelSelectorOpIn;
        } else if (LabelSelectorOpNotIn.operator.equals(o)) {
            return LabelSelectorOpNotIn;
        } else if (LabelSelectorOpExists.operator.equals(o)) {
            return LabelSelectorOpExists;
        } else if (LabelSelectorOpDoesNotExist.operator.equals(o)) {
            return LabelSelectorOpDoesNotExist;
        } else {
            return null;
        }
    }
}
