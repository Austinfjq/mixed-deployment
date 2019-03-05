package cn.harmonycloud.entry;

public class NowNode {

    private String hostName;
    private String hostIP;
    private Integer podNums;
    private Double cpuUsage;
    private Double memUsage;

    public void NowNode() {
    }

    public String getHostName() {
        return hostName;
    }

    public String getHostIP() {
        return hostIP;
    }

    public int getPodNums() {
        return podNums;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public double getMemUsage() {
        return memUsage;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public void setPodNums(int podNums) {
        this.podNums = podNums;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setMemUsage(double memUsage) {
        this.memUsage = memUsage;
    }
}
