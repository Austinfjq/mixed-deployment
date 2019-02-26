package cn.harmonycloud.schedulingalgorithm.dataobject;

import cn.harmonycloud.schedulingalgorithm.affinity.Taint;

import java.util.List;
import java.util.Map;

public class Node {
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
    private String cpuCores;
    /**
     * 节点cpu使用率
     */
    private String cpuUsage;
    /**
     * 节点内存使用量
     */
    private String memUsage;
    /**
     * 节点内存总容量
     */
    private String memMaxCapacity;
    /**
     * 节点磁盘使用量
     */
    private String diskUsage;
    /**
     * 节点磁盘总容量
     */
    private String diskMaxCapacity;
    /**
     * 节点网络带宽
     */
    private String netBandwidth;
    /**
     * 节点pressure状态
     */
    private String condition;
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
    private Map<String, List<ProtocolPort>> usedPorts;
    /**
     * 节点污点
     */
    private Taint[] taints;
    /**
     * 节点Condition
     */
    private NodeCondition[] conditions;
    /**
     * 节点可用pod数量
     */
    private String allocatablePods;
    /**
     * 节点可用cpu核数
     */
    private String allocatableCpuCores;
    /**
     * 节点可用内存字节数
     */
    private String allocatableMem;

    public String getAllocatableCpuCores() {
        return allocatableCpuCores;
    }

    public void setAllocatableCpuCores(String allocatableCpuCores) {
        this.allocatableCpuCores = allocatableCpuCores;
    }

    public String getAllocatableMem() {
        return allocatableMem;
    }

    public void setAllocatableMem(String allocatableMem) {
        this.allocatableMem = allocatableMem;
    }

    public String getAllocatablePods() {
        return allocatablePods;
    }

    public void setAllocatablePods(String allocatablePods) {
        this.allocatablePods = allocatablePods;
    }

    public Map<String, List<ProtocolPort>> getUsedPorts() {
        return usedPorts;
    }

    public void setUsedPorts(Map<String, List<ProtocolPort>> usedPorts) {
        this.usedPorts = usedPorts;
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

    public String getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(String cpuCores) {
        this.cpuCores = cpuCores;
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

    public String getMemMaxCapacity() {
        return memMaxCapacity;
    }

    public void setMemMaxCapacity(String memMaxCapacity) {
        this.memMaxCapacity = memMaxCapacity;
    }

    public String getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(String diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getDiskMaxCapacity() {
        return diskMaxCapacity;
    }

    public void setDiskMaxCapacity(String diskMaxCapacity) {
        this.diskMaxCapacity = diskMaxCapacity;
    }

    public String getNetBandwidth() {
        return netBandwidth;
    }

    public void setNetBandwidth(String netBandwidth) {
        this.netBandwidth = netBandwidth;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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

    public Taint[] getTaints() {
        return taints;
    }

    public void setTaints(Taint[] taints) {
        this.taints = taints;
    }

    public NodeCondition[] getConditions() {
        return conditions;
    }

    public void setConditions(NodeCondition[] conditions) {
        this.conditions = conditions;
    }
}
