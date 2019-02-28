package cn.harmonycloud.schedulingalgorithm.affinity;

public class PreferredSchedulingTerm {
    private int weight;
    private NodeSelectorTerm preference;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public NodeSelectorTerm getPreference() {
        return preference;
    }

    public void setPreference(NodeSelectorTerm preference) {
        this.preference = preference;
    }

    /**
     * unused properpy
     */
    private Object additionalProperties;

    public Object getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Object additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
