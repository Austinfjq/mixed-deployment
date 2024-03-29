package cn.harmonycloud.dataProcessing.entry;

import cn.harmonycloud.dataProcessing.metric.Constant;

import java.util.ArrayList;
import java.util.Map;


public class PodData {

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
    private Double cpuUsage;
    private Double cpuRequest;
    private Double cpuLimit;
    private Double memUsage;
    private Double memRequest;
    private Double memLimit;
    private String volumeType;
    private Double volumeUsage;
    private Double readsBytes;
    private Double writesBytes;
    private Double receiveBytes;
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
        this.labels = labels;
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
}
