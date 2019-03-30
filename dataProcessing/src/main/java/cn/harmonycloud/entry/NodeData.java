package cn.harmonycloud.entry;

import cn.harmonycloud.metric.Constant;

import java.util.Map;

public class NodeData {
    private String nodeName;
    private String nodeIP;
    private Double cpuCores;
    private Double cpuUsage;
    private Double allocatableCpuCores;
    private Double memUsage;
    private Double allocatableMem;
    private Double memMaxCapacity;
    private Double diskUsage;
    private Double diskMaxCapacity;
    private Double netBandwidth;
    private Double allocatablePods;
    private Double podMaxCapacity;
    private String condition;
    private Long podNums;
    private String clusterMasterIP;

    private Map<String, String> labels;
    private boolean unschedulable;
    private Map<String, String> usedPorts;
    private String taints;
    private Map<String, String> imageStates;
    private Double requestedVolumes;
    private Double allocatableVolumesCount;
    private Map<String, String> nodeConditions;
    private String preferAvoidPodsAnnotationKey;

    public NodeData() {
        this.nodeName = "";
        this.nodeIP = "";
        this.cpuCores = 0.0;
        this.cpuUsage = 0.0;
        this.allocatableCpuCores = 0.0;
        this.memUsage = 0.0;
        this.allocatableMem = 0.0;
        this.memMaxCapacity = 0.0;
        this.diskUsage = 0.0;
        this.diskMaxCapacity = 0.0;
        this.netBandwidth = 0.0;
        this.allocatablePods = 0.0;
        this.podMaxCapacity = 0.0;
        this.condition = "";
        this.taints = "";
        this.requestedVolumes = 0.0;
        this.allocatableVolumesCount = 0.0;
        this.preferAvoidPodsAnnotationKey = "";
        this.podNums = 0l;
        this.clusterMasterIP = Constant.K8S_MASTER;
    }

    public NodeData(String hostName, String ip) {
        this.nodeName = hostName;
        this.nodeIP = ip;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getNodeIP() {
        return nodeIP;
    }

    public Double getCpuCores() {
        return cpuCores;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public Double getAllocatableCpuCores() {
        return allocatableCpuCores;
    }

    public Double getMemUsage() {
        return memUsage;
    }

    public Double getAllocatableMem() {
        return allocatableMem;
    }

    public Double getMemMaxCapacity() {
        return memMaxCapacity;
    }

    public Double getDiskUsage() {
        return diskUsage;
    }

    public Double getDiskMaxCapacity() {
        return diskMaxCapacity;
    }

    public Double getNetBandwidth() {
        return netBandwidth;
    }

    public Double getAllocatablePods() {
        return allocatablePods;
    }

    public Double getPodMaxCapacity() {
        return podMaxCapacity;
    }

    public String getCondition() {
        return condition;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public boolean isUnschedulable() {
        return unschedulable;
    }

    public Map<String, String> getUsedPorts() {
        return usedPorts;
    }

    public String getTaints() {
        return taints;
    }

    public Map<String, String> getImageStates() {
        return imageStates;
    }

    public Double getRequestedVolumes() {
        return requestedVolumes;
    }

    public Double getAllocatableVolumesCount() {
        return allocatableVolumesCount;
    }

    public Map<String, String> getNodeConditions() {
        return nodeConditions;
    }

    public String getPreferAvoidPodsAnnotationKey() {
        return preferAvoidPodsAnnotationKey;
    }

    public Long getPodNums() {
        return podNums;
    }

    public String getClusterMasterIP() {
        return clusterMasterIP;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setNodeIP(String nodeIP) {
        this.nodeIP = nodeIP;
    }

    public void setCpuCores(Double cpuCores) {
        this.cpuCores = cpuCores;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setAllocatableCpuCores(Double allocatableCpuCores) {
        this.allocatableCpuCores = allocatableCpuCores;
    }

    public void setMemUsage(Double memUsage) {
        this.memUsage = memUsage;
    }

    public void setAllocatableMem(Double allocatableMem) {
        this.allocatableMem = allocatableMem;
    }

    public void setMemMaxCapacity(Double memMaxCapacity) {
        this.memMaxCapacity = memMaxCapacity;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public void setDiskMaxCapacity(Double diskMaxCapacity) {
        this.diskMaxCapacity = diskMaxCapacity;
    }

    public void setNetBandwidth(Double netBandwidth) {
        this.netBandwidth = netBandwidth;
    }

    public void setAllocatablePods(Double allocatablePods) {
        this.allocatablePods = allocatablePods;
    }

    public void setPodMaxCapacity(Double podMaxCapacity) {
        this.podMaxCapacity = podMaxCapacity;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setRequestedVolumes(Double requestedVolumes) {
        this.requestedVolumes = requestedVolumes;
    }

    public void setAllocatableVolumesCount(Double allocatableVolumesCount) {
        this.allocatableVolumesCount = allocatableVolumesCount;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public void setUnschedulable(boolean unschedulable) {
        this.unschedulable = unschedulable;
    }

    public void setUsedPorts(Map<String, String> usedPorts) {
        this.usedPorts = usedPorts;
    }

    public void setTaints(String taints) {
        this.taints = taints;
    }

    public void setImageStates(Map<String, String> imageStates) {
        this.imageStates = imageStates;
    }

    public void setPodNums(Long podNums) {
        this.podNums = podNums;
    }

    public void setNodeConditions(Map<String, String> nodeConditions) {
        this.nodeConditions = nodeConditions;
    }

    public void setPreferAvoidPodsAnnotationKey(String preferAvoidPodsAnnotationKey) {
        this.preferAvoidPodsAnnotationKey = preferAvoidPodsAnnotationKey;
    }

    public String getKey() {
        return nodeName;
    }

    public void setKey(String key) {
        this.nodeName = key;
    }
}
