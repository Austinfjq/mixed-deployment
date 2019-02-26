package cn.harmonycloud.datacenter.dao;

import cn.harmonycloud.datacenter.entity.NodeLoad;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INodeLoadDao {
    public List<NodeLoad> getNodeLoadForecastValues(String startTime,String endTime);
}
