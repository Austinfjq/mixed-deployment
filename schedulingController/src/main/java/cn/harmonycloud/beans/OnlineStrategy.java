package cn.harmonycloud.beans;

/**
 * @classname：OnlineStrategy
 * @author：WANGYUZHONG
 * @date：2019/4/10 11:53
 * @description:在线应用的调控策略
 * @version:1.0
 **/
public class OnlineStrategy {

    public Integer operation; //调控动作，表示1增加，2减少
    public String namespace;
    public String serviceName;
    public int number;
    public String clusterMasterIP;


    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getClusterMasterIP() {
        return clusterMasterIP;
    }

    public void setClusterMasterIP(String clusterMasterIP) {
        this.clusterMasterIP = clusterMasterIP;
    }

    @Override
    public String toString() {
        return "OnlineStrategy{" +
                "operation=" + operation +
                ", namespace='" + namespace + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", number='" + number + '\'' +
                ", clusterMasterIP='" + clusterMasterIP + '\'' +
                '}';
    }
}
