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

package cn.harmonycloud;


import cn.harmonycloud.entry.DataSet;
import cn.harmonycloud.models.DoubleExponentialSmoothingModel;
import cn.harmonycloud.models.MovingAverageModel;
import cn.harmonycloud.models.SimpleExponentialSmoothingModel;
import cn.harmonycloud.models.TripleExponentialSmoothingModel;


public class Forecaster
{
    private Forecaster()
    {
    }

    public static ForecastingModel getBestForecast(DataSet dataSet )
    {
        return getBestForecast( dataSet, EvaluationCriteria.BLEND );
    }
    

    public static ForecastingModel getBestForecast(DataSet dataSet, EvaluationCriteria evalMethod )
    {
        ForecastingModel bestModel = null;

                // Try moving average model
                ForecastingModel model = new MovingAverageModel(5);
                model.init( dataSet );
                if ( betterThan( model, bestModel, evalMethod ) )
                    bestModel = model;
                
                // Try moving average model using periods per year if avail.
                if ( dataSet.getPeriodsPerYear() > 0 )
                    {
                        model = new MovingAverageModel( dataSet.getPeriodsPerYear() );
                        model.init( dataSet );
                        if ( betterThan( model, bestModel, evalMethod ) )
                            bestModel = model;
                    }

                
                // Try the best fit simple exponential smoothing model
                model = SimpleExponentialSmoothingModel.getBestFitModel(dataSet);
                if ( betterThan( model, bestModel, evalMethod ) )
                    bestModel = model;
                
                // Try the best fit double exponential smoothing model
                model = DoubleExponentialSmoothingModel.getBestFitModel(dataSet);
                if ( betterThan( model, bestModel, evalMethod ) )
                    bestModel = model;
                
                // Try the best fit triple exponential smoothing model
                model = TripleExponentialSmoothingModel.getBestFitModel(dataSet);
                if ( betterThan( model, bestModel, evalMethod ) )
                    bestModel = model;
        
        return bestModel;
    }
    

    private static boolean betterThan( ForecastingModel model1,
                                       ForecastingModel model2,
                                       EvaluationCriteria evalMethod )
    {
        // Special case. Any model is better than no model!
        if ( model2 == null )
            return true;
        
        double tolerance = 0.00000001;
        
        // Use evaluation method as requested by user
        if ( evalMethod == EvaluationCriteria.BIAS )
            return ( model1.getBias() <= model2.getBias() );
        else if ( evalMethod == EvaluationCriteria.MAD )
            return ( model1.getMAD() <= model2.getMAD() );
        else if ( evalMethod == EvaluationCriteria.MAPE )
            return ( model1.getMAPE() <= model2.getMAPE() );
        else if ( evalMethod == EvaluationCriteria.MSE )
            return ( model1.getMSE() <= model2.getMSE() );
        else if ( evalMethod == EvaluationCriteria.SAE )
            return ( model1.getSAE() <= model2.getSAE() );
        else if ( evalMethod == EvaluationCriteria.AIC )
            return ( model1.getAIC() <= model2.getAIC() );
        
        // Default evaluation method is a combination
        int score = 0;
        if ( model1.getAIC()-model2.getAIC() <= tolerance )
            score++;
        else if ( model1.getAIC()-model2.getAIC() >= tolerance )
            score--;
        
        if ( model1.getBias()-model2.getBias() <= tolerance )
            score++;
        else if ( model1.getBias()-model2.getBias() >= tolerance )
            score--;
        
        if ( model1.getMAD()-model2.getMAD() <= tolerance )
            score++;
        else if ( model1.getMAD()-model2.getMAD() >= tolerance )
            score--;
        
        if ( model1.getMAPE()-model2.getMAPE() <= tolerance )
            score++;
        else if ( model1.getMAPE()-model2.getMAPE() >= tolerance )
            score--;
        
        if ( model1.getMSE()-model2.getMSE() <= tolerance )
            score++;
        else if ( model1.getMSE()-model2.getMSE() >= tolerance )
            score--;
        
        if ( model1.getSAE()-model2.getSAE() <= tolerance )
            score++;
        else if ( model1.getSAE()-model2.getSAE() >= tolerance )
            score--;
        
        if ( score == 0 )
            {
                // At this point, we're still unsure which one is best
                //  so we'll take another approach
                double diff = model1.getAIC() - model2.getAIC()
                    + model1.getBias() - model2.getBias()
                    + model1.getMAD()  - model2.getMAD()
                    + model1.getMAPE() - model2.getMAPE()
                    + model1.getMSE()  - model2.getMSE()
                    + model1.getSAE()  - model2.getSAE();
                return ( diff < 0 );
            }
        
        return ( score > 0 );
    }
}
