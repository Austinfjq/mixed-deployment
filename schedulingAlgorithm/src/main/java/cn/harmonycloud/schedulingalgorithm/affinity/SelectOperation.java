package cn.harmonycloud.schedulingalgorithm.affinity;

public enum SelectOperation {
    DoesNotExist("!"),
    Equals("="),
    DoubleEquals("=="),
    In("in"),
    NotEquals("!="),
    NotIn("notin"),
    Exists("exists"),
    GreaterThan("gt"),
    LessThan("lt");

    private final String select;
    SelectOperation(final String s) {
        this.select = s;
    }

    @Override
    public String toString() {
        return select;
    }
}
