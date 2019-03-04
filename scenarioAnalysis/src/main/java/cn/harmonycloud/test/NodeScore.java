package cn.harmonycloud.test;

public class NodeScore {
    private String hostName;
    private String ip;
    private Integer score;

    public NodeScore(String hostName, String ip, Integer score) {
        this.hostName = hostName;
        this.ip = ip;
        this.score = score;
    }

    public String getHostName() {
        return hostName;
    }

    public String getIp() {
        return ip;
    }

    public Integer getScore() {
        return score;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
