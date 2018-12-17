package cn.harmonycloud.schedulingalgorithm.dataobject;

public class ContainerPort {
    private String name;
    /**
     * hostPort 需要用到
     */
    private int hostPort;
    private int containerPort;
    /**
     * protocol 需要用到
     */
    private String protocol;
    /**
     * hostIP 需要用到
     */
    private String hostIP;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public int getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(int containerPort) {
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
