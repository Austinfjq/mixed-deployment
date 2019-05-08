package cn.harmonycloud.beans;

/**
 * @classname：Service
 * @author：WANGYUZHONG
 * @date：2019/4/10 15:18
 * @description:TODO
 * @version:1.0
 **/
public class Service {
    private String masterIp;
    private String namespace;
    private String serviceName;
    private int serviceType;//service类型，1代表在线，2代表离线

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



    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }









    @Override
    public String toString() {
        return "Service{" +
                "masterIp='" + masterIp + '\'' +
                ", namespace='" + namespace+ '\'' +
                ", serviceName=" + serviceName +
                ", seviceType='" + serviceType+ '\'' +
                '}';
    }

}
