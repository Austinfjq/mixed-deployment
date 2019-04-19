package cn.harmonycloud.datacenter.entity.es;

import cn.harmonycloud.datacenter.tools.Constant;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static cn.harmonycloud.datacenter.tools.Constant.POD_INDEX;
import static cn.harmonycloud.datacenter.tools.Constant.POD_TYPE;

/**
 * @Author: changliu
 * @Date: 2018/11/27 16:27
 * @Description:
 */
@Document(indexName = POD_INDEX, type = POD_TYPE)
public class PodData {
    @Id
    private String id;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private String time;

    private String podName;
    private String podIp;
    private String nodeName;
    private String serviceName;
    private String namespace;
    private String onlineType;
    private String resourceKind;
    private String resourceName;
    private String state;
    private String startTime;
    private String imageName;
    @Field(type = FieldType.Double)
    private Double cpuUsage;
    @Field(type = FieldType.Double)
    private Double cpuRequest;
    @Field(type = FieldType.Double)
    private Double cpuLimit;
    @Field(type = FieldType.Double)
    private Double memUsage;
    @Field(type = FieldType.Double)
    private Double memRequest;
    @Field(type = FieldType.Double)
    private Double memLimit;
    private String volumeType;
    @Field(type = FieldType.Double)
    private Double volumeUsage;
    @Field(type = FieldType.Double)
    private Double readsBytes;
    @Field(type = FieldType.Double)
    private Double writesBytes;
    @Field(type = FieldType.Double)
    private Double receiveBytes;
    @Field(type = FieldType.Double)
    private Double responseBytes;
    private String locateNodeIP;
    private Map<String, String> nodeSelector;
    private Map<String, String> labels;
    private ArrayList<String> persistentVolumeClaimNames;
    private Map<String, String> containers;
    private Map<String, String> affinity;
    private String deletionStamp;
    private String ownerReferencesUid;
    private ArrayList<Map<String, String>> toleration;
    @Field(type = FieldType.Long)
    private Long netErrors;
    private String clusterMasterIP;

    public PodData() {
        this.podName = "";
        this.podIp = "";
        this.nodeName = "";
        this.serviceName = "";
        this.namespace = "";
        this.onlineType = "";
        this.resourceKind = "";
        this.resourceName = "";
        this.state = "";
        this.startTime = "";
        this.imageName = "";
        this.cpuUsage = 0.0;
        this.cpuRequest = 0.0;
        this.cpuLimit = 0.0;
        this.memUsage = 0.0;
        this.memRequest = 0.0;
        this.memLimit = 0.0;
        this.volumeType = "";
        this.volumeUsage = 0.0;
        this.readsBytes = 0.0;
        this.writesBytes = 0.0;
        this.receiveBytes = 0.0;
        this.responseBytes = 0.0;
        this.netErrors = 0l;
        this.locateNodeIP = "";
        this.clusterMasterIP = Constant.K8S_MASTER;
    }

