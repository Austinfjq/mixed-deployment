package cn.harmonycloud.schedulingalgorithm.selecthost;

import cn.harmonycloud.schedulingalgorithm.common.HostPriority;

import java.util.List;

public interface SelectHostRule {
    List<HostPriority> selectHost(List<HostPriority> hostPriorityList);
}
