package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.dao.INodeLoadDao;
import cn.harmonycloud.datacenter.dao.IServiceLoadDao;
import cn.harmonycloud.datacenter.entity.mysql.ForecastCell;
import cn.harmonycloud.datacenter.entity.es.ForecastResultCell;
import cn.harmonycloud.datacenter.service.IForecastCellService;
import cn.harmonycloud.datacenter.service.IForecastDataService;
import cn.harmonycloud.datacenter.service.IForecastResultCellService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:18
 *@Modify By:
 */

@RestController
public class ForecastDataController {

    @Autowired
    private IForecastResultCellService forecastResultCellService;

    @Autowired
    private IForecastCellService forecastCellService;

    @Autowired
    private IForecastDataService forecastDataService;

    @PutMapping("/forecastResultCells")
    public Map<String, Object> saveAllForecastResultCells(@RequestBody List<ForecastResultCell> requestList){
        Map<String,Object> responseMap = new HashMap<>();
        if(requestList.size() > 0){
            for(ForecastResultCell forecastResultCell : requestList){
                forecastResultCell.setId(UUID.randomUUID().toString());
            }
            Iterable<ForecastResultCell> forecastResultCells = forecastResultCellService.saveAllForecastResultCells(requestList);
            if(Lists.newArrayList(forecastResultCells).size() == requestList.size()){
                responseMap.put("isSucceed",true);
            }else{
                responseMap.put("isSucceed",false);
            }
        }else{
            responseMap.put("isSucceed",false);
        }

        return responseMap;
    }

    @GetMapping("/forecastCellList")
    public List<ForecastCell> getAllForecastCells(){
        return forecastCellService.getAllForecastCells();
    }


    /**
     * 获得所有service和node的所有指标的预测数据
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/forecast/forecastValue")
    public List<Object> getAllForecastValue(@RequestParam("startTime") String startTime,
                                            @RequestParam("endTime") String endTime){
        return forecastDataService.getAllForecastValue(startTime,endTime);
    }
}
