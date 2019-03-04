package cn.harmonycloud.schedulingalgorithm.affinity;

public class Toleration {
    private String key;
    private TolerationOperator operator;
    private String value;
    private TaintEffect effect;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TolerationOperator getOperator() {
        return operator;
    }

    public void setOperator(TolerationOperator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TaintEffect getEffect() {
        return effect;
    }

    public void setEffect(TaintEffect effect) {
        this.effect = effect;
    }
}
