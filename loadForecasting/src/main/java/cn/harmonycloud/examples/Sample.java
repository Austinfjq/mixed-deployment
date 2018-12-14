package cn.harmonycloud.examples;


import cn.harmonycloud.Forecaster;
import cn.harmonycloud.ForecastingModel;
import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;

public class Sample
{
    public static void main( String args[] )
    {
        // Create some sample observed data values
        DataSet observedData = new DataSet();
        DataPoint dp;
        
        dp = new DataPoint( 2.1 , 0);
        observedData.add( dp );
        
        dp = new DataPoint( 7.7 ,1);
        observedData.add( dp );
        
        dp = new DataPoint( 13.6 ,2);
        observedData.add( dp );
        
        dp = new DataPoint( 27.2 ,3);
        observedData.add( dp );
        
        dp = new DataPoint( 40.9 ,4);
        observedData.add( dp );
        
        dp = new DataPoint( 61.1 ,5);
        observedData.add( dp );
        
        dp = new DataPoint( 59.2 ,6);
        observedData.add( dp );

        observedData.setPeriodsPerYear(2);
        
        
        System.out.println("Input data, observed values");
        System.out.println( observedData );
        
        // Obtain a good forecasting model given this data set
        ForecastingModel forecaster
            = Forecaster.getBestForecast( observedData );
        System.out.println("Forecast model type selected: "+forecaster.getForecastType());
        System.out.println( forecaster.toString() );
        
        
        // Create additional data points for which forecast values are required
        DataSet requiredDataPoints = new DataSet();
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
        forecaster.forecast( requiredDataPoints );
        
        // Output the results
        System.out.println("Output data, forecast values");
        System.out.println( requiredDataPoints );
    }
}
