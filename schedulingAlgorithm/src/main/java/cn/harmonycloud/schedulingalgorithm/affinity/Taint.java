package cn.harmonycloud.schedulingalgorithm.affinity;

public class Taint {
    private String key;
    private String value;
    private String effect;
    private TaintEffect effectObject;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
