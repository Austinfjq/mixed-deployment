package cn.harmonycloud.bean;

public class NodeInfo {
    private String ip;
    private String hostname;
    private int score;
    public NodeInfo(){}

    public NodeInfo(String ip,String hostname,int score){
        this.ip = ip;
        this.hostname = hostname;
        this.score = score;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
