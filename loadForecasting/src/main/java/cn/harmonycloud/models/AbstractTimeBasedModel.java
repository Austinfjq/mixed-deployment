package cn.harmonycloud.models;


import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;
import cn.harmonycloud.exception.ModelNotInitializedException;

import java.util.Iterator;

public abstract class AbstractTimeBasedModel extends AbstractForecastingModel
{

    private String timeVariable = null;

    private double timeDiff = 0.0; //时间间隔

    private int minPeriods = 0;

    private DataSet observedValues; //原始预测数据

    private DataSet forecastValues; //预测数据序列

    private double minTimeValue;  //时间序列最早的那个点的时间

    private double maxTimeValue;  //时间序列最晚的那个点的时间
    

    public AbstractTimeBasedModel()
    {
    }

    public AbstractTimeBasedModel( String timeVariable )
    {
        this.timeVariable = timeVariable;
    }

    protected abstract int getNumberOfPeriods();

    public void init( DataSet dataSet )
    {

        System.out.println("超类!");
        if ( dataSet == null  || dataSet.size() == 0 )
            throw new IllegalArgumentException("Data set cannot be empty in call to init.");
        
        int minPeriods = getNumberOfPeriods();
        
        if ( dataSet.size() < minPeriods )
            throw new IllegalArgumentException("Data set too small. Need "
                                               +minPeriods
                                               +" data points, but only "
                                               +dataSet.size()
                                               +" passed to init.");
        
        observedValues = new DataSet( dataSet );
        observedValues.sort();
        
        // Check that intervals between data points are consistent
        //  i.e. check for complete data set
        Iterator<DataPoint> it = observedValues.iterator();
        
        DataPoint dp = it.next();  // first data point
        double lastValue = dp.getTimeValue();

        dp = it.next();  // second data point
        double currentValue = dp.getTimeValue();
        
        // Create data set in which to save new forecast values
        forecastValues = new DataSet();
        
        // Determine "standard"/expected time difference between observations
        timeDiff = currentValue - lastValue;
        
        // Min. time value is first observation time
        minTimeValue = lastValue;
        
        while ( it.hasNext() )
            {
                lastValue = currentValue;
                
                // Get next data point
                dp = it.next();
                currentValue = dp.getTimeValue();
                
                double diff = currentValue - lastValue;
                if ( Math.abs(timeDiff - diff) > TOLERANCE )
                    throw new IllegalArgumentException( "Inconsistent intervals found in time series, using variable '"+timeVariable+"'" );
                
                try
                    {
                        initForecastValue( currentValue );
                    }
                catch (IllegalArgumentException ex)
                    {
                        // We can ignore these during initialization
                    }
            }
        
        // ?????????????????????????????????????????/
        DataSet testDataSet = new DataSet( observedValues );
        int count = 0;
        while ( count++ < minPeriods )
            testDataSet.remove( (testDataSet.iterator()).next() );
        
        // Calculate accuracy
        calculateAccuracyIndicators( testDataSet );
    }

    

    private double initForecastValue( double timeValue )
        throws IllegalArgumentException
    {
        // Temporary store for current forecast value
        double forecast = forecast(timeValue);
        
        // Create new forecast data point
        DataPoint dpForecast = new DataPoint( forecast , timeValue);
        
        // Add new data point to forecast set
        forecastValues.add( dpForecast );
        
        // Update maximum time value, if necessary
        if ( timeValue > maxTimeValue )
            maxTimeValue = timeValue;
        
        return forecast;
    }
    

    public double forecast( DataPoint dataPoint )
        throws IllegalArgumentException
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        // Get value of independent variable (the time variable)
        double t = dataPoint.getTimeValue();
        
        return getForecastValue( t );
    }
    

    protected abstract double forecast( double timeValue )
        throws IllegalArgumentException;
    

    protected double getForecastValue( double timeValue )
        throws IllegalArgumentException
    {
        if ( timeValue>=minTimeValue-TOLERANCE
             && timeValue<=maxTimeValue+TOLERANCE )
            {
                // Find required forecast value in set of
                //  pre-computed forecasts
                Iterator<DataPoint> it = forecastValues.iterator();
                while ( it.hasNext() )
                    {
                        DataPoint dp = it.next();
                        double currentTime
                            = dp.getTimeValue();
                        
                        // If required data point found,
                        //  return pre-computed forecast
                        if ( Math.abs(currentTime-timeValue) < TOLERANCE )
                            return dp.getValue();
                    }
            }
        
        try
            {
                return initForecastValue( timeValue );
            }
        catch ( IllegalArgumentException idex )
            {
                throw new IllegalArgumentException(
                                                   "Time value (" + timeValue
                                                   + ") invalid for Time Based forecasting model. Valid values are in the range "
                                                   + minTimeValue + "-" + maxTimeValue
                                                   + " in increments of " + timeDiff + "." );
            }
    }
    

    protected double getObservedValue( double timeValue )
        throws IllegalArgumentException
    {
        // Find required forecast value in set of
        //  pre-computed forecasts
        Iterator<DataPoint> it = observedValues.iterator();
        while ( it.hasNext() )
            {
                DataPoint dp = it.next();
                double currentTime
                    = dp.getTimeValue();
                
                // If required data point found,
                //  return pre-computed forecast
                if ( Math.abs(currentTime-timeValue) < TOLERANCE )
                    return dp.getValue();
            }
        
        throw new
            IllegalArgumentException("No observation found for time value, "
                                     +timeVariable+"="+timeValue);
    }
    

    public String getTimeVariable()
    {
        return timeVariable;
    }
    

    public double getMinimumTimeValue()
    {
        return minTimeValue;
    }
    

    public double getMaximumTimeValue()
    {
        return maxTimeValue;
    }
    

    public String getIndependentVariable()
    {
        return timeVariable;
    }
    

    protected double getTimeInterval()
    {
        return timeDiff;
    }
    

    public String getForecastType()
    {
        return "Time Based Model";
    }

    public String toString()
    {
        return "time based model, spanning " + getNumberOfPeriods()
            + " periods and using a time variable of "
            + timeVariable+".";
    }
}