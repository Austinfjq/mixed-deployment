package cn.harmonycloud.datacenter.entity.test;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.Objects;

import static cn.harmonycloud.datacenter.tools.Constant.RESULT_NODE_INDEX;
import static cn.harmonycloud.datacenter.tools.Constant.RESULT_NODE_TYPE;

@Document(indexName = RESULT_NODE_INDEX, type = RESULT_NODE_TYPE)
public class ResultNode {
    @Id
    private String id;

    private String namespace;
    private String serviceName;
    private ArrayList<NodeScore> nodeList;

    public ResultNode() {
    }

    public ResultNode(String namespace, String serviceName, ArrayList<NodeScore> nodeList) {
        this.namespace = namespace;
        this.serviceName = serviceName;
        this.nodeList = nodeList;
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

    public ArrayList<NodeScore> getNodeList() {
        return nodeList;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setNodeList(ArrayList<NodeScore> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultNode that = (ResultNode) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(namespace, that.namespace) &&
                Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(nodeList, that.nodeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, namespace, serviceName, nodeList);
    }
}
