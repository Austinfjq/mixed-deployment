package cn.harmonycloud.apiserver.controller;

import cn.harmonycloud.apiserver.DAO.ForecastResultSaveDAO;
import cn.harmonycloud.apiserver.entry.ForecastResultCell;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangyuzhong
 * @date 18-12-14 上午10:29
 * @Despriction
 */

@RestController
@RequestMapping("/forecast")
public class ForecastController {

    @Autowired//自动注入
    private ForecastResultSaveDAO forecastResultSaveDAO;


    @RequestMapping("/test")
    public  String testSpring(String data){
        System.out.println("访问到了");

        System.out.println(data);

        if (null == data) {
            System.out.println("no data!");
            return "true";
        }
//        List<ForecastResultCell> forecastResultCells = JSONArray.parseArray(data,ForecastResultCell.class);
//
//        List<Map<String,Object>> ls= null;
//        try {
//            ls = forecastResultSaveDAO.objectsToMaps(forecastResultCells);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(ls.toString());
//        try {
//            forecastResultSaveDAO.insertEsByBulk(ESConnector.getInstance(),"forecast","forecastService",ls);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return "true";
    }
}
