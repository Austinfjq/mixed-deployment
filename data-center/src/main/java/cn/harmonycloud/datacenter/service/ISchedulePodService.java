package cn.harmonycloud.datacenter.service;

import cn.harmonycloud.datacenter.entity.es.SchedulePod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISchedulePodService {
    //获取所有的schedulePod
    public Iterable<SchedulePod> getAllSchedulePods();
    //保存所有传入的schedulePod
    public Iterable<SchedulePod> saveAllSchedulePods(List<SchedulePod> schedulePods);
}
