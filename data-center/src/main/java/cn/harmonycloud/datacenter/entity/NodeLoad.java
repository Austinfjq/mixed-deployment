package cn.harmonycloud.datacenter.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author wangyuzhong
 * @date 19-1-8 下午5:30
 * @Despriction
 */
public class NodeLoad {

    private String nodeIP;
    private double cpuUsage;
    private double memUsage;
    private double diskUsage;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private String startTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    public NodeLoad() {
    }

    public NodeLoad(String nodeIP, double cpuUsage, double memUsage, double diskUsage, String startTime, String endTime) {
        this.nodeIP = nodeIP;
        this.cpuUsage = cpuUsage;
        this.memUsage = memUsage;
        this.diskUsage = diskUsage;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getNodeIP() {
        return nodeIP;
    }

    public void setNodeIP(String nodeIP) {
        this.nodeIP = nodeIP;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemUsage() {
        return memUsage;
    }

    public void setMemUsage(double memUsage) {
        this.memUsage = memUsage;
    }

    public double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "NodeLoad{" +
                "NodeIp='" + nodeIP + '\'' +
                ", CPUUsage=" + cpuUsage +
                ", MemUsage=" + memUsage +
                ", diskUsage=" + diskUsage +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
