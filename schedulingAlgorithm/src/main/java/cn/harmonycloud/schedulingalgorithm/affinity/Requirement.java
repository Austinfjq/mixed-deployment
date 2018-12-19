package cn.harmonycloud.schedulingalgorithm.affinity;

public class Requirement {
    private String key;
    private SelectOperation operator;
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

    public SelectOperation getOperator() {
        return operator;
    }

    public void setOperator(SelectOperation operator) {
        this.operator = operator;
    }

    public String[] getStrValues() {
        return strValues;
    }

    public void setStrValues(String[] strValues) {
        this.strValues = strValues;
    }
}
