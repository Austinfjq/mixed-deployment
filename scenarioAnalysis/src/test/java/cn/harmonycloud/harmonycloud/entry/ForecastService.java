package cn.harmonycloud.harmonycloud.entry;

/**
 * @Author: changliu
 * @Date: 2018/12/30 16:10
 * @Description:
 */
public class ForecastService {

    private String namespace;
    private String serviceName;
    private Integer podNums;
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


}
