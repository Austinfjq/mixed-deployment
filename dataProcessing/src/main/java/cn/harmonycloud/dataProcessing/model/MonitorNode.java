package cn.harmonycloud.dataProcessing.model;

import cn.harmonycloud.dataProcessing.metric.Constant;

public class MonitorNode {
    private String clusterMasterIP;
    private String nodeIP;
    private Double cpuUsage;
    private Double memUsage;
    private Double diskUsage;

    public MonitorNode() {
        this.nodeIP = "";
        this.cpuUsage = 0.0;
        this.memUsage = 0.0;
        this.diskUsage = 0.0;
        this.clusterMasterIP = Constant.K8S_MASTER;
    }

    public MonitorNode(String ip) {
        this.nodeIP = ip;
    }

    public String getClusterMasterIP() {
        return clusterMasterIP;
    }

    public void setClusterMasterIP(String clusterMasterIP) {
        this.clusterMasterIP = clusterMasterIP;
    }

    public String getNodeIP() {
        return nodeIP;
    }

    public void setNodeIP(String nodeIP) {
        this.nodeIP = nodeIP;
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
}
