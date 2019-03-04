package cn.harmonycloud.datacenter.entity.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

import static cn.harmonycloud.datacenter.tools.Constant.NODE_INDEX;
import static cn.harmonycloud.datacenter.tools.Constant.NODE_TYPE;

/**
 * @Author: changliu
 * @Date: 2018/11/27 16:47
 * @Description:
 */

@Document(indexName = NODE_INDEX, type = NODE_TYPE)
public class NodeData {
    @Id
    private String id;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private String time;

    private String nodeName;
    @Field(type = FieldType.Text,fielddata = true,analyzer = "whitespace",searchAnalyzer = "whitespace")
    private String nodeIP;
    @Field(type = FieldType.Double)
    private Double cpuCores;
    @Field(type = FieldType.Double)
    private Double cpuUsage;
    @Field(type = FieldType.Double)
    private Double allocatableCpuCores;
    @Field(type = FieldType.Double)
    private Double memUsage;
    @Field(type = FieldType.Double)
    private Double allocatableMem;
    @Field(type = FieldType.Double)
    private Double memMaxCapacity;
    @Field(type = FieldType.Double)
    private Double diskUsage;
    @Field(type = FieldType.Double)
    private Double diskMaxCapacity;
    @Field(type = FieldType.Double)
    private Double netBandwidth;
    @Field(type = FieldType.Double)
    private Double allocatablePods;
    @Field(type = FieldType.Double)
    private Double podMaxCapacity;
    private String condition;
    @Field(type = FieldType.Long)
    private Long podNums;
    private Map<String, String> labels;
    @Field(type = FieldType.Boolean)
    private boolean unschedulable;
    private Map<String, String> usedPorts;
    private String taints;
    private Map<String, String> imageStates;
    @Field(type = FieldType.Double)
    private Double requestedVolumes;
    @Field(type = FieldType.Double)
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
    }

    public NodeData(String hostName, String ip) {
        this.nodeName = hostName;
        this.nodeIP = ip;
    }

    public Long getPodNums() {
        return podNums;
    }

    public void setPodNums(Long podNums) {
        this.podNums = podNums;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
