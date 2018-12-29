package cn.harmonycloud.bean;

import cn.harmonycloud.utils.OwnTypes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private OwnTypes ownType;
    private String ownName;
    private String namespace;
    private int replicas;
    private List<NodeInfo> nodeList = new ArrayList<>();

//    private Map<String,Integer> nodeList = new HashMap<>();//ip->score

    public Rule(){}

    public Rule(String json){
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

    public static void main(String[] args){
        JSONObject object = new JSONObject();
        object.put("ownType","deployment");
        object.put("ownName","nginx-app");
        object.put("namespace","wy");
        object.put("replicas",1);

        List<NodeInfo> nodeList = new ArrayList<>();
        NodeInfo node1 = new NodeInfo();
        node1.setIp("10.10.102.25");
        node1.setHostname("10.10.103.25-master");
        node1.setScore(10);

        NodeInfo node2 = new NodeInfo();
        node2.setIp("10.10.102.28");
        node2.setHostname("10.10.103.28-build");
        node2.setScore(9);

        nodeList.add(node1);
        nodeList.add(node2);
        object.put("nodelist",nodeList);

        Rule rule = new Rule(object.toString());
        System.out.println("Successfully!");
    }
}

