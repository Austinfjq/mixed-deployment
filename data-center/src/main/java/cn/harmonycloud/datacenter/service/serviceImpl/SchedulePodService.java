package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.dao.ISchedulePodDao;
import cn.harmonycloud.datacenter.entity.es.SchedulePod;
import cn.harmonycloud.datacenter.repository.SchedulePodRepository;
import cn.harmonycloud.datacenter.service.ISchedulePodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SchedulePodService implements ISchedulePodService {
    @Resource(name = "schedulePodDao")
    private ISchedulePodDao schedulePodDao;

    @Autowired
    private SchedulePodRepository schedulePodRepository;

    @Override
    public List<SchedulePod> getAllSchedulePods() {
        return schedulePodDao.findAllSchedulePod();
    }

    @Override
    public Iterable<SchedulePod> saveAllSchedulePods(List<SchedulePod> schedulePods) {
        return schedulePodRepository.saveAll(schedulePods);
    }
}