    public PodData(String podName, String namespace) {
        this.podName = podName;
        this.namespace = namespace;
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

    public String getPodName() {
        return podName;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getLocateNodeIP() {
        return locateNodeIP;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getResourceKind() {
        return resourceKind;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getPodIp() {
        return podIp;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getState() {
        return state;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getImageName() {
        return imageName;
    }

    public String getOnlineType() {
        return onlineType;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public Double getCpuRequest() {
        return cpuRequest;
    }

    public Double getCpuLimit() {
        return cpuLimit;
    }

    public Double getMemUsage() {
        return memUsage;
    }

    public Double getMemRequest() {
        return memRequest;
    }

    public Double getMemLimit() {
        return memLimit;
    }

    public String getVolumeType() {
        return volumeType;
    }

    public Double getVolumeUsage() {
        return volumeUsage;
    }

    public Double getReadsBytes() {
        return readsBytes;
    }

    public Double getWritesBytes() {
        return writesBytes;
    }

    public Double getReceiveBytes() {
        return receiveBytes;
    }

    public Double getResponseBytes() {
        return responseBytes;
    }

    public Map<String, String> getNodeSelector() {
        return nodeSelector;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public ArrayList<String> getPersistentVolumeClaimNames() {
        return persistentVolumeClaimNames;
    }

    public Map<String, String> getAffinity() {
        return affinity;
    }

    public ArrayList<Map<String, String>> getToleration() {
        return toleration;
    }

    public String getDeletionStamp() {
        return deletionStamp;
    }

    public String getOwnerReferencesUid() {
        return ownerReferencesUid;
    }

    public Map<String, String> getContainers() {
        return containers;
    }

    public Long getNetErrors() {
        return netErrors;
    }

    public String getClusterMasterIP() {
        return clusterMasterIP;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setLocateNodeIP(String locateNodeIP) {
        this.locateNodeIP = locateNodeIP;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setResourceKind(String resourceKind) {
        this.resourceKind = resourceKind;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public void setPodIp(String podIp) {
        this.podIp = podIp;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setOnlineType(String onlineType) {
        this.onlineType = onlineType;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setCpuRequest(Double cpuRequest) {
        this.cpuRequest = cpuRequest;
    }

    public void setCpuLimit(Double cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public void setMemUsage(Double memUsage) {
        this.memUsage = memUsage;
    }

    public void setMemRequest(Double memRequest) {
        this.memRequest = memRequest;
    }

    public void setMemLimit(Double memLimit) {
        this.memLimit = memLimit;
    }

    public void setVolumeType(String volumeType) {
        this.volumeType = volumeType;
    }

    public void setVolumeUsage(Double volumeUsage) {
        this.volumeUsage = volumeUsage;
    }

    public void setReadsBytes(Double readsBytes) {
        this.readsBytes = readsBytes;
    }

    public void setWritesBytes(Double writesBytes) {
        this.writesBytes = writesBytes;
    }

    public void setReceiveBytes(Double receiveBytes) {
        this.receiveBytes = receiveBytes;
    }

    public void setResponseBytes(Double responseBytes) {
        this.responseBytes = responseBytes;
    }

    public void setNodeSelector(Map<String, String> nodeSelector) {
        this.nodeSelector = nodeSelector;
    }

    public void setLabels(Map<String, String> labels) {
        Map<String, String> map = new HashMap<>();
        for(Map.Entry entry : labels.entrySet()){
            map.put(entry.getKey().toString().replace('.','_'),entry.getValue().toString());
        }
        this.labels = map;
    }
    public void changeLabels(){
        Map<String,String> map = new HashMap<>();
        for(Map.Entry entry : labels.entrySet()){
            map.put(entry.getKey().toString().replace('_','.'),entry.getValue().toString());
        }
        this.labels = map;
    }

    public void setPersistentVolumeClaimNames(ArrayList<String> persistentVolumeClaimNames) {
        this.persistentVolumeClaimNames = persistentVolumeClaimNames;
    }

    public void setAffinity(Map<String, String> affinity) {
        this.affinity = affinity;
    }

    public void setToleration(ArrayList<Map<String, String>> toleration) {
        this.toleration = toleration;
    }

    public void setDeletionStamp(String deletionStamp) {
        this.deletionStamp = deletionStamp;
    }

    public void setOwnerReferencesUid(String ownerReferencesUid) {
        this.ownerReferencesUid = ownerReferencesUid;
    }

    public void setContainers(Map<String, String> containers) {
        this.containers = containers;
    }

    public void setNetErrors(Long netErrors) {
        this.netErrors = netErrors;
    }

    public void setClusterMasterIP(String clusterMasterIP) {
        this.clusterMasterIP = clusterMasterIP;
    }

    public ArrayList<String> getKey() {
        ArrayList<String> key = new ArrayList<>();
        key.add(this.podName);
        key.add(this.namespace);
        return key;
    }

    public void setKey(ArrayList<String> key) {
        this.setPodName(key.get(0));
        this.setNamespace(key.get(1));
    }

    @Override
    public String toString() {
        return "PodData{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", podName='" + podName + '\'' +
                ", podIp='" + podIp + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", namespace='" + namespace + '\'' +
                ", onlineType='" + onlineType + '\'' +
                ", resourceKind='" + resourceKind + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", state='" + state + '\'' +
                ", startTime='" + startTime + '\'' +
                ", imageName='" + imageName + '\'' +
                ", cpuUsage=" + cpuUsage +
                ", cpuRequest=" + cpuRequest +
                ", cpuLimit=" + cpuLimit +
                ", memUsage=" + memUsage +
                ", memRequest=" + memRequest +
                ", memLimit=" + memLimit +
                ", volumeType='" + volumeType + '\'' +
                ", volumeUsage=" + volumeUsage +
                ", readsBytes=" + readsBytes +
                ", writesBytes=" + writesBytes +
                ", receiveBytes=" + receiveBytes +
                ", responseBytes=" + responseBytes +
                ", locateNodeIP='" + locateNodeIP + '\'' +
                ", deletionStamp='" + deletionStamp + '\'' +
                ", ownerReferencesUid='" + ownerReferencesUid + '\'' +
                ", netErrors=" + netErrors +
                ", clusterMasterIP='" + clusterMasterIP + '\'' +
                '}';
    }
}
