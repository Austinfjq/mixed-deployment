package cn.harmonycloud.datacenter.service;

import cn.harmonycloud.datacenter.entity.es.ForecastResultCell;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:23
 *@Modify By:
 */

@Service
public interface IForecastResultCellService {
    public Iterable<ForecastResultCell> saveAllForecastResultCells(List<ForecastResultCell> forecastResultCells);
}
