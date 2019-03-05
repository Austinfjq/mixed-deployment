package cn.harmonycloud.entry;

import java.util.Date;


public class ForecastService {

    private String namespace;
    private String serviceName;
    private String startTime;
    private String endTime;
    private Double normalErrorRate;
    private Double normalTimeResponse;
    private Double errorRate;
    private Long requestNumber;
    private Double timeResponse;

    public void ForecastService() {
    }

    public String getNamespace() {
        return namespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Double getNormalErrorRate() {
        return normalErrorRate;
    }

    public Double getNormalTimeResponse() {
        return normalTimeResponse;
    }

    public Double getErrorRate() {
        return errorRate;
    }

    public Long getRequestNumber() {
        return requestNumber;
    }

    public Double getTimeResponse() {
        return timeResponse;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setNormalErrorRate(Double normalErrorRate) {
        this.normalErrorRate = normalErrorRate;
    }

    public void setNormalTimeResponse(Double normalTimeResponse) {
        this.normalTimeResponse = normalTimeResponse;
    }

    public void setErrorRate(Double errorRate) {
        this.errorRate = errorRate;
    }

    public void setRequestNumber(Long requestNumber) {
        this.requestNumber = requestNumber;
    }

    public void setTimeResponse(Double timeResponse) {
        this.timeResponse = timeResponse;
    }
}
