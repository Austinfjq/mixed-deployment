package cn.harmonycloud.entry;

import java.util.Date;

/**
 * @Author: changliu
 * @Date: 2018/12/30 16:10
 * @Description:
 */
public class ForecastService {

    private String namespace;
    private String serviceName;
    private Integer podNums;
    private Double requestLoad;
    private String period;

    public void ForecastService() {
    }

    public String getNamespace() {
        return namespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Integer getPodNums() {
        return podNums;
    }

    public String getPeriod() {
        return period;
    }

    public Double getRequestLoad() {
        return requestLoad;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setPodNums(int podNums) {
        this.podNums = podNums;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setRequestLoad(Double requestLoad) {
        this.requestLoad = requestLoad;
    }
}
