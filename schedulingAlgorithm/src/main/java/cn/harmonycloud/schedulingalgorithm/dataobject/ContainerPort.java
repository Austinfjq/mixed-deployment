package cn.harmonycloud.schedulingalgorithm.dataobject;

public class ContainerPort {
    /**
     * hostPort 需要用到
     */
    private Integer containerPort;
    /**
     * protocol 需要用到
     */
    private String protocol;
    /**
     * hostIP 需要用到
     */
    private String hostIP;

    public Integer getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(Integer containerPort) {
        this.containerPort = containerPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }
}
