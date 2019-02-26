package cn.harmonycloud.test;

public class ResultPod {
    private String namespace;
    private String serviceName;
    private String podName;

    public ResultPod(String namespace, String serviceName, String podName) {
        this.namespace = namespace;
        this.serviceName = serviceName;
        this.podName = podName;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getPodName() {
        return podName;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }
}
