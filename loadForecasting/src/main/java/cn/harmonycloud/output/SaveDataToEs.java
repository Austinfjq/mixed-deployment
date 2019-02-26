package cn.harmonycloud.output;

import cn.harmonycloud.entry.ForecastResultCell;
import cn.harmonycloud.entry.HttpClientResult;
import cn.harmonycloud.tools.DateUtil;
import cn.harmonycloud.tools.HttpClientUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyuzhong
 * @date 18-12-14 上午11:13
 * @Despriction
 */
public class SaveDataToEs {

    public static HttpClientResult saveDataToEs(String datalist){
        Map<String,String> data = new HashMap<>();
        data.put("data",datalist);
        String url = "http://localhost:8080/forecast/save";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doPost(url, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpClientResult;
    }

    public static HttpClientResult saveData(List<ForecastResultCell> data) {
        System.out.println(DateUtil.forecastResultCellListToJson(data));
        return saveDataToEs(DateUtil.forecastResultCellListToJson(data));
    }
}
