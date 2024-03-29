package cn.harmonycloud.schedulingalgorithm.affinity;

public class PodAffinityTerm {
    private LabelSelector labelSelector;
    private String[] namespaces;
    private String topologyKey;

    public LabelSelector getLabelSelector() {
        return labelSelector;
    }

    public void setLabelSelector(LabelSelector labelSelector) {
        this.labelSelector = labelSelector;
    }

    public String[] getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(String[] namespaces) {
        this.namespaces = namespaces;
    }

    public String getTopologyKey() {
        return topologyKey;
    }

    public void setTopologyKey(String topologyKey) {
        this.topologyKey = topologyKey;
    }
}
