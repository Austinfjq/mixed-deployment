package cn.harmonycloud.schedulingalgorithm.affinity;

public class Requirement {
    private String key;
    private String operator;
    private SelectOperation operatorObject;
    // In huge majority of cases we have at most one value here.
    // It is generally faster to operate on a single-element slice
    // than on a single-element map, so we have a slice here.
    private String[] strValues;

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

    public SelectOperation getOperatorObject() {
        if (operatorObject == null && operator != null) {
            operatorObject = SelectOperation.getOperatorObject(operator);
        }
        return operatorObject;
    }

    public void setOperatorObject(SelectOperation operatorObject) {
        this.operatorObject = operatorObject;
    }

    public String[] getStrValues() {
        return strValues;
    }

    public void setStrValues(String[] strValues) {
        this.strValues = strValues;
    }
}
