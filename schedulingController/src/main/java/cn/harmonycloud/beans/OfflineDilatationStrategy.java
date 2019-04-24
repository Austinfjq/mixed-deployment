package cn.harmonycloud.beans;

import java.util.List;

/**
 * @classname：OfflineStrategy
 * @author：WANGYUZHONG
 * @date：2019/4/10 11:54
 * @description:离线应用扩容策略
 * @version:1.0
 **/
public class OfflineDilatationStrategy {

    private String masterIp;
    private String namespace;
    private String serviceName;
    private List<SchedulableNode> schedulableNodes;

    public String getMasterIp() {
        return masterIp;
    }

    public void setMasterIp(String masterIp) {
        this.masterIp = masterIp;
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

    public List<SchedulableNode> getSchedulableNodes() {
        return schedulableNodes;
    }

    public void setSchedulableNodes(List<SchedulableNode> schedulableNodes) {
        this.schedulableNodes = schedulableNodes;
    }
}
