package cn.harmonycloud.schedulingalgorithm.presort;

import cn.harmonycloud.schedulingalgorithm.algorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.List;

public interface PresortRule {
    List<Pod> sort(List<Pod> pods, Cache cache);
}
