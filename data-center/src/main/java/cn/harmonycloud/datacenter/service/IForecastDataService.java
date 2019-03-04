package cn.harmonycloud.datacenter.service;

import cn.harmonycloud.datacenter.entity.NodeLoad;
import cn.harmonycloud.datacenter.entity.ServiceLoad;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ServiceLoader;

/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/1/26 16:53
*@Modify By:
*/

@Service
public interface IForecastDataService {
    public List<Object> getAllForecastValue(String startTime,String endTime);
    public List<ServiceLoad> getAllServiceLoads(String startTime,String endTime);
    public List<NodeLoad> getAllNodeLoads(String startTime,String endTime);
}
