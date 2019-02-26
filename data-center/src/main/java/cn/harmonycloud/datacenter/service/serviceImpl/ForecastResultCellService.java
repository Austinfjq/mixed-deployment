package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.entity.es.ForecastResultCell;
import cn.harmonycloud.datacenter.repository.ForecastResultCellRepository;
import cn.harmonycloud.datacenter.service.IForecastResultCellService;
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
public class ForecastResultCellService implements IForecastResultCellService {
    @Autowired
    private ForecastResultCellRepository forecastResultCellRepository;

    @Override
    public Iterable<ForecastResultCell> saveAllForecastResultCells(List<ForecastResultCell> forecastResultCells) {
        return forecastResultCellRepository.saveAll(forecastResultCells);
    }
}
