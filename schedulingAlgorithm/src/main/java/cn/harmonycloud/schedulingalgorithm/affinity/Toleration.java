package cn.harmonycloud.schedulingalgorithm.affinity;

public class Toleration {
    private String key;
    private String operator;
    private TolerationOperator operatorObject;
    private String value;
    private String effect;
    private TaintEffect effectObject;

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

    public TolerationOperator getOperatorObject() {
        if (operatorObject == null && operator != null) {
            operatorObject = TolerationOperator.getOperatorObject(operator);
        }
        return operatorObject;
    }

    public void setOperatorObject(TolerationOperator operatorObject) {
        this.operatorObject = operatorObject;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public TaintEffect getEffectObject() {
        if (effectObject == null && effect != null) {
            effectObject = TaintEffect.getEffectObject(effect);
        }
        return effectObject;
    }

    public void setEffectObject(TaintEffect effectObject) {
        this.effectObject = effectObject;
    }
}
