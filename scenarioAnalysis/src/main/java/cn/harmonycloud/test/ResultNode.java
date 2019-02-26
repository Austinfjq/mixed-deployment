package cn.harmonycloud.test;

import java.util.ArrayList;

public class ResultNode {
    private String namespace;
    private String serviceName;
    private ArrayList<NodeScore> nodeList;

    public ResultNode(String namespace, String serviceName, ArrayList<NodeScore> nodeList) {
        this.namespace = namespace;
        this.serviceName = serviceName;
        this.nodeList = nodeList;
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
}
