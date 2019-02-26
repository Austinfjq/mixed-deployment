package cn.harmonycloud.harmonycloud.entry;

public class Result {
    public int operation; //0增加，1减少
    public String namespace;
    public String serviceName;
    public String number;

    public Result() {
    }

    public Result(int operation, String namespace, String serviceName, String number) {
        this.operation = operation;
        this.namespace = namespace;
        this.serviceName = serviceName;
        this.number = number;
    }

    public int getOperation() {
        return operation;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getNumber() {
        return number;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
