package cn.harmonycloud.models;


/**
 * A weighted moving average forecast model is based on an artificially
 * constructed time series in which the value for a given time period is
 * replaced by the weighted mean of that value and the values for some number
 * of preceding time periods. As you may have guessed from the description,
 * this model is best suited to time-series data; i.e. data that changes over
 * time.q
 *
 * <p>Since the forecast value for any given period is a weighted average of
 * the previous periods, then the forecast will always appear to "lag" behind
 * either increases or decreases in the observed (dependent) values. For
 * example, if a data series has a noticable upward trend then a weighted
 * moving average forecast will generally provide an underestimate of the
 * values of the dependent variable.
 *
 * <p>The weighted moving average model, like the moving average model, has
 * an advantage over other forecasting models in that it does smooth out
 * peaks and troughs (or valleys) in a set of observations. However, like the
 * moving average model, it also has several disadvantages. In particular this
 * model does not produce an actual equation. Therefore, it is not all that
 * useful as a medium-long range forecasting tool. It can only reliably be
 * used to forecast a few periods into the future.
 * @author Steven R. Gould
 * @since 0.4
 */
public class WeightedMovingAverageModel extends AbstractTimeBasedModel
{

    private double[] weights;

    public WeightedMovingAverageModel(double[] weights )
    {
        setWeights( weights );
    }

    protected WeightedMovingAverageModel()
    {
    }
    

    protected void setWeights( double[] weights )
    {
        int periods = weights.length;
        

        double sum = 0.0;
        for ( int w=0; w<periods; w++ )
            sum += weights[w];

        boolean adjust = false;
        if ( Math.abs( sum - 1.0 ) > TOLERANCE )
            adjust = true;

        this.weights = new double[ periods ];
        for ( int w=0; w<periods; w++ )
            this.weights[w] = (adjust ? weights[w]/sum : weights[w]);
    }
    

    protected double forecast( double timeValue )
        throws IllegalArgumentException
    {
        int periods = getNumberOfPeriods();
        double t = timeValue;
        double timeDiff = getTimeInterval();  // 时间间隔
        
        if ( timeValue - timeDiff*periods < getMinimumTimeValue() )
            return getObservedValue( t );
        
        double forecast = 0.0;
        for ( int p=periods-1; p>=0; p-- )
            {
                t -= timeDiff;
                try
                    {
                        forecast += weights[p]*getObservedValue( t );
                    }
                catch ( IllegalArgumentException iaex )
                    {
                        forecast += weights[p]*getForecastValue( t );
                    }
            }
        
        return forecast;
    }

    public int getNumberOfPredictors()
    {
        return 1;
    }

    protected int getNumberOfPeriods()
    {
        return weights.length;
    }

    public String getForecastType()
    {
        return "Weighted Moving Average";
    }
    

    public String toString()
    {
        return "weighted moving average model, spanning "
            + getNumberOfPeriods()
            + " periods and using an independent variable of "
            + getIndependentVariable() + ".";
    }
}
