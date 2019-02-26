package cn.harmonycloud.schedulingalgorithm.dataobject;

import java.util.Map;

public class Container {
    private Map<String, Quantity> requests;
    private Map<String, Quantity> limits;

    public Map<String, Quantity> getRequests() {
        return requests;
    }

    public void setRequests(Map<String, Quantity> requests) {
        this.requests = requests;
    }

    public Map<String, Quantity> getLimits() {
        return limits;
    }

    public void setLimits(Map<String, Quantity> limits) {
        this.limits = limits;
    }
}
