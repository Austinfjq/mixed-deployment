package cn.harmonycloud.schedulingalgorithm.dataobject;

import cn.harmonycloud.schedulingalgorithm.affinity.Affinity;
import cn.harmonycloud.schedulingalgorithm.affinity.SimpleAffinity;
import cn.harmonycloud.schedulingalgorithm.affinity.Toleration;

import java.util.Date;
import java.util.Map;

public class Pod {
    /**
     * for pod to be scheduled
     * operation: 1增加，2减少
     */
    private Integer operation;
    /**
     * Pod所属服务
     */
    private String serviceName;
    /**
     * Pod所属Namespace
     */
    private String namespace;

    /**
     * unix时间
     */
    private String time;
    /**
     * Pod名
     */
    private String podName;
    /**
     * Podid
     */
    private String podID;
    /**
     * Pod所在节点名
     */
    private String nodeName;
    /**
     * Pod所属resourceKind
     */
    private String resourceKind;
    /**
     * Pod所属resourceName
     */
    private String resourceName;
    /**
     * Pod状态
     */
    private String state;
    /**
     * Pod启动时间
     */
    private String startTime;
    /**
     * Pod镜像名
     */
    private String imageName;
    /**
     * Pod的cpu使用量
     */
    private Double cpuUsage;
    /**
     * Pod的cpu请求量
     */
    private Double cpuRequest;
    /**
     * Pod的cpu限制量
     */
    private Double cpuLimit;
    /**
     * Pod的mem使用量
     */
    private Double memUsage;
    /**
     * Pod的mem请求量
     */
    private Double memRequest;
    /**
     * Pod的mem限制量
     */
    private Double memLimit;
    /**
     * Pod存储卷类型
     */
    private String volumeType;
    /**
     * Pod存储使用量
     */
    private Double volumeUsage;
    /**
     * Pod网络请求字节数
     */
    private String requestBytes;
    /**
     * Pod网络传输字节数
     */
    private String responseBytes;
    /**
     * Pod对node的选择器
     */
    private Map<String, String> nodeSelector;
    /**
     * Pod的标签
     */
    private Map<String, String> labels;
    /**
     * Pod的亲和性 String
     */
    private SimpleAffinity affinity;
    /**
     * Pod的亲和性 Affinity对象
     */
    private Affinity affinityObject;
    /**
     * Pod的删除时间戳
     */
    private String deletionStamp;
    /**
     * 容器
     */
    private Container containers;
    /**
     * 需要的端口
     */
    private ContainerPort[] wantPorts;
    /**
     * 对node的容忍条件
     */
    private Toleration[] toleration;

    /**
     * 属于哪个集群
     */
    private String clusterMasterIP;

    /**
     * 不需要的数据
     */
    private String locateNodeIP;
    private String[] key;
    private String onlineType;
    private String ownerReferencesUid;
    private String[] persistentVolumeClaimNames;
    private String podIp;
    private Double readsBytes;
    private Double receiveBytes;
    private Double writesBytes;


    public Pod() {
    }

    public Pod(int operation, String namespace, String serviceName, String clusterMasterIP) {
        this.operation = operation;
        this.namespace = namespace;
        this.serviceName = serviceName;
        this.clusterMasterIP = clusterMasterIP;
    }

    public String getClusterMasterIP() {
        return clusterMasterIP;
    }

