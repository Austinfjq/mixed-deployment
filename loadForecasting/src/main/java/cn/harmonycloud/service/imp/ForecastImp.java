package cn.harmonycloud.service.imp;

import cn.harmonycloud.beans.DataPoint;
import cn.harmonycloud.beans.DataSet;
import cn.harmonycloud.beans.ForecastCell;
import cn.harmonycloud.beans.ForecastResultCell;
import cn.harmonycloud.models.HoltWinters;
import cn.harmonycloud.service.IData;
import cn.harmonycloud.service.IForecast;
import cn.harmonycloud.tools.DateUtil;
import cn.harmonycloud.tools.PropertyFileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @classname：ForecastImp
 * @author：WANGYUZHONG
 * @date：2019/4/16 17:58
 * @description:TODO
 * @version:1.0
 **/
public class ForecastImp implements IForecast {

    private final static Logger LOGGER = LoggerFactory.getLogger(ForecastImp.class);

    @Override
    public List<ForecastResultCell> forecast(ForecastCell forecastCell) {
        DataSet dataSet = getDataSet(forecastCell);
        IData iData = new DataImp();

        if (null == dataSet) {
            LOGGER.error("get dataset fialed!");
            return null;
        }

        HoltWinters holtWinters = null;
        if (forecastCell.getForecastingModel().equals("")) {
            LOGGER.info("this forecastCell without forecastingModel!");
            holtWinters = new HoltWinters(dataSet);
            iData.savaForecastModel(forecastCell.getID(), String.valueOf(forecastCell.getType()), forecastCell.getForecastingIndex(), holtWinters.getModelName(), holtWinters.getParams());
        } else if (forecastCell.getForecastingModel().equals("HoltWinters")){
            JSONObject jsonObject = JSONObject.parseObject(forecastCell.getModelParams());
            double alpha = jsonObject.getDouble("alpha");
            double beta = jsonObject.getDouble("beta");
            double gamma = jsonObject.getDouble("gamma");
            holtWinters = new HoltWinters(dataSet, alpha, beta, gamma);
        } else {
            LOGGER.error("this model is not support!");
        }

        if (null == holtWinters) {
            LOGGER.error("the model is null!");
            return null;
        }

        DataSet forecastResult = holtWinters.forecast(Integer.valueOf(PropertyFileUtil.getValue("PredectPeriod"))/forecastCell.getTimeInterval());

        if (null == forecastResult) {
            LOGGER.error("forecast failed!");
            return null;
        }

        List<ForecastResultCell> forecastResultCellList = new ArrayList<>();

        for (DataPoint dataPoint:forecastResult) {
            ForecastResultCell forecastResultCell = new ForecastResultCell(forecastCell.getID(),forecastCell.getForecastingIndex(),DateUtil.stampToDate(NumberFormat.getInstance().format(dataPoint.getTimeValue()).replace(",","")),dataPoint.getValue());
            forecastResultCellList.add(forecastResultCell);
        }

        return forecastResultCellList;
    }

    private DataSet getDataSet(ForecastCell forecastCell) {
        IData iData = new DataImp();

        String endTime = forecastCell.getForcastingEndTime();

        if (endTime.equals("")) {
            endTime = DateUtil.getCurrentTime();
        }

        String startTime = DateUtil.getStartTime(endTime, Integer.valueOf(PropertyFileUtil.getValue("HistoryDataPeriods"))*forecastCell.getNumberOfPerPeriod()*forecastCell.getTimeInterval());
        String dataSetStr = iData.getIndexHistoryData(forecastCell.getID(), String.valueOf(forecastCell.getType()), forecastCell.getForecastingIndex(), startTime, endTime);

        Type type = new TypeReference<Collection<DataPoint>>() {}.getType();
        Collection<DataPoint> list = JSON.parseObject(dataSetStr, type);

        DataSet dataSet = new DataSet(forecastCell.getNumberOfPerPeriod(),forecastCell.getTimeInterval(),list);

        dataSet.init();
        return dataSet;
    }
}
