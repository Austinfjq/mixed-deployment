package cn.harmonycloud.input;

import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;
import cn.harmonycloud.entry.ForecastCell;
import cn.harmonycloud.entry.HttpClientResult;
import cn.harmonycloud.exception.ServiceIndexDataGotException;
import cn.harmonycloud.tools.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyuzhong
 * @date 18-12-6 下午7:02
 * @Despriction
 */
public class DeriveData {

    public static String getDataJson(ForecastCell forecastCell){
        Map<String,String> params = new HashMap<>();
        params.put("ID",forecastCell.getID());
        params.put("type",String.valueOf(forecastCell.getType()));
        params.put("index",forecastCell.getForecastingIndex());
        params.put("startTime",forecastCell.getForcastingEndTime().toString());
        String url = "http://localhost:8080/forecast/indexData";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doPost(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            System.out.println("get index data failed!");
            return null;
        }

        return httpClientResult.getContent();

    }

    public static DataSet getDataSet(ForecastCell forecastCell) {
        String dataSetStr = getDataJson(forecastCell);
        if (dataSetStr == null) {
            throw new ServiceIndexDataGotException("this ForecastCell has not data!");
        }

        Type type = new TypeReference<Collection<DataPoint>>() {}.getType();
        Collection<DataPoint> list = JSON.parseObject(dataSetStr, type);

        DataSet dataSet = new DataSet(forecastCell.getNumberOfPerPeriod(),forecastCell.getTimeInterval(),list);

        dataSet.init();
        return dataSet;
    }
}
