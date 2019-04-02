package cn.harmonycloud.schedulingalgorithm.dataobject;

import java.util.ArrayList;
import java.util.List;

public class Service implements Cloneable {
    /**
     * for pods to be scheduled
     */
    private Double cpuCosume;
    /**
     * for pods to be scheduled
     */
    private Double memCosume;
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
    private Double cpuUsage;
    /**
     * 服务内存使用量
     */
    private Double memUsage;
    /**
     * 服务存储使用量
     */
    private Double diskUsage;
    /**
     * 服务请求流量
     */
    private Double requestBytes;
    /**
     * 服务响应流量
     */
    private Double responseBytes;
    /**
     * 服务请求连接数
     */
    private Long requestConnections;
    /**
     * 服务网络错误数
     */
    private Long netErrors;
    /**
     * 服务网络响应时间
     */
    private String responseTime;

    /**
     * 不需要的数据
     */
    private String[] key;
    private String nodePort;

    public String[] getKey() {
        return key;
    }

    public void setKey(String[] key) {
        this.key = key;
    }

    public String getNodePort() {
        return nodePort;
    }

    public void setNodePort(String nodePort) {
        this.nodePort = nodePort;
    }

    public Double getCpuCosume() {
        return cpuCosume;
    }

    public void setCpuCosume(Double cpuCosume) {
        this.cpuCosume = cpuCosume;
    }

    public Double getMemCosume() {
        return memCosume;
    }

    public void setMemCosume(Double memCosume) {
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

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemUsage() {
        return memUsage;
    }

    public void setMemUsage(Double memUsage) {
        this.memUsage = memUsage;
    }

    public Double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Double getRequestBytes() {
        return requestBytes;
    }

    public void setRequestBytes(Double requestBytes) {
        this.requestBytes = requestBytes;
    }

    public Double getResponseBytes() {
        return responseBytes;
    }

    public void setResponseBytes(Double responseBytes) {
        this.responseBytes = responseBytes;
    }

    public Long getRequestConnections() {
        return requestConnections;
    }

    public void setRequestConnections(Long requestConnections) {
        this.requestConnections = requestConnections;
    }

    public Long getNetErrors() {
        return netErrors;
    }

    public void setNetErrors(Long netErrors) {
        this.netErrors = netErrors;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public Service clone() {
        try {
            Service s = (Service) super.clone();
            s.setPodList((ArrayList<String>) ((ArrayList<String>) (s.getPodList())).clone());
            return s;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
