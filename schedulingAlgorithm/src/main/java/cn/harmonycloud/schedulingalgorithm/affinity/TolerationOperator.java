package cn.harmonycloud.schedulingalgorithm.affinity;

public enum TolerationOperator {
    Exists("Exists"),
    Equal("Equal");

    private final String operator;

    TolerationOperator(final String o) {
        this.operator = o;
    }

    @Override
    public String toString() {
        return operator;
    }

    public static TolerationOperator getOperatorObject(String o) {
        if (Exists.operator.equals(o)) {
            return Exists;
        } else if (Equal.operator.equals(o)) {
            return Equal;
        } else {
            return null;
        }
    }
}
