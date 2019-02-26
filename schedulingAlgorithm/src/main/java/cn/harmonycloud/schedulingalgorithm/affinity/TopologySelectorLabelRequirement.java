package cn.harmonycloud.schedulingalgorithm.affinity;

public class TopologySelectorLabelRequirement {
    private String key;
    private String[] values;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
