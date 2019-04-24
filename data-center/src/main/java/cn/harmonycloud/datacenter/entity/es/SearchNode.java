package cn.harmonycloud.datacenter.entity.es;

public class SearchNode {
    private String clusterIp;
    private String hostName;

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String toString() {
        return "SearchNode{" +
                "clusterIp='" + clusterIp + '\'' +
                ", hostName='" + hostName + '\'' +
                '}';
    }
}
