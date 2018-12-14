package cn.harmonycloud.input;

import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;
import cn.harmonycloud.entry.ForecastCell;
import cn.harmonycloud.exception.ServiceIndexDataGotException;
import cn.harmonycloud.tools.HttpSend;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @author wangyuzhong
 * @date 18-12-6 下午7:02
 * @Despriction
 */
public class DeriveData {

    public static String getDataJson(ForecastCell forecastCell){
        String params = "dsd";
        String url = "http://localhost:8080/forecastCell/getdata";
        return HttpSend.sendPost(url,params);
    }

    public static DataSet getDataSet(ForecastCell forecastCell) {
        String dataSetStr = getDataJson(forecastCell);
        if (dataSetStr == null) {
            throw new ServiceIndexDataGotException("this ForecastCell has not data!");
        }

        Type type = new TypeReference<Collection<DataPoint>>() {}.getType();
        Collection<DataPoint> list = JSON.parseObject(dataSetStr, type);

        DataSet dataSet = new DataSet(forecastCell.getNumberOfPerPeriod(),list);

        return dataSet;
    }
}
