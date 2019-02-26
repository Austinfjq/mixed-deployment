package cn.harmonycloud.datacenter.service;

import cn.harmonycloud.datacenter.entity.mysql.ForecastCell;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:23
 *@Modify By:
 */

@Service
public interface IForecastCellService {
    public List<ForecastCell> getAllForecastCells();
}
