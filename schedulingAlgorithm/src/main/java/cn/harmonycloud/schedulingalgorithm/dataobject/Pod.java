package cn.harmonycloud.schedulingalgorithm.dataobject;

public class Pod {
    /**
     * operation: 0增加，1减少
     */
    private Integer operation;
    private String podId;
    private String serviceName;

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
