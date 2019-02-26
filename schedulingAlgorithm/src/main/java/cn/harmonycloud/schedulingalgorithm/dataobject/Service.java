package cn.harmonycloud.schedulingalgorithm.dataobject;

import java.util.List;

public class Service {
    /**
     * for pods to be scheduled
     */
    private String cpuCosume;
    /**
     * for pods to be scheduled
     */
    private String memCosume;
    /**
     * for pods to be scheduled
     */
    private String DownNetIOCosume;
    /**
     * for pods to be scheduled
     */
    private String UPNetIOCosume;

    /**
     * for pods to be scheduled
     * 资源密集类型
     * 1.CPU密集型 2.mem密集型 3.磁盘密集型 4.网络密集型
     */
    private Integer intensiveType;

    /**
     * unix时间
     */
    private String time;
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 服务所属namespace
     */
    private String namespace;
    /**
     * 服务ClusterIP
     */
    private String clusterIP;
    /**
     * 服务离在线类型，离线1
     */
    private String serviceType;
    /**
     * 服务下pod列表
     */
    private List<String> podList;
    /**
     * 服务cpu使用量
     */
    private String cpuUsage;
    /**
     * 服务内存使用量
     */
    private String memUsage;
    /**
     * 服务存储使用量
     */
    private String diskUsage;
    /**
     * 服务请求流量
     */
    private String requestBytes;
    /**
     * 服务响应流量
     */
    private String responseBytes;
    /**
     * 服务请求连接数
     */
    private String requestConnections;
    /**
     * 服务网络错误数
     */
    private String netErrors;
    /**
     * 服务网络响应时间
     */
    private String responseTime;

    public String getCpuCosume() {
        return cpuCosume;
    }

    public void setCpuCosume(String cpuCosume) {
        this.cpuCosume = cpuCosume;
    }

    public String getMemCosume() {
        return memCosume;
    }

    public void setMemCosume(String memCosume) {
        this.memCosume = memCosume;
    }

    public String getDownNetIOCosume() {
        return DownNetIOCosume;
    }

    public void setDownNetIOCosume(String downNetIOCosume) {
        DownNetIOCosume = downNetIOCosume;
    }

    public String getUPNetIOCosume() {
        return UPNetIOCosume;
    }

    public void setUPNetIOCosume(String UPNetIOCosume) {
        this.UPNetIOCosume = UPNetIOCosume;
    }

    public Integer getIntensiveType() {
        return intensiveType;
    }

    public void setIntensiveType(Integer intensiveType) {
        this.intensiveType = intensiveType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getClusterIP() {
        return clusterIP;
    }

    public void setClusterIP(String clusterIP) {
        this.clusterIP = clusterIP;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<String> getPodList() {
        return podList;
    }

    public void setPodList(List<String> podList) {
        this.podList = podList;
    }

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getMemUsage() {
        return memUsage;
    }

    public void setMemUsage(String memUsage) {
        this.memUsage = memUsage;
    }

    public String getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(String diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getRequestBytes() {
        return requestBytes;
    }

    public void setRequestBytes(String requestBytes) {
        this.requestBytes = requestBytes;
    }

    public String getResponseBytes() {
        return responseBytes;
    }

    public void setResponseBytes(String responseBytes) {
        this.responseBytes = responseBytes;
    }

    public String getRequestConnections() {
        return requestConnections;
    }

    public void setRequestConnections(String requestConnections) {
        this.requestConnections = requestConnections;
    }

    public String getNetErrors() {
        return netErrors;
    }

    public void setNetErrors(String netErrors) {
        this.netErrors = netErrors;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }
}
