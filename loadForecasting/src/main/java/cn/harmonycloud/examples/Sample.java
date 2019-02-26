package cn.harmonycloud.examples;


import cn.harmonycloud.Forecaster;
import cn.harmonycloud.ForecastingModel;
import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;
import cn.harmonycloud.entry.ForecastResultCell;
import cn.harmonycloud.output.SaveDataToEs;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sample
{
    public static void main( String args[] )
    {
        // Create some sample observed data values
        DataSet dataSet = DataTranslation.getDataFromCsv();
        
        
        System.out.println("Input data, observed values");
        System.out.println( dataSet );
        
        // Obtain a good forecasting model given this data set
        ForecastingModel forecaster
            = Forecaster.getBestForecast( dataSet );
        System.out.println("Forecast model type selected: "+forecaster.getForecastType());
        System.out.println( forecaster.toString() );

        DataPoint dp = null;

        // Create additional data points for which forecast values are required
        DataSet requiredDataPoints = new DataSet();
        for ( int count=1545035040; count<1545038880; count=count+480 )
            {
                dp = new DataPoint( 0.0 ,count);
                
                requiredDataPoints.add( dp );
            }

        
        // Use the given forecasting model to forecast values for
        //  the required (future) data points
        forecaster.forecast( requiredDataPoints );

//        List<ForecastResultCell> list = new ArrayList<>();
//        Iterator<DataPoint> iterator = requiredDataPoints.iterator();
//
//        while(iterator.hasNext()) {
//            DataPoint dataPoint = iterator.next();
//            list.add(new ForecastResultCell("myservice","load",dataPoint.getTimeValue()+"",dataPoint.getValue()));
//        }
//
//        SaveDataToEs.saveDataToEs(forecastResultCellListToJson(list));
        // Output the results
        System.out.println("Output data, forecast values");
        System.out.println( requiredDataPoints );
    }

    public static String forecastResultCellToJson(ForecastResultCell forecastResultCell) {
        return JSON.toJSONString(forecastResultCell);
    }

    public static String forecastResultCellListToJson(List<ForecastResultCell> forecastResultCellList) {
        return JSON.toJSONString(forecastResultCellList);
    }
}
