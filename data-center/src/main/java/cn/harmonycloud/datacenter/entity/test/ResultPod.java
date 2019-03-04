package cn.harmonycloud.datacenter.entity.test;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Objects;

import static cn.harmonycloud.datacenter.tools.Constant.RESULT_POD_INDEX;
import static cn.harmonycloud.datacenter.tools.Constant.RESULT_POD_TYPE;

@Document(indexName = RESULT_POD_INDEX,type = RESULT_POD_TYPE)
public class ResultPod {
    @Id
    private String id;
    private String namespace;
    private String serviceName;
    private String podName;

    public ResultPod() {
    }

    public ResultPod(String namespace, String serviceName, String podName) {
        this.namespace = namespace;
        this.serviceName = serviceName;
        this.podName = podName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getPodName() {
        return podName;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultPod resultPod = (ResultPod) o;
        return Objects.equals(id, resultPod.id) &&
                Objects.equals(namespace, resultPod.namespace) &&
                Objects.equals(serviceName, resultPod.serviceName) &&
                Objects.equals(podName, resultPod.podName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, namespace, serviceName, podName);
    }
}
