package cn.harmonycloud.beans;

/**
 * @author wangyuzhong
 * @date 19-1-8 下午5:30
 * @Despriction
 */
public class NodeLoad {

    private String NodeIp;
    private double CPUUsage;
    private double MemUsage;
    private double diskUsage;
    private String startTime;
    private String endTime;

    public NodeLoad() {
    }

    public NodeLoad(String nodeIp, double CPUUsage, double memUsage, double diskUsage, String startTime, String endTime) {
        NodeIp = nodeIp;
        this.CPUUsage = CPUUsage;
        MemUsage = memUsage;
        this.diskUsage = diskUsage;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getNodeIp() {
        return NodeIp;
    }

    public void setNodeIp(String nodeIp) {
        NodeIp = nodeIp;
    }

    public double getCPUUsage() {
        return CPUUsage;
    }

    public void setCPUUsage(double CPUUsage) {
        this.CPUUsage = CPUUsage;
    }

    public double getMemUsage() {
        return MemUsage;
    }

    public void setMemUsage(double memUsage) {
        MemUsage = memUsage;
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
                "NodeIp='" + NodeIp + '\'' +
                ", CPUUsage=" + CPUUsage +
                ", MemUsage=" + MemUsage +
                ", diskUsage=" + diskUsage +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
