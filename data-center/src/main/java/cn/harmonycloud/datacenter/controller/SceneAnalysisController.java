package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.service.IForecastDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
*@Author: shaodilong
*@Description: 场景分析模块接口
*@Date: Created in 2019/5/6 19:32
*@Modify By:
*/
@RestController
public class SceneAnalysisController {
    @Autowired
    private IForecastDataService forecastDataService;

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
}
