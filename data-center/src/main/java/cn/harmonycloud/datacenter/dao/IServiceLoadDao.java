package cn.harmonycloud.datacenter.dao;

import cn.harmonycloud.datacenter.entity.NodeLoad;
import cn.harmonycloud.datacenter.entity.ServiceLoad;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IServiceLoadDao {
    public List<ServiceLoad> getServiceLoadForecastValues(String startTime, String endTime);
}
