package cn.harmonycloud.service;

import cn.harmonycloud.beans.ForecastCell;
import cn.harmonycloud.beans.ForecastResultCell;

import java.util.List;

public interface IForecast {

    /**
     * @Author WANGYUZHONG
     * @Description //对一个forecastCell进行预测
     * @Date 18:00 2019/4/16
     * @Param 
     * @return
     **/
    List<ForecastResultCell> forecast(ForecastCell forecastCell);
}
