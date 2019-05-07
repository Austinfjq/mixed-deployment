package cn.harmonycloud.datacenter.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface IForecastResultCellDao {
    //获取某个服务在将来一段时间的最大请求量
    public double getNextPeriodMaxRequestNums(String forecastResultCellID,String startTime,String endTime);
}
