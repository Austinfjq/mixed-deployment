package cn.harmonycloud.entry;

public class NowNode {

    private String nodeName;
    private String nodeIP;
    private Integer podNums;
    private Double cpuUsage;
    private Double memUsage;

    public void NowNode() {
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getNodeIP() {
        return nodeIP;
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

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setNodeIP(String nodeIP) {
        this.nodeIP = nodeIP;
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
