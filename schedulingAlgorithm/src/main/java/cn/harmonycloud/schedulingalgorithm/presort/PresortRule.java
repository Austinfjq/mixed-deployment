package cn.harmonycloud.schedulingalgorithm.presort;

import cn.harmonycloud.schedulingalgorithm.common.Pod;

import java.util.List;

public interface PresortRule {
    List<Pod> sort(List<Pod> Pods);
}
