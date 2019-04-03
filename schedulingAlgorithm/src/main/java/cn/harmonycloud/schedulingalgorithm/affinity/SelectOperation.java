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

    public static SelectOperation getOperatorObject(String o) {
        if (DoesNotExist.select.equals(o)) {
            return DoesNotExist;
        } else if (Equals.select.equals(o)) {
            return Equals;
        } else if (DoubleEquals.select.equals(o)) {
            return DoubleEquals;
        } else if (In.select.equals(o)) {
            return In;
        } else if (NotEquals.select.equals(o)) {
            return NotEquals;
        } else if (NotIn.select.equals(o)) {
            return NotIn;
        } else if (Exists.select.equals(o)) {
            return Exists;
        } else if (GreaterThan.select.equals(o)) {
            return GreaterThan;
        } else if (LessThan.select.equals(o)) {
            return LessThan;
        } else {
            return null;
        }
    }
}
