package cn.harmonycloud.entry;

public class NowService {
    private String namespace;
    private String serviceName;
    private Integer podNums;
    private Double cpuUsage;
    private Double memUsage;
    private String onlineType;

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

    public String getOnlineType() {
        return onlineType;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public Double getMemUsage() {
        return memUsage;
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

    public void setOnlineType(String onlineType) {
        this.onlineType = onlineType;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setMemUsage(double memUsage) {
        this.memUsage = memUsage;
    }
}
