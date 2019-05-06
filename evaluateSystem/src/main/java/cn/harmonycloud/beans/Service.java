package cn.harmonycloud.beans;

/**
 * @classname：Service
 * @author：WANGYUZHONG
 * @date：2019/4/10 15:18
 * @description:TODO
 * @version:1.0
 **/
public class Service {
    private String clusterMasterIp;
    private String namespace;
    private String serviceName;
    private String onlineType; //service类型，online代表在线




    public String getClusterMasterIp() {
        return clusterMasterIp;
    }

    public void setClusterMasterIp(String clusterMasterIp) {
        this.clusterMasterIp = clusterMasterIp;
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
    public String getOnlineType() {
        return onlineType;
    }

    public void setOnlineType(String onlineType) {
        this.onlineType = onlineType;
    }


    @Override
    public String toString() {
        return "Service{" +
                "clusterMasterIp='" + clusterMasterIp + '\'' +
                ", namespace='" + namespace+ '\'' +
                ", serviceName=" + serviceName +
                ", onlineType='" + onlineType+ '\'' +
                '}';
    }

}
