package cn.harmonycloud.schedulingalgorithm.dataobject;

public class Resource {
    private Long milliCPU;
    private Long memory;
    private Long allowedPodNumber;

    public Resource() {}

    public Resource(Long milliCPU, Long memory, Long allowedPodNumber) {
        this.milliCPU = milliCPU;
        this.memory = memory;
        this.allowedPodNumber = allowedPodNumber;
    }

    public Long getMilliCPU() {
        return milliCPU;
    }

    public void setMilliCPU(Long milliCPU) {
        this.milliCPU = milliCPU;
    }

    public Long getMemory() {
        return memory;
    }

    public void setMemory(Long memory) {
        this.memory = memory;
    }

    public Long getAllowedPodNumber() {
        return allowedPodNumber;
    }

    public void setAllowedPodNumber(Long allowedPodNumber) {
        this.allowedPodNumber = allowedPodNumber;
    }
}
