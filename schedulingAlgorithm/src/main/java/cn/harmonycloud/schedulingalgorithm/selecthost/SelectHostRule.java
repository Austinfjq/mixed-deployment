package cn.harmonycloud.schedulingalgorithm.selecthost;

import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;

import java.util.List;

public interface SelectHostRule {
    HostPriority selectHost(List<HostPriority> hostPriorityList);
}
