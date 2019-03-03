package cn.harmonycloud.datacenter.dao;

import cn.harmonycloud.datacenter.entity.es.SchedulePod;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISchedulePodDao {
    public List<SchedulePod> findAllSchedulePod();
}
