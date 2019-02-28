package cn.harmonycloud.entry;

import java.util.ArrayList;
import java.util.Objects;


public class ServiceData {

    private String namespace;
    private String serviceName;
    private String clusterIP;
    private String nodePort;
    private String serviceType;
    private String onlineType;
    private Long podNums;
    private ArrayList<String> podList;
    private Double cpuUsage;
    private Double memUsage;
    private Double diskUsage;
    private Double requestBytes;
    private Double responseBytes;
    private Long requestConnections;
    private Long netErrors;
    private Double responseTime;
    private Double application;
    private String volumeType;
    private Double volumeUsage;

    private String resourceKind;
    private String resourceName;

    public ServiceData() {
        this.cpuUsage = 0.0;
        this.memUsage = 0.0;
        this.diskUsage = 0.0;
        this.requestBytes = 0.0;
        this.responseBytes = 0.0;
        this.requestConnections = 0l;
        this.volumeType = "";
        this.volumeUsage = 0.0;
    }

    public ServiceData(String serviceNamespace, String serviceName, String clusterIP) {
        this.namespace = serviceNamespace;
        this.serviceName = serviceName;
        this.clusterIP = clusterIP;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ArrayList<String> getPodList() {
        return podList;
    }

    public String getClusterIP() {
        return clusterIP;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getNodePort() {
        return nodePort;
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

    public Double getDiskUsage() {
        return diskUsage;
    }

    public Double getRequestBytes() {
        return requestBytes;
    }

    public Double getResponseBytes() {
        return responseBytes;
    }

    public Long getPodNums() {
        return podNums;
    }

    public Long getRequestConnections() {
        return requestConnections;
    }

    public Long getNetErrors() {
        return netErrors;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public Double getApplication() {
        return application;
    }

    public String getResourceKind() {
        return resourceKind;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getVolumeType() {
        return volumeType;
    }

    public Double getVolumeUsage() {
        return volumeUsage;
    }

    public void setServiceNamespace(String serviceNamespace) {
        this.namespace = serviceNamespace;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setPodList(ArrayList<String> podList) {
        this.podList = podList;
    }

    public void setClusterIP(String clusterIP) {
        this.clusterIP = clusterIP;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setNodePort(String nodePort) {
        this.nodePort = nodePort;
    }

    public void setOnlineType(String onlineType) {
        this.onlineType = onlineType;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setMemUsage(Double memUsage) {
        this.memUsage = memUsage;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public void setRequestBytes(Double requestBytes) {
        this.requestBytes = requestBytes;
    }

    public void setResponseBytes(Double responseBytes) {
        this.responseBytes = responseBytes;
    }

    public void setPodNums(Long podNums) {
        this.podNums = podNums;
    }

    public void setRequestConnections(Long requestConnections) {
        this.requestConnections = requestConnections;
    }

    public void setNetErrors(Long netErrors) {
        this.netErrors = netErrors;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }

    public void setApplication(Double application) {
        this.application = application;
    }

    public void setResourceKind(String resourceKind) {
        this.resourceKind = resourceKind;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setVolumeType(String volumeType) {
        this.volumeType = volumeType;
    }

    public void setVolumeUsage(Double volumeUsage) {
        this.volumeUsage = volumeUsage;
    }

    public ArrayList<String> getKey() {
        ArrayList<String> key = new ArrayList<>();
        key.add(this.serviceName);
        key.add(this.namespace);
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceData)) return false;
        ServiceData service = (ServiceData) o;
        return Objects.equals(getNamespace(), service.getNamespace()) &&
                Objects.equals(getServiceName(), service.getServiceName()) &&
                Objects.equals(getClusterIP(), service.getClusterIP());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNamespace(), getServiceName());
    }

}
