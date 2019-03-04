package cn.harmonycloud.datacenter.service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/1/26 16:53
*@Modify By:
*/

@Service
public interface IForecastDataService {
    public List<Object> getAllForecastValue(String startTime,String endTime);
}
