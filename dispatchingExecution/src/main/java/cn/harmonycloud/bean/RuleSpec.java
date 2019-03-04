package cn.harmonycloud.bean;

import cn.harmonycloud.utils.OwnTypes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.fabric8.kubernetes.api.model.KubernetesResource;

import java.util.ArrayList;
import java.util.List;

public class RuleSpec implements KubernetesResource {
    private OwnTypes ownerType;
    private String ownerName;
    private String namespace;
    private int replicas;
    private List<NodeInfo> nodeList = new ArrayList<>();
//    public RuleSpec(){}
    public RuleSpec(String namespace,JSONObject owner,JSONArray nodeList){
        switch (owner.get("ownerType").toString()){
            case "deployment":ownerType = OwnTypes.DEPLOYMENT;break;
            case "replicaset":ownerType = OwnTypes.REPLICASET;break;
            case "statefulset":ownerType = OwnTypes.STATEFULSET;break;
            case "daemonset":ownerType = OwnTypes.DAEMONSET;break;
            default:
                System.out.println("ownerType["+owner.get("ownerType").toString()+"] is unknown!");
                break;
        }
        ownerName = owner.getString("ownerName");
        this.namespace = namespace;
        this.replicas = 1;

        for(Object node : nodeList){
            JSONObject n = JSON.parseObject(node.toString());
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setIp(n.getString("ip"));
            nodeInfo.setHostname(n.getString("hostname"));
            nodeInfo.setScore(n.getInteger("score"));
            this.nodeList.add(nodeInfo);
        }
    }

    public int getReplicas() {
        return replicas;
    }

    public OwnTypes getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnTypes ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<NodeInfo> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeInfo> nodeList) {
        this.nodeList = nodeList;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

}
