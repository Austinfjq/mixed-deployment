package cn.harmonycloud.models;


import cn.harmonycloud.entry.DataSet;

public class MovingAverageModel extends WeightedMovingAverageModel
{
    public MovingAverageModel()
    {
    }

    public MovingAverageModel(String independentVariable )
    {
        super( independentVariable );
    }
    

    public MovingAverageModel(int period )
    {
        double[] weights = new double[period];
        for ( int p=0; p<period; p++ )
            weights[p] = 1.0/period;
        
        setWeights( weights );
    }
    


    public MovingAverageModel(String independentVariable, int period )
    {
        super( independentVariable );
        
        double[] weights = new double[period];
        for ( int p=0; p<period; p++ )
            weights[p] = 1.0/period;
        
        setWeights( weights );
    }


    public void init( DataSet dataSet )
    {
        if ( getNumberOfPeriods() <= 0 )
            {
                int period = getNumberOfPeriods();

                double[] weights = new double[period];
                for ( int p=0; p<period; p++ )
                    weights[p] = 1/period;
                
                setWeights( weights );
            }
        
        super.init( dataSet );
    }
    

    public String getForecastType()
    {
        return "Moving average";
    }

    public String toString()
    {
        return "Moving average model, spanning " + getNumberOfPeriods()
            + " periods and using an independent variable of "
            + getIndependentVariable()+".";
    }
}
