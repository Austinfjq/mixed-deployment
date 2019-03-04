package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.entity.mysql.ForecastCell;
import cn.harmonycloud.datacenter.mapper.ForecastCellMapper;
import cn.harmonycloud.datacenter.service.IForecastCellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:22
 *@Modify By:
 */

@Service
public class ForecastCellService implements IForecastCellService {
    @Autowired
    private ForecastCellMapper forecastCellMapper;
    @Override
    public List<ForecastCell> getAllForecastCells() {
        return forecastCellMapper.getAllForecastCells();
    }
}
