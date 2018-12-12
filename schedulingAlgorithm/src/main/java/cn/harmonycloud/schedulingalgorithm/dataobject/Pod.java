package cn.harmonycloud.schedulingalgorithm.dataobject;

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
    private String cpuUsage;
    /**
     * Pod的cpu请求量
     */
    private String cpuRequest;
    /**
     * Pod的cpu限制量
     */
    private String cpuLimit;
    /**
     * Pod的mem使用量
     */
    private String memUsage;
    /**
     * Pod的mem请求量
     */
    private String memRequest;
    /**
     * Pod的mem限制量
     */
    private String memLimit;
    /**
     * Pod存储卷类型
     */
    private String volumeType;
    /**
     * Pod存储使用量
     */
    private String volumeUsage;
    /**
     * Pod网络请求字节数
     */
    private String requestBytes;
    /**
     * Pod网络传输字节数
     */
    private String responseBytes;

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

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getCpuRequest() {
        return cpuRequest;
    }

    public void setCpuRequest(String cpuRequest) {
        this.cpuRequest = cpuRequest;
    }

    public String getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(String cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public String getMemUsage() {
        return memUsage;
    }

    public void setMemUsage(String memUsage) {
        this.memUsage = memUsage;
    }

    public String getMemRequest() {
        return memRequest;
    }

    public void setMemRequest(String memRequest) {
        this.memRequest = memRequest;
    }

    public String getMemLimit() {
        return memLimit;
    }

    public void setMemLimit(String memLimit) {
        this.memLimit = memLimit;
    }

    public String getVolumeType() {
        return volumeType;
    }

    public void setVolumeType(String volumeType) {
        this.volumeType = volumeType;
    }

    public String getVolumeUsage() {
        return volumeUsage;
    }

    public void setVolumeUsage(String volumeUsage) {
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
}
