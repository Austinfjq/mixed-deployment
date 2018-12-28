package cn.harmonycloud.schedulingalgorithm.dataobject;

public class Resource {
    private Integer milliCPU;
    private Integer memory;
    private Integer allowedPodNumber;

    public Resource() {}

    public Resource(Integer milliCPU, Integer memory, Integer allowedPodNumber) {
        this.milliCPU = milliCPU;
        this.memory = memory;
        this.allowedPodNumber = allowedPodNumber;
    }

    public Integer getMilliCPU() {
        return milliCPU;
    }

    public void setMilliCPU(Integer milliCPU) {
        this.milliCPU = milliCPU;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getAllowedPodNumber() {
        return allowedPodNumber;
    }

    public void setAllowedPodNumber(Integer allowedPodNumber) {
        this.allowedPodNumber = allowedPodNumber;
    }
}
