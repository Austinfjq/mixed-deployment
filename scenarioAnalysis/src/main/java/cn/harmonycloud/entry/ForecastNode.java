package cn.harmonycloud.entry;

import java.util.Date;

public class ForecastNode {

    private String hostName;
    private String hostIp;
    private Integer podNums;
    private String period;
    private Double cpuUsage;
    private Double memUsage;

    public void ForecastService() {

    }

    public String getHostName() {
        return hostName;
    }

    public String getHostIp() {
        return hostIp;
    }

    public Integer getPodNums() {
        return podNums;
    }

    public String getPeriod() {
        return period;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public Double getMemUsage() {
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

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setMemUsage(Double memUsage) {
        this.memUsage = memUsage;
    }
}
