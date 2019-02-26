package cn.harmonycloud.entry;

public class NowNode {

    private String hostName;
    private String hostIp;
    private Integer podNums;
    private String period;
    private Double cpuUsage;
    private Double memUsage;

    public void NowNode() {
    }

    public String getHostName() {
        return hostName;
    }

    public String getHostIp() {
        return hostIp;
    }

    public int getPodNums() {
        return podNums;
    }

    public String getPeriod() {
        return period;
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

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public void setPodNums(int podNums) {
        this.podNums = podNums;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setMemUsage(double memUsage) {
        this.memUsage = memUsage;
    }
}
