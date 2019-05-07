package cn.harmonycloud.beans;

/**
 * @classname：Node
 * @author：WANGYUZHONG
 * @date：2019/4/10 15:21
 * @description:系统中的工作节点
 * @version:1.0
 **/
public class Node {
    private String clusterMasterIp;
    private String nodeName;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }



    public String getClusterMasterIp() {
        return clusterMasterIp;
    }

    public void setClusterMasterIp(String clusterMasterIp) {
        this.clusterMasterIp = clusterMasterIp;
    }





    @Override
    public String toString() {
        return "Node{" +
                "clusterMasterIp='" + clusterMasterIp + '\'' +
                ", nodeName='" + nodeName + '\'' +

                '}';
    }
}
