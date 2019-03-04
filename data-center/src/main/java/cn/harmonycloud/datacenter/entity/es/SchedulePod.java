package cn.harmonycloud.datacenter.entity.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import static cn.harmonycloud.datacenter.tools.Constant.SCHEDULE_POD_INDEX;
import static cn.harmonycloud.datacenter.tools.Constant.SCHEDULE_POD_TYPE;

@Document(indexName = SCHEDULE_POD_INDEX,type = SCHEDULE_POD_TYPE)
public class SchedulePod {
    @Id
    private String id;
    @Field(type = FieldType.Integer)
    private int operation;//1增加，2减少
    private String namespace;
    private String serviceName;
    @Field(type = FieldType.Integer)
    private int number;

    public SchedulePod() {
    }

    public SchedulePod(String id, int operation, String namespace, String serviceName, int number) {
        this.id = id;
        this.operation = operation;
        this.namespace = namespace;
        this.serviceName = serviceName;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
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

    @Override
    public String toString() {
        return "SchedulePod{" +
                "id='" + id + '\'' +
                ", operation=" + operation +
                ", namespace='" + namespace + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", number=" + number +
                '}';
    }
}
