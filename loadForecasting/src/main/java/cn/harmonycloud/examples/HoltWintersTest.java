package cn.harmonycloud.examples;

import cn.harmonycloud.ForecastingModel;
import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;
import cn.harmonycloud.models.TripleExponentialSmoothingModel;

/**
 * @author wangyuzhong
 * @date 18-12-17 下午2:37
 * @Despriction
 */
public class HoltWintersTest {

    public static void main(String[] args) {
        DataSet dataSet = DataTranslation.getDataFromTxt();
        ForecastingModel model = TripleExponentialSmoothingModel.getBestFitModel(dataSet);
        System.out.println("Forecast model type selected: "+model.getForecastType());
        System.out.println( model.toString() );

        DataSet requiredDataPoints = new DataSet();

        //初始化需要预测的时间点,预测值设为0
        for ( int count=62; count<69; count++ )
        {
            DataPoint dp = new DataPoint( 0.0 ,count);
            requiredDataPoints.add( dp );
        }

        model.forecast( requiredDataPoints );

        System.out.println(requiredDataPoints.toString());
    }
}
