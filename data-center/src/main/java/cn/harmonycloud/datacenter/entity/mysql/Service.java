package cn.harmonycloud.datacenter.entity.mysql;
/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/1/25 22:46
*@Modify By:
*/
public class Service {
    private String serviceId;
    private String namespace;
    private String clusterIp;
    private String ownerType;
    private String ownerName;
    private int cpuRequest;
    private int cpuLimit;
    private int memRequest;
    private int period;
    private String responseTime;
    private int type;

    public Service() {
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getCpuRequest() {
        return cpuRequest;
    }

    public void setCpuRequest(int cpuRequest) {
        this.cpuRequest = cpuRequest;
    }

    public int getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(int cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public int getMemRequest() {
        return memRequest;
    }

    public void setMemRequest(int memRequest) {
        this.memRequest = memRequest;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "service{" +
                "serviceId='" + serviceId + '\'' +
                ", namespace='" + namespace + '\'' +
                ", clusterIp='" + clusterIp + '\'' +
                ", ownerType='" + ownerType + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", cpuRequest=" + cpuRequest +
                ", cpuLimit=" + cpuLimit +
                ", memRequest=" + memRequest +
                ", period=" + period +
                ", responseTime='" + responseTime + '\'' +
                ", type=" + type +
                '}';
    }
}
