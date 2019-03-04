package cn.harmonycloud;

import cn.harmonycloud.entry.*;
import cn.harmonycloud.input.DeriveData;
import cn.harmonycloud.models.*;
import cn.harmonycloud.output.SaveDataToEs;
import cn.harmonycloud.tools.Constant;
import com.alibaba.fastjson.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午3:03
 * @Despriction
 */
public class TaskThreadExecutor implements Runnable{

    private ForecastCell forecastCell;

    public TaskThreadExecutor(ForecastCell forecastCell) {
        this.forecastCell = forecastCell;
    }

    @Override
    public void run() {
        if (forecastCell == null) {
            System.out.println("forecast cell is null!");
            return;
        }

        DataSet dataSet = DeriveData.getDataSet(forecastCell);
        System.out.println(dataSet.toString());

        HoltWinters holtWinters = new HoltWinters(dataSet);

        DataSet result = holtWinters.forecast(Constant.FORECOST_COUNT);

        List<ForecastResultCell> forecastResultCellList = new ArrayList<>();

        for (DataPoint dataPoint:result) {
            ForecastResultCell forecastResultCell = new ForecastResultCell(forecastCell.getID(),forecastCell.getForecastingIndex(),stampToDate(NumberFormat.getInstance().format(dataPoint.getTimeValue()).replace(",","")),dataPoint.getValue());
            forecastResultCellList.add(forecastResultCell);
        }


        HttpClientResult httpClientResult = SaveDataToEs.saveData(forecastResultCellList);

        if (httpClientResult == null) {
            System.out.println("failed!");
        }

        if (httpClientResult.getCode() == 200) {
            System.out.println(httpClientResult.getContent());
        }else {
            System.out.println("save failed!");
        }

    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt*1000);
        res = simpleDateFormat.format(date);
        return res;
    }


    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("period",7);

        System.out.println(jsonObject.getIntValue("period"));
    }
}
