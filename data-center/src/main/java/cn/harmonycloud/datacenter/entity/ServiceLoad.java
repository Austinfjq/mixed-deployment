package cn.harmonycloud.datacenter.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author wangyuzhong
 * @date 19-1-8 下午5:25
 * @Despriction
 */
public class ServiceLoad {

    private String namespace;
    private String serviceName;
    private double normalTimeResponse;
    private double normalErrorRate;
    private double timeResponse;
    private double errorRate;
    private double requestNumber;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private String startTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    public ServiceLoad() {
    }

    public ServiceLoad(String namespace, String serviceName, double timeResponse, double errorRate, double requestNumber, String startTime, String endTime) {
        this.namespace = namespace;
        this.serviceName = serviceName;
        this.timeResponse = timeResponse;
        this.errorRate = errorRate;
        this.requestNumber = requestNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getNormalTimeResponse() {
        return normalTimeResponse;
    }

    public void setNormalTimeResponse(double normalTimeResponse) {
        this.normalTimeResponse = normalTimeResponse;
    }

    public double getNormalErrorRate() {
        return normalErrorRate;
    }

    public void setNormalErrorRate(double normalErrorRate) {
        this.normalErrorRate = normalErrorRate;
    }

    public double getTimeResponse() {
        return timeResponse;
    }

    public void setTimeResponse(double timeResponse) {
        this.timeResponse = timeResponse;
    }

    public double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
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

    public double getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(double requestNumber) {
        this.requestNumber = requestNumber;
    }

    @Override
    public String toString() {
        return "ServiceLoad{" +
                "namespace='" + namespace + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", normalTimeResponse=" + normalTimeResponse +
                ", normalErrorRate=" + normalErrorRate +
                ", timeResponse=" + timeResponse +
                ", errorRate=" + errorRate +
                ", requestNumber=" + requestNumber +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
