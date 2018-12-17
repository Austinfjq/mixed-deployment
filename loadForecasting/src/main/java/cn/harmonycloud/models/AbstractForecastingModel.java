//
//  OpenForecast - open source, general-purpose forecasting package.
//  Copyright (C) 2002-2011  Steven R. Gould
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public
//  License along with this library; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//

package cn.harmonycloud.models;


import cn.harmonycloud.ForecastingModel;
import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;
import cn.harmonycloud.exception.ModelNotInitializedException;

import java.util.Iterator;


public abstract class AbstractForecastingModel implements ForecastingModel
{

    static double TOLERANCE = 0.00000001;
    

    protected AccuracyIndicators accuracyIndicators = new AccuracyIndicators();
    
    /**
     * Remembers whether this model has been properly initialized.
     */
    protected boolean initialized = false;
    
    /**
     * Default constructor.
     */
    protected AbstractForecastingModel()
    {
    }

    public double getAIC()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getAIC();
    }
    

    public double getBias()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getBias();
    }

    public double getMAD()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getMAD();
    }

    public double getMAPE()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getMAPE();
    }

    public double getMSE()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getMSE();
    }

    public double getSAE()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getSAE();
    }
    

        public DataSet forecast(DataSet dataSet )
        {
            if ( !initialized )
                throw new ModelNotInitializedException();

            Iterator<DataPoint> it = dataSet.iterator();
            while ( it.hasNext() )
                {
                    DataPoint dp = it.next();
                    dp.setValue( forecast(dp) );
                }

            return dataSet;
        }


    protected void calculateAccuracyIndicators( DataSet dataSet )
    {
        initialized = true;
        
        // Reset various helper summations
        double sumErr = 0.0;
        double sumAbsErr = 0.0;
        double sumAbsPercentErr = 0.0;
        double sumErrSquared = 0.0;
        
        // Obtain the forecast values for this model
        DataSet forecastValues = new DataSet( dataSet );
        forecast( forecastValues );
        
        // Calculate the Sum of the Absolute Errors
        Iterator<DataPoint> it = dataSet.iterator();   //预测的数据
        Iterator<DataPoint> itForecast = forecastValues.iterator(); //实际的数据
        while ( it.hasNext() )
            {
                // Get next data point
                DataPoint dp = it.next();
                double x = dp.getValue();
                
                // Get next forecast value
                DataPoint dpForecast = itForecast.next();
                double forecastValue = dpForecast.getValue();
                
                // Calculate error in forecast, and update sums appropriately
                double error = forecastValue - x;
                sumErr += error;
                sumAbsErr += Math.abs( error );
                sumAbsPercentErr += Math.abs( error / x );
                sumErrSquared += error*error;
            }
        
        // Initialize the accuracy indicators
        int n = dataSet.size();
        int p = getNumberOfPredictors();

        accuracyIndicators.setAIC( n*Math.log(2*Math.PI)
                   + Math.log(sumErrSquared/n)
                   + 2 * ( p+2 ) );
        accuracyIndicators.setBias( sumErr / n );
        accuracyIndicators.setMAD( sumAbsErr / n );
        accuracyIndicators.setMAPE( sumAbsPercentErr / n );
        accuracyIndicators.setMSE( sumErrSquared / n );
        accuracyIndicators.setSAE( sumAbsErr );
    }
}
