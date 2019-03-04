package cn.harmonycloud.schedulingalgorithm.affinity;

public class SimpleAffinity {
    private String nodeAffinity;
    private String podAffinity;
    private String podAntiAffinity;

    public String getNodeAffinity() {
        return nodeAffinity;
    }

    public void setNodeAffinity(String nodeAffinity) {
        this.nodeAffinity = nodeAffinity;
    }

    public String getPodAffinity() {
        return podAffinity;
    }

    public void setPodAffinity(String podAffinity) {
        this.podAffinity = podAffinity;
    }

    public String getPodAntiAffinity() {
        return podAntiAffinity;
    }

    public void setPodAntiAffinity(String podAntiAffinity) {
        this.podAntiAffinity = podAntiAffinity;
    }
}
