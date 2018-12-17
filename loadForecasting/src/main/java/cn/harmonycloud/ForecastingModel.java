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


import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;

/**
 * Defines a consistent interface that must be implemented by all Forecasting
 * Models. Note that any forecasting model should first be initialized by
 * calling init. Once init has been called, any of the other methods can be
 * expected to return reasonable results.
 * @author Steven R. Gould
 */
public interface ForecastingModel
{
    void init(DataSet dataSet);

    double getAIC();

    double getBias();

    double getMAD();

    double getMAPE();

    double getMSE();

    double getSAE();

    int getNumberOfPredictors();

    double forecast(DataPoint dataPoint);

    DataSet forecast(DataSet dataSet);

    String getForecastType();

    String toString();
}

