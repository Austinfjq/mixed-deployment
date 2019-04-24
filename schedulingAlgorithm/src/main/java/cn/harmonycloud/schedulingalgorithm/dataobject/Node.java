package cn.harmonycloud.schedulingalgorithm.dataobject;

import cn.harmonycloud.schedulingalgorithm.affinity.Taint;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node implements Cloneable {
    /**
     * unix时间
     */
    private String time;
    /**
     * 节点名
     */
    private String nodeName;
    /**
     * 节点ip
     */
    private String nodeIP;
    /**
     * 节点cpu核数
     */
    private Double cpuCores;
    /**
     * 节点cpu使用率，百分比
     */
    private Double cpuUsage;
    /**
     * 节点内存使用量，百分比
     */
    private Double memUsage;
    /**
     * 节点内存总容量
     */
    private Double memMaxCapacity;
    /**
     * 节点磁盘使用量，百分比
     */
    private Double diskUsage;
    /**
     * 节点磁盘总容量
     */
    private Double diskMaxCapacity;
    /**
     * 节点网络带宽
     */
    private Double netBandwidth;
    /**
     * 节点标签
     */
    private Map<String, String> labels;
    /**
     * 节点是否是不可调度的
     */
    private Boolean unschedulable;
    /**
     * 节点已使用的端口和对应的协议
     */
    private Map<String, String> usedPorts;
    /**
     * 节点污点 String
     */
    private String taints;
    /**
     * 节点污点
     */
    private Taint[] taintsArray;
    /**
     * 节点Condition
     */
    private NodeCondition nodeConditions;
    /**
     * 节点可用pod数量
     */
    private Double allocatablePods;
    /**
     * 节点可用cpu核数
     */
    private Double allocatableCpuCores;
    /**
     * 节点可用内存字节数
     */
    private Double allocatableMem;
    /**
     * 节点避免Pod的注释
     */
    private String preferAvoidPodsAnnotationKey;
    /**
     * 节点镜像状态
     */
    private Map<String, String> imageStates;
    /**
     * 在节点上需要的卷数量
     */
    private Double requestedVolumes;
    /**
     * 节点上可分配卷数量
     */
    private Double allocatableVolumesCount;

    /**
     * 属于哪个集群
     */
    private String clusterMasterIP;

    public String getClusterMasterIP() {
        return clusterMasterIP;
    }

    public void setClusterMasterIP(String clusterMasterIP) {
        this.clusterMasterIP = clusterMasterIP;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeIP() {
        return nodeIP;
    }

    public void setNodeIP(String nodeIP) {
        this.nodeIP = nodeIP;
    }

    public Double getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(Double cpuCores) {
        this.cpuCores = cpuCores;
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

    public Double getMemMaxCapacity() {
        return memMaxCapacity;
    }

    public void setMemMaxCapacity(Double memMaxCapacity) {
        this.memMaxCapacity = memMaxCapacity;
    }

    public Double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Double getDiskMaxCapacity() {
        return diskMaxCapacity;
    }

    public void setDiskMaxCapacity(Double diskMaxCapacity) {
        this.diskMaxCapacity = diskMaxCapacity;
    }

    public Double getNetBandwidth() {
        return netBandwidth;
    }

    public void setNetBandwidth(Double netBandwidth) {
        this.netBandwidth = netBandwidth;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public Boolean getUnschedulable() {
        return unschedulable;
    }

    public void setUnschedulable(Boolean unschedulable) {
        this.unschedulable = unschedulable;
    }

    public Map<String, String> getUsedPorts() {
        return usedPorts;
    }

    public void setUsedPorts(Map<String, String> usedPorts) {
        this.usedPorts = usedPorts;
    }

    public NodeCondition getNodeConditions() {
        return nodeConditions;
    }

    public void setNodeConditions(NodeCondition nodeConditions) {
        this.nodeConditions = nodeConditions;
    }

    public String getTaints() {
        return taints;
    }

    public void setTaints(String taints) {
        this.taints = taints;
    }

    public Taint[] getTaintsArray() {
        return taintsArray;
    }

    public void setTaintsArray(Taint[] taintsArray) {
        this.taintsArray = taintsArray;
    }

    public Double getAllocatablePods() {
        return allocatablePods;
    }

    public void setAllocatablePods(Double allocatablePods) {
        this.allocatablePods = allocatablePods;
    }

    public Double getAllocatableCpuCores() {
        return allocatableCpuCores;
    }

    public void setAllocatableCpuCores(Double allocatableCpuCores) {
        this.allocatableCpuCores = allocatableCpuCores;
    }

    public Double getAllocatableMem() {
        return allocatableMem;
    }

    public void setAllocatableMem(Double allocatableMem) {
        this.allocatableMem = allocatableMem;
    }

    public String getPreferAvoidPodsAnnotationKey() {
        return preferAvoidPodsAnnotationKey;
    }

    public void setPreferAvoidPodsAnnotationKey(String preferAvoidPodsAnnotationKey) {
        this.preferAvoidPodsAnnotationKey = preferAvoidPodsAnnotationKey;
    }

    public Map<String, String> getImageStates() {
        return imageStates;
    }

    public void setImageStates(Map<String, String> imageStates) {
        this.imageStates = imageStates;
    }

    public Double getRequestedVolumes() {
        return requestedVolumes;
    }

    public void setRequestedVolumes(Double requestedVolumes) {
        this.requestedVolumes = requestedVolumes;
    }

    public Double getAllocatableVolumesCount() {
        return allocatableVolumesCount;
    }

    public void setAllocatableVolumesCount(Double allocatableVolumesCount) {
        this.allocatableVolumesCount = allocatableVolumesCount;
    }

    @Override
    public Node clone() {
        try {
            Node n = (Node) super.clone();
            n.setLabels(n.getLabels() == null ? null : new HashMap<>(n.getLabels()));
            n.setUsedPorts(n.getUsedPorts() == null ? null : new HashMap<>(n.getUsedPorts()));
            n.setImageStates(n.getImageStates() == null ? null : new HashMap<>(n.getImageStates()));
            return n;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
