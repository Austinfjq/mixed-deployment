package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.Date;

public class Taint {
    private String key;
    private String value;
    private TaintEffect effect;
    private Date timeAdded;

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

    public TaintEffect getEffect() {
        return effect;
    }

    public void setEffect(TaintEffect effect) {
        this.effect = effect;
    }

    public Date getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Date timeAdded) {
        this.timeAdded = timeAdded;
    }
}
