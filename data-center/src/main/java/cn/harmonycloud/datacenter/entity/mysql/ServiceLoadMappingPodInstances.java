package cn.harmonycloud.datacenter.entity.mysql;

public class ServiceLoadMappingPodInstances {
    private String mappingId;
    private String serviceId;
    private int serviceLoad;
    private int podInstances;

    public ServiceLoadMappingPodInstances() {
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getServiceLoad() {
        return serviceLoad;
    }

    public void setServiceLoad(int serviceLoad) {
        this.serviceLoad = serviceLoad;
    }

    public int getPodInstances() {
        return podInstances;
    }

    public void setPodInstances(int podInstances) {
        this.podInstances = podInstances;
    }

    @Override
    public String toString() {
        return "ServiceLoadMappingPodInstances{" +
                "mappingId='" + mappingId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceLoad=" + serviceLoad +
                ", podInstances=" + podInstances +
                '}';
    }
}
