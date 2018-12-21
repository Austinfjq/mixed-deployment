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
}
