package cn.harmonycloud.entry;

public class NowService {
    private String namespace;
    private String serviceName;
    private Integer podNums;
    private Double cpuUsage;
    private Double memUsage;
    private Boolean isOffline;

    public void NowService(){}

    public String getNamespace() {
        return namespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Integer getPodNums() {
        return podNums;
    }


    public Double getCpuUsage() {
        return cpuUsage;
    }

    public Double getMemUsage() {
        return memUsage;
    }

    public Boolean isOffline() {
        return isOffline;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public void setOffline(Boolean offline) {
        isOffline = offline;
    }
}
