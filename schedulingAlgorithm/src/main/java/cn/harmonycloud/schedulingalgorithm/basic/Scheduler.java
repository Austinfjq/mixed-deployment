package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.List;

public interface Scheduler {
    void schedule(List<Pod> schedulingRequests);
}
