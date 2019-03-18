package cn.harmonycloud.bean;

import cn.harmonycloud.utils.OwnTypes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RuleSpec implements KubernetesResource {
    private final static Logger LOGGER = LoggerFactory.getLogger(RuleSpec.class);
    private String ownerType;
    private String ownerName;
    private String namespace;
    private int replicas;
    private NodeInfo[] nodes = null;

    //    public RuleSpec(){}
    public RuleSpec(String namespace,JSONObject owner,JSONArray nodeList){
        switch (owner.get("resourceKind").toString()){
            case "Deployment":ownerType = "deployment";break;
            case "Replicaset":ownerType = "replicaSet";break;
            case "Statefulset":ownerType = "statefulSet";break;
            case "Daemonset":ownerType = "deamonSet";break;
            default:
                System.out.println("ownerType["+owner.get("ownerType").toString()+"] is unknown!");
                break;
        }
        ownerName = owner.getString("resourceName");
        this.namespace = namespace;
        this.replicas = 1;

        LOGGER.info("NodeList:"+nodeList);
        int index = 0;
        this.nodes = new NodeInfo[nodeList.size()];
        for(Object node : nodeList){
            JSONObject n = JSON.parseObject(node.toString());
            NodeInfo nodeInfo = new NodeInfo();
//            nodeInfo.setIp(n.getString("ip"));
            nodeInfo.setHostname(n.getString("hostname"));
            nodeInfo.setScore(n.getInteger("score"));
            this.nodes[index++] = nodeInfo;
        }
    }

    public int getReplicas() {
        return replicas;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public NodeInfo[] getNodes() {
        return nodes;
    }

    public void setNodes(NodeInfo[] nodes) {
        this.nodes = nodes;
    }

}
