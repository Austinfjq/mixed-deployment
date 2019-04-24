package cn.harmonycloud.datacenter.entity.es;

public class SearchPod {
    private String clusterIp;
    private String namespace;
    private String serviceName;
    private String hostName;
    private String startTime;
    private String endTime;

    public double getRequestNums() {
        return requestNums;
    }

    public void setRequestNums(double requestNums) {
        this.requestNums = requestNums;
    }

    private double requestNums;
    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    @Override
    public String toString() {
        return "SearchPod{" +
                "clusterIp='" + clusterIp + '\'' +
                ", namespace='" + namespace + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", hostName='" + hostName + '\'' +
                '}';
    }
}
