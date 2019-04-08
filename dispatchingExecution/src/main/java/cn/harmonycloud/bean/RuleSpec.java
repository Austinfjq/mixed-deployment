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
    public RuleSpec(String namespace,String ownerType,String ownerName,JSONArray nodeList,int replicas){
        this.ownerType = ownerType;
        this.ownerName = ownerName;
        this.namespace = namespace;
        this.replicas = replicas;

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
