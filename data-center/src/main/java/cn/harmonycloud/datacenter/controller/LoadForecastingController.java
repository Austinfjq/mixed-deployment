package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.ForecastResultCell;
import cn.harmonycloud.datacenter.entity.mysql.ForecastCell;
import cn.harmonycloud.datacenter.service.*;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
*@Author: shaodilong
*@Description: 负载预测模块接口
*@Date: Created in 2019/5/6 19:07
*@Modify By:
*/
@RestController
public class LoadForecastingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadForecastingController.class);
    @Autowired
    private IForecastCellService forecastCellService;
    @Autowired
    private IServiceDataService serviceDataService;
    @Autowired
    private INodeDataService nodeDataService;
    @Autowired
    private IForecastResultCellService forecastResultCellService;

    /**
     * 返回所有的ForecastCell
     *
     * @return
     */
    @GetMapping("/forecastCellList")
    public List<ForecastCell> getAllForecastCells(){
        return forecastCellService.getAllForecastCells();
    }

    /**
     * 取某个指标的历史数据值
     *
     * @param requestMap
     * @return
     */
    @PostMapping("/indexData")
    public List<DataPoint> getIndexDatas(@RequestBody Map<String, Object> requestMap){

        int type = Integer.parseInt(requestMap.get("type").toString());
        String id = (String) requestMap.get("id");
        String indexName = (String) requestMap.get("index");//需要获取的指标名
        String startTime = (String) requestMap.get("startTime");
        String endTime = (String) requestMap.get("endTime");

        if(type == 0){//id = clusterMasterIP&namespace&serviceName
            return serviceDataService.getIndexDatas(id,indexName,startTime,endTime);
        }else if(type == 1){//id = clusterMasterIP&nodeName
            return nodeDataService.getIndexDatas(id,indexName,startTime,endTime);
        }else{
            return new ArrayList<>();
        }
    }

    /**
     * 保存ForecastResultCell对象数组
     *
     * @param requestList
     * @return
     */
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

    /**
     * 获得某个在线服务的某个指标在某段时间段的均值
     *
     * @param requestMap
     * @return
     */
    @PostMapping("/service/getServiceIndex")
    public Map<String, Object> getIndexTimeSeries(@RequestBody Map<String, Object> requestMap){
        Map<String, Object> responseMap = new HashMap<>();
        String namespace = (String) requestMap.get("namespace");
        String serviceName = (String) requestMap.get("serviceName");
        String indexName = (String) requestMap.get("indexName");
        String startTime = (String) requestMap.get("startTime");
        String endTime = (String) requestMap.get("endTime");

        Double avg_index = serviceDataService.getIndexTimeSeries(namespace,serviceName,indexName,startTime,endTime);
        if(avg_index != null){
            responseMap.put("indexTimeSeries",avg_index);
        }else{ }
        return responseMap;
    }

    /**
     * 获取某一个ForecastCell
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
     * 保存ForecastCell的预测模型
     * 将生成的预测模型名称和参数持久化到数据库中
     *
     * @param requestMap
     * @return
     */
    @PutMapping("/forecast/forecastCellModel")
    public Map<String,Object> saveForecastingModel(@RequestBody Map<String,Object> requestMap){
        Map<String,Object> responseMap = new HashMap<>();
        if(!(requestMap.containsKey("ID")&&requestMap.containsKey("type")&&requestMap.containsKey("indexName")
                &&requestMap.containsKey("forecastingModel")&&requestMap.containsKey("modelParams"))){
            LOGGER.error("Request body doesn't contain all the right parameters");
            responseMap.put("isSucceed",false);
            return responseMap;
        }
        String cellId = requestMap.get("ID").toString();
        int type = Integer.parseInt(requestMap.get("type").toString());
        String forecastingIndex = requestMap.get("indexName").toString();
        String forecastingModel = requestMap.get("forecastingModel").toString();
        String modelParams = requestMap.get("modelParams").toString();
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
     *
     * @param requestMap
     * @return
     */
    @PutMapping("/forecast/forecastCellEndTime")
    public Map<String,Object> saveForecastingEndTime(@RequestBody Map<String,Object> requestMap){
        Map<String,Object> responseMap = new HashMap<>();
        if(!(requestMap.containsKey("ID")&&requestMap.containsKey("type")&&requestMap.containsKey("indexName")&&requestMap.containsKey("lastForecastTime"))){
            LOGGER.error("Request body doesn't contain all the right parameters");
            responseMap.put("isSucceed",false);
            return responseMap;
        }
        String cellId = requestMap.get("ID").toString();
        int type = Integer.parseInt(requestMap.get("type").toString());
        String forecastingIndex = requestMap.get("indexName").toString();
        String forecastingEndTime = requestMap.get("lastForecastTime").toString();
        int result = forecastCellService.saveForecastingEndTime(cellId,type,forecastingIndex,forecastingEndTime);
        if(result>0){
            responseMap.put("isSucceed",true);
        }else{
            responseMap.put("isSucceed",false);
        }
        return responseMap;
    }
}
