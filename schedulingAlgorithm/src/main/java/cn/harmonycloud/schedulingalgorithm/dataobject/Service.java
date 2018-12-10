package cn.harmonycloud.schedulingalgorithm.dataobject;

public class Service {
    private String serviceName;
    private String intensiveType;
    private Double podCpu;
    private Double podMemory;
    private Double podDisk;
    private Double podIo;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIntensiveType() {
        return intensiveType;
    }

    public void setIntensiveType(String intensiveType) {
        this.intensiveType = intensiveType;
    }

    public Double getPodCpu() {
        return podCpu;
    }

    public void setPodCpu(Double podCpu) {
        this.podCpu = podCpu;
    }

    public Double getPodMemory() {
        return podMemory;
    }

    public void setPodMemory(Double podMemory) {
        this.podMemory = podMemory;
    }

    public Double getPodDisk() {
        return podDisk;
    }

    public void setPodDisk(Double podDisk) {
        this.podDisk = podDisk;
    }

    public Double getPodIo() {
        return podIo;
    }

    public void setPodIo(Double podIo) {
        this.podIo = podIo;
    }
}
