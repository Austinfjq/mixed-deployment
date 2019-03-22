package cn.harmonycloud.entry;

import java.util.Date;

public class Result {
    public Integer operation; //1增加，2减少
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

    public Integer getOperation() {
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

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

}