    public void setClusterMasterIP(String clusterMasterIP) {
        this.clusterMasterIP = clusterMasterIP;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getPodID() {
        return podID;
    }

    public void setPodID(String podID) {
        this.podID = podID;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getResourceKind() {
        return resourceKind;
    }

    public void setResourceKind(String resourceKind) {
        this.resourceKind = resourceKind;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getCpuRequest() {
        return cpuRequest;
    }

    public void setCpuRequest(Double cpuRequest) {
        this.cpuRequest = cpuRequest;
    }

    public Double getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(Double cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public Double getMemUsage() {
        return memUsage;
    }

    public void setMemUsage(Double memUsage) {
        this.memUsage = memUsage;
    }

    public Double getMemRequest() {
        return memRequest;
    }

    public void setMemRequest(Double memRequest) {
        this.memRequest = memRequest;
    }

    public Double getMemLimit() {
        return memLimit;
    }

    public void setMemLimit(Double memLimit) {
        this.memLimit = memLimit;
    }

    public String getVolumeType() {
        return volumeType;
    }

    public void setVolumeType(String volumeType) {
        this.volumeType = volumeType;
    }

    public Double getVolumeUsage() {
        return volumeUsage;
    }

    public void setVolumeUsage(Double volumeUsage) {
        this.volumeUsage = volumeUsage;
    }

    public String getRequestBytes() {
        return requestBytes;
    }

    public void setRequestBytes(String requestBytes) {
        this.requestBytes = requestBytes;
    }

    public String getResponseBytes() {
        return responseBytes;
    }

    public void setResponseBytes(String responseBytes) {
        this.responseBytes = responseBytes;
    }

    public Map<String, String> getNodeSelector() {
        return nodeSelector;
    }

    public void setNodeSelector(Map<String, String> nodeSelector) {
        this.nodeSelector = nodeSelector;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public SimpleAffinity getAffinity() {
        return affinity;
    }

    public void setAffinity(SimpleAffinity affinity) {
        this.affinity = affinity;
    }

    public Affinity getAffinityObject() {
        return affinityObject;
    }

    public void setAffinityObject(Affinity affinityObject) {
        this.affinityObject = affinityObject;
    }

    public String getDeletionStamp() {
        return deletionStamp;
    }

    public void setDeletionStamp(String deletionStamp) {
        this.deletionStamp = deletionStamp;
    }

    public Container getContainers() {
        return containers;
    }

    public void setContainers(Container containers) {
        this.containers = containers;
    }

    public ContainerPort[] getWantPorts() {
        return wantPorts;
    }

    public void setWantPorts(ContainerPort[] wantPorts) {
        this.wantPorts = wantPorts;
    }

    public Toleration[] getToleration() {
        return toleration;
    }

    public void setToleration(Toleration[] toleration) {
        this.toleration = toleration;
    }

    public String getLocateNodeIP() {
        return locateNodeIP;
    }

    public void setLocateNodeIP(String locateNodeIP) {
        this.locateNodeIP = locateNodeIP;
    }

    public String[] getKey() {
        return key;
    }

    public void setKey(String[] key) {
        this.key = key;
    }

    public String getOnlineType() {
        return onlineType;
    }

    public void setOnlineType(String onlineType) {
        this.onlineType = onlineType;
    }

    public String getOwnerReferencesUid() {
        return ownerReferencesUid;
    }

    public void setOwnerReferencesUid(String ownerReferencesUid) {
        this.ownerReferencesUid = ownerReferencesUid;
    }

    public String[] getPersistentVolumeClaimNames() {
        return persistentVolumeClaimNames;
    }

    public void setPersistentVolumeClaimNames(String[] persistentVolumeClaimNames) {
        this.persistentVolumeClaimNames = persistentVolumeClaimNames;
    }

    public String getPodIp() {
        return podIp;
    }

    public void setPodIp(String podIp) {
        this.podIp = podIp;
    }

    public Double getReadsBytes() {
        return readsBytes;
    }

    public void setReadsBytes(Double readsBytes) {
        this.readsBytes = readsBytes;
    }

    public Double getReceiveBytes() {
        return receiveBytes;
    }

    public void setReceiveBytes(Double receiveBytes) {
        this.receiveBytes = receiveBytes;
    }

    public Double getWritesBytes() {
        return writesBytes;
    }

    public void setWritesBytes(Double writesBytes) {
        this.writesBytes = writesBytes;
    }
}
