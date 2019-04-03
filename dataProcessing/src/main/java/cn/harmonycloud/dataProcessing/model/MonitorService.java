package cn.harmonycloud.dataProcessing.model;

import cn.harmonycloud.dataProcessing.metric.Constant;

import java.util.ArrayList;

public class MonitorService {

    private String namespace;
    private String serviceName;
    private Long netErrors;
    private Double responseTime;
    private String clusterMasterIP;

    public MonitorService() {
        this.clusterMasterIP = Constant.K8S_MASTER;
    }

    public MonitorService(String serviceNamespace, String serviceName) {
        this.namespace = serviceNamespace;
        this.serviceName = serviceName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getNetErrors() {
        return netErrors;
    }

    public void setNetErrors(Long netErrors) {
        this.netErrors = netErrors;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }

    public String getClusterMasterIP() {
        return clusterMasterIP;
    }

    public void setClusterMasterIP(String clusterMasterIP) {
        this.clusterMasterIP = clusterMasterIP;
    }

    public ArrayList<String> getKey() {
        ArrayList<String> key = new ArrayList<>();
        key.add(this.serviceName);
        key.add(this.namespace);
        return key;
    }

}
