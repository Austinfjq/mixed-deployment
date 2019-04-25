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

    @Override
    public ForecastCell getOneForecastCellByIndex(String cellId, int type, String forecastingIndex) {
        return forecastCellMapper.getOneForecastCellByIndex(cellId,type,forecastingIndex);
    }

    @Override
    public int saveForecastingModel(String cellId, int type, String forecastingIndex, String forecastingModel, String modelParams) {
        ForecastCell forecastCell = getOneForecastCellByIndex(cellId,type,forecastingIndex);
        if(forecastCell == null){
            return forecastCellMapper.insertForecastingModel(cellId,type,forecastingIndex,forecastingModel,modelParams);
        }else{
            return forecastCellMapper.updateForecastingModel(cellId,type,forecastingIndex,forecastingModel,modelParams);
        }
    }

    @Override
    public int saveForecastingEndTime(String cellId, int type, String forecastingIndex, String forecastingEndTime) {
        ForecastCell forecastCell = getOneForecastCellByIndex(cellId,type,forecastingIndex);
        if(forecastCell == null){
            return forecastCellMapper.insertForecastingEndTime(cellId,type,forecastingIndex,forecastingEndTime);
        }else{
            return forecastCellMapper.updateForecastingEndTime(cellId,type,forecastingIndex,forecastingEndTime);
        }
    }

}
