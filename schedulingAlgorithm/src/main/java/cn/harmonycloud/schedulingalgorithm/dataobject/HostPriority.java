package cn.harmonycloud.schedulingalgorithm.dataobject;

public class HostPriority {
    private String hostname;
    private Integer score;

    public HostPriority(String hostname, Integer score) {
        this.hostname = hostname;
        this.score = score;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
