package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.common.Pod;

import java.util.List;

public interface Scheduler {
    void schedule(List<Pod> schedulingRequests);
}
