package cn.harmonycloud.beans;

/**
 * @classname：OfflineShrinkageStrategy
 * @author：WANGYUZHONG
 * @date：2019/4/10 14:39
 * @description:离线应用缩容策略
 * @version:1.0
 **/
public class OfflineShrinkageStrategy {

    private String masterIP;
    private String namespace;
    private String serviceName;
    private String podName;

    public String getMasterIP() {
        return masterIP;
    }

    public void setMasterIP(String masterIP) {
        this.masterIP = masterIP;
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

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }
}
