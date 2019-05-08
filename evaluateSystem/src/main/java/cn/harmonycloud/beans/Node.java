package cn.harmonycloud.beans;

/**
 * @classname：Node
 * @author：WANGYUZHONG
 * @date：2019/4/10 15:21
 * @description:系统中的工作节点
 * @version:1.0
 **/
public class Node {
    private String masterIp;
    private String hostName;
    public String getMasterIp() {
        return masterIp;
    }

    public void setMasterIp(String masterIp) {
        this.masterIp = masterIp;
    }



    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String toString() {
        return "Node{" +
                "MasterIp='" + masterIp + '\'' +
                ", hostName='" + hostName + '\'' +

                '}';
    }
}
