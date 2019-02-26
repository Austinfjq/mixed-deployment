package cn.harmonycloud.schedulingalgorithm.dataobject;

public class HostPriority {
    private String host;
    private Integer weightedScore;

    public HostPriority(String host, Integer weightedScore) {
        this.host = host;
        this.weightedScore = weightedScore;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getWeightedScore() {
        return weightedScore;
    }

    public void setWeightedScore(Integer weightedScore) {
        this.weightedScore = weightedScore;
    }
}
