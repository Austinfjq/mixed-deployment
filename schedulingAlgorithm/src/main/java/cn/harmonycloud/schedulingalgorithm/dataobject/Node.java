package cn.harmonycloud.schedulingalgorithm.dataobject;

public class Node {
    /**
     * unix时间
     */
    private String time;
    /**
     * 节点名
     */
    private String nodeName;
    /**
     * 节点ip
     */
    private String nodeIP;
    /**
     * 节点cpu核数
     */
    private String cpuCores;
    /**
     * 节点cpu使用率
     */
    private String cpuUsage;
    /**
     * 节点内存使用量
     */
    private String memUsage;
    /**
     * 节点内存总容量
     */
    private String memMaxCapacity;
    /**
     * 节点磁盘使用量
     */
    private String diskUsage;
    /**
     * 节点磁盘总容量
     */
    private String diskMaxCapacity;
    /**
     * 节点网络带宽
     */
    private String netBandwidth;
    /**
     * 节点pressure状态
     */
    private String condition;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeIP() {
        return nodeIP;
    }

    public void setNodeIP(String nodeIP) {
        this.nodeIP = nodeIP;
    }

    public String getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(String cpuCores) {
        this.cpuCores = cpuCores;
    }

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getMemUsage() {
        return memUsage;
    }

    public void setMemUsage(String memUsage) {
        this.memUsage = memUsage;
    }

    public String getMemMaxCapacity() {
        return memMaxCapacity;
    }

    public void setMemMaxCapacity(String memMaxCapacity) {
        this.memMaxCapacity = memMaxCapacity;
    }

    public String getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(String diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getDiskMaxCapacity() {
        return diskMaxCapacity;
    }

    public void setDiskMaxCapacity(String diskMaxCapacity) {
        this.diskMaxCapacity = diskMaxCapacity;
    }

    public String getNetBandwidth() {
        return netBandwidth;
    }

    public void setNetBandwidth(String netBandwidth) {
        this.netBandwidth = netBandwidth;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
