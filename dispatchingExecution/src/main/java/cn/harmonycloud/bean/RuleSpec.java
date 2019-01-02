package cn.harmonycloud.bean;

import cn.harmonycloud.utils.OwnTypes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.fabric8.kubernetes.api.model.KubernetesResource;

import java.util.ArrayList;
import java.util.List;

public class RuleSpec implements KubernetesResource {
    private OwnTypes ownType;
    private String ownName;
    private String namespace;
    private int replicas;
    private String timestamp;
    private List<NodeInfo> nodeList = new ArrayList<>();

//    public RuleSpec(){}

    public RuleSpec(String json){
        JSONObject data = JSON.parseObject(json);
        switch (data.get("ownType").toString()){
            case "deployment":ownType = OwnTypes.DEPLOYMENT;break;
            case "replicaset":ownType = OwnTypes.REPLICASET;break;
            case "statefulset":ownType = OwnTypes.STATEFULSET;break;
            case "daemonset":ownType = OwnTypes.DAEMONSET;break;
            default:
                System.out.println("ownType is unknown!");
                break;
        }
        ownName = data.getString("ownName");
        namespace = data.getString("namespace");
        replicas = data.getInteger("replicas");
        timestamp = data.getString("timestamp");

        JSONArray nodeArray = data.getJSONArray("nodelist");
        for(Object node : nodeArray){
            JSONObject n = JSON.parseObject(node.toString());
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setIp(n.getString("ip"));
            nodeInfo.setHostname(n.getString("hostname"));
            nodeInfo.setScore(n.getInteger("score"));
            nodeList.add(nodeInfo);
        }
    }

    public OwnTypes getOwnType() {
        return ownType;
    }

    public void setOwnType(OwnTypes ownType) {
        this.ownType = ownType;
    }

    public String getOwnName() {
        return ownName;
    }

    public void setOwnName(String ownName) {
        this.ownName = ownName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }
}
