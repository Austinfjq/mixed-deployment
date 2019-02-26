package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.entity.es.SchedulePod;
import cn.harmonycloud.datacenter.repository.SchedulePodRepository;
import cn.harmonycloud.datacenter.service.ISchedulePodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulePodService implements ISchedulePodService {
    @Autowired
    private SchedulePodRepository schedulePodRepository;

    @Override
    public Iterable<SchedulePod> getAllSchedulePods() {
        return schedulePodRepository.findAll();
    }

    @Override
    public Iterable<SchedulePod> saveAllSchedulePods(List<SchedulePod> schedulePods) {
        return schedulePodRepository.saveAll(schedulePods);
    }
}
