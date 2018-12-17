package cn.harmonycloud;

import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;
import cn.harmonycloud.entry.ForecastCell;
import cn.harmonycloud.input.DeriveData;

/**
 * @author wangyuzhong
 * @date 18-12-10 上午11:32
 * @Despriction
 */
public class NodeTaskThreadExecutor implements Runnable{

    private ForecastCell forecastCell;

    public NodeTaskThreadExecutor(ForecastCell forecastCell) {
        this.forecastCell = forecastCell;
    }

    @Override
    public void run() {
        if (forecastCell == null) {
            return;
        }

        DataSet dataSet = DeriveData.getDataSet(forecastCell);
        dataSet.setPeriodsPerYear(forecastCell.getNumberOfPerPeriod());

        if (forecastCell.getForecastingModel().equals("")) {
            ForecastingModel forecastingModel = Forecaster.getBestForecast(dataSet);

            if (forecastingModel == null) {
                System.out.println("get beat forecast model failed!");
            }

            System.out.println("Forecast model type selected: "+forecastingModel.getForecastType());
            System.out.println( forecastingModel.toString() );


            // Create additional data points for which forecast values are required
            DataSet requiredDataPoints = new DataSet();
            DataPoint dp;
            for ( int count=7; count<15; count++ )
            {
                dp = new DataPoint( 0.0 ,count);

                requiredDataPoints.add( dp );
            }

            // Dump data set before forecast
            System.out.println("Required data set before forecast");
            System.out.println( requiredDataPoints );

            // Use the given forecasting model to forecast values for
            //  the required (future) data points
            forecastingModel.forecast( requiredDataPoints );

            // Output the results
            System.out.println("Output data, forecast values");
            System.out.println( requiredDataPoints );
        }

    }
}
