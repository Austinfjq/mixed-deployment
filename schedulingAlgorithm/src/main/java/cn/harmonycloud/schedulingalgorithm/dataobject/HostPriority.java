package cn.harmonycloud.schedulingalgorithm.dataobject;

public class HostPriority {
    private String host;
    private Double weightedScore;

    public HostPriority(String host, Double weightedScore) {
        this.host = host;
        this.weightedScore = weightedScore;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Double getWeightedScore() {
        return weightedScore;
    }

    public void setWeightedScore(Double weightedScore) {
        this.weightedScore = weightedScore;
    }
}
