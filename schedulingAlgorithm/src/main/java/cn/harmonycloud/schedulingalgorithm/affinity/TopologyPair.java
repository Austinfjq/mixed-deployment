package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.Objects;

public class TopologyPair {
    private String key;
    private String value;

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

    public TopologyPair(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof TopologyPair)) {
            return false;
        } else {
            TopologyPair pair = (TopologyPair) obj;
            return Objects.equals(this.getKey(), pair.getKey()) && Objects.equals(this.getValue(), pair.getValue());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
