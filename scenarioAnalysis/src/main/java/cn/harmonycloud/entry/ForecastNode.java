package cn.harmonycloud.entry;

import java.util.Date;

public class ForecastNode {

    private String startTime;
    private String endTime;
    private String nodeIp;
    private Double cpuUsage;
    private Double memUsage;
    private Double diskUsage;

    public void ForecastService() {

    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public Double getDiskUsage() {
        return diskUsage;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public Double getMemUsage() {
        return memUsage;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setMemUsage(Double memUsage) {
        this.memUsage = memUsage;
    }
}
