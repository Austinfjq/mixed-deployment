package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.entity.ServiceLoad;
import cn.harmonycloud.datacenter.entity.mysql.ForecastCell;
import cn.harmonycloud.datacenter.entity.es.ForecastResultCell;
import cn.harmonycloud.datacenter.service.IForecastCellService;
import cn.harmonycloud.datacenter.service.IForecastDataService;
import cn.harmonycloud.datacenter.service.IForecastResultCellService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
     * 根据ID,type,indexName获取对应的ForecastCell
     *
     * @param cellId
     * @param type
     * @param forecastingIndex
     * @return
     */
    @GetMapping("/forecast/forecastCell")
    public ForecastCell getOneForecastCellByIndex(@RequestParam("ID") String cellId,
                                                  @RequestParam("type") int type,
                                                  @RequestParam("indexName") String forecastingIndex){
        return forecastCellService.getOneForecastCellByIndex(cellId,type,forecastingIndex);
    }

    /**
     * 获得所有service和node的所有指标的预测数据
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/forecast/forecastValues")
    public List getAllForecastValue(@RequestParam("id") String id,
                                    @RequestParam("startTime") String startTime,
                                    @RequestParam("endTime") String endTime){
        if(id.equals("service")){
            return forecastDataService.getAllServiceLoads(startTime,endTime);
        }else if(id.equals("node")){
            return forecastDataService.getAllNodeLoads(startTime,endTime);
        }else{
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 保存ForecastCell的预测模型
     * 将生成的预测模型名称和参数持久化到数据库中
     *
     * @param cellId
     * @param type
     * @param forecastingIndex
     * @param forecastingModel
     * @param modelParams
     * @return
     */
    @PutMapping("/forecast/forecastCellModel")
    public Map<String,Object> saveForecastingModel(@RequestParam("ID") String cellId,
                                                   @RequestParam("type") int type,
                                                   @RequestParam("indexName") String forecastingIndex,
                                                   @RequestParam("forecastingModel") String forecastingModel,
                                                   @RequestParam("modelParams") String modelParams){
        Map<String,Object> responseMap = new HashMap<>();
        int result = forecastCellService.saveForecastingModel(cellId,type,forecastingIndex,forecastingModel,modelParams);
        if(result>0){
            responseMap.put("isSucceed",true);
        }else{
            responseMap.put("isSucceed",false);
        }
        return responseMap;
    }

    /**
     * 保存ForecastCell完成预测的未来最远时间点
     * @param cellId
     * @param type
     * @param forecastingIndex
     * @param forecastingEndTime
     * @return
     */
    @PutMapping("/forecast/forecastCellEndTime")
    public Map<String,Object> saveForecastingEndTime(@RequestParam("ID") String cellId,
                                                   @RequestParam("type") int type,
                                                   @RequestParam("indexName") String forecastingIndex,
                                                   @RequestParam("lastForecastTime") String forecastingEndTime){
        Map<String,Object> responseMap = new HashMap<>();
        int result = forecastCellService.saveForecastingEndTime(cellId,type,forecastingIndex,forecastingEndTime);
        if(result>0){
            responseMap.put("isSucceed",true);
        }else{
            responseMap.put("isSucceed",false);
        }
        return responseMap;
    }
}
