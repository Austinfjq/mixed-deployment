package cn.harmonycloud.models;


import cn.harmonycloud.beans.DataPoint;
import cn.harmonycloud.beans.DataSet;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;

/**
 * @author wangyuzhong
 * @date 18-10-29 下午2:48
 * @Despriction
 */
public class HoltWinters {

    private static final String ModelName = "HoltWinters";
    private double alpha;
    private double beta;
    private double gamma;
    private double initialLevel;
    private double initialTrend;
    private double[] initialSeasonalIndices;
    private double[] originalData;
    private DataSet dataPoints;
    private boolean initial = false;


    public HoltWinters(DataSet dataPoints) {
        this.dataPoints = dataPoints;
        init();
        findBestParams();
    }

    public HoltWinters(DataSet dataPoints, double alpha, double beta, double gamma) {
        this.dataPoints = dataPoints;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        init();
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    public double getGamma() {
        return gamma;
    }

    public void init() {
        if (null == dataPoints) {
            throw new RuntimeException("Data is null!");
        }

        int i=0;
        originalData = new double[dataPoints.size()];

        Iterator<DataPoint> iterator = dataPoints.iterator();
        while(iterator.hasNext()) {
            originalData[i] = iterator.next().getValue();
            i++;
        }

        initialLevel = calculateInitialLevel(originalData,dataPoints.getPeriodsPerYear());
        initialTrend = calculateInitialTrend(originalData,dataPoints.getPeriodsPerYear());
        initialSeasonalIndices = calculateSeasonalIndices(originalData,dataPoints.getPeriodsPerYear(),dataPoints.size()/dataPoints.getPeriodsPerYear());

        initial = true;
    }

    public DataSet forecast(int m) {
        if (!initial) {
            init();
        }
        double[] result = calculateHoltWinters(m);

        DataSet dataSet = new DataSet();

        for (int k = dataPoints.size(); k<result.length; k++) {
            DataPoint dataPoint = new DataPoint(result[k],dataPoints.getMaxTime()+(k-dataPoints.size()+1)*dataPoints.getTimeInterval());
            dataSet.add(dataPoint);
        }
        return dataSet;
    }

    private double[] calculateHoltWinters(int m) {

        //St是水平组件值
        double[] St = new double[originalData.length];
        //Bt是趋势组件值
        double[] Bt = new double[originalData.length];
        //It是季节组件值
        double[] It = new double[originalData.length];
        //Ft是预测值
        double[] Ft = new double[originalData.length + m];

        //Initialize base values
        St[1] = initialLevel;
        Bt[1] = initialTrend;

        for (int i = 0; i < dataPoints.getPeriodsPerYear(); i++) {
            It[i] = initialSeasonalIndices[i];
        }

        Ft[m] = (St[0] + (m * Bt[0])) * It[0];//This is actually 0 since Bt[0] = 0
        Ft[m + 1] = (St[1] + (m * Bt[1])) * It[1];//Forecast starts from period + 2

        //Start calculations
        for (int i = 2; i < originalData.length; i++) {

            //Calculate overall smoothing
            if ((i - dataPoints.getPeriodsPerYear()) >= 0) {
                St[i] = alpha * originalData[i] / It[i - dataPoints.getPeriodsPerYear()] + (1.0 - alpha) * (St[i - 1] + Bt[i - 1]);
            } else {
                St[i] = alpha * originalData[i] + (1.0 - alpha) * (St[i - 1] + Bt[i - 1]);
            }

            //Calculate trend smoothing
            Bt[i] = gamma * (St[i] - St[i - 1]) + (1 - gamma) * Bt[i - 1];

            //Calculate seasonal smoothing
            if ((i - dataPoints.getPeriodsPerYear()) >= 0) {
                It[i] = beta * originalData[i] / St[i] + (1.0 - beta) * It[i - dataPoints.getPeriodsPerYear()];
            }

            //Calculate forecast
            if (((i + m) >= dataPoints.getPeriodsPerYear())) {
                Ft[i + m] = (St[i] + (m * Bt[i])) * It[i - dataPoints.getPeriodsPerYear() + m];
            }

        }

        return Ft;
    }


    private double[] calculateHoltWinters(double alpha,double beta,double gamma,int m) {

        //St是水平组件值
        double[] St = new double[originalData.length];

        //Bt是趋势组件值
        double[] Bt = new double[originalData.length];

        //It是季节组件值
        double[] It = new double[originalData.length];

        //Ft是预测值
        double[] Ft = new double[originalData.length + m];

        //Initialize base values
        St[1] = initialLevel;
        Bt[1] = initialTrend;

        for (int i = 0; i < dataPoints.getPeriodsPerYear(); i++) {
            It[i] = initialSeasonalIndices[i];
        }

        Ft[m] = (St[0] + (m * Bt[0])) * It[0];//This is actually 0 since Bt[0] = 0
        Ft[m + 1] = (St[1] + (m * Bt[1])) * It[1];//Forecast starts from period + 2

        //Start calculations
        for (int i = 2; i < originalData.length; i++) {

            //Calculate overall smoothing
            if ((i - dataPoints.getPeriodsPerYear()) >= 0) {
                St[i] = alpha * originalData[i] / It[i - dataPoints.getPeriodsPerYear()] + (1.0 - alpha) * (St[i - 1] + Bt[i - 1]);
            } else {
                St[i] = alpha * originalData[i] + (1.0 - alpha) * (St[i - 1] + Bt[i - 1]);
            }

            //Calculate trend smoothing
            Bt[i] = gamma * (St[i] - St[i - 1]) + (1 - gamma) * Bt[i - 1];

            //Calculate seasonal smoothing
            if ((i - dataPoints.getPeriodsPerYear()) >= 0) {
                It[i] = beta * originalData[i] / St[i] + (1.0 - beta) * It[i - dataPoints.getPeriodsPerYear()];
            }

            //Calculate forecast
            if (((i + m) >= dataPoints.getPeriodsPerYear())) {
                Ft[i + m] = (St[i] + (m * Bt[i])) * It[i - dataPoints.getPeriodsPerYear() + m];
            }

        }
        return Ft;
    }

    private static double calculateInitialLevel(double[] y, int period) {

         double sum = 0;
         for (int i = 0; i < period; i++) {
             sum += y[i];
         }

         return sum / period;
    }

    private double calculateInitialTrend(double[] y, int period) {

        double sum = 0;

        for (int i = 0; i < period; i++) {
            sum += (y[period + i] - y[i]);
        }
        return sum / (period * period);
    }

    private static double[] calculateSeasonalIndices(double[] y, int period, int seasons) {

        //每个季节的平均值
        double[] seasonalAverage = new double[seasons];
        //季节初始值
        double[] seasonalIndices = new double[period];
        //平均观察值　(实际值/对应季节的平均值)
        double[] averagedObservations = new double[y.length];
        //计算每个季节的平均值
        for (int i = 0; i < seasons; i++) {
            for (int j = 0; j < period; j++) {
                seasonalAverage[i] += y[(i * period) + j];
            }
            seasonalAverage[i] /= period;
        }

        //计算平均观察值
        for (int i = 0; i < seasons; i++) {
            for (int j = 0; j < period; j++) {
                averagedObservations[(i * period) + j] = y[(i * period) + j] / seasonalAverage[i];
            }
        }

        //计算季节初始值　是所有季节对应的平均观察值的和
        for (int i = 0; i < period; i++) {
            for (int j = 0; j < seasons; j++) {
                seasonalIndices[i] += averagedObservations[(j * period) + i];
            }
            seasonalIndices[i] /= seasons;
        }

        return seasonalIndices;
    }

    public void findBestParams() {
        double minDiff = Double.MAX_VALUE;
        for (double alpha = 0; alpha <= 1.0; alpha = alpha+0.01) {
            for (double beta = 0; beta <= 1.0; beta = beta+0.01) {
                for (double gamma = 0; gamma <= 1.0; gamma = gamma+0.01) {
                    double diff = 0;
                    HoltWinters holtWinters = new HoltWinters(this.dataPoints, alpha, beta, gamma);
                    double[] calRes = holtWinters.calculateHoltWinters(0);
                    for (int i = 0; i < originalData.length; i++) {
                        diff+=(Math.pow(calRes[i]-originalData[i],2));
                    }
                    double msg = diff/originalData.length;
                    if (msg<minDiff) {
                        this.alpha = alpha;
                        this.beta = beta;
                        this.gamma = gamma;
                        minDiff = msg;
                    }
                }
            }
        }
    }


    public String getParams() {
        JSONObject jsonObject  = new JSONObject();
        jsonObject.put("alpha", this.alpha);
        jsonObject.put("beta", this.beta);
        jsonObject.put("gamma", this.gamma);

        return jsonObject.toJSONString();
    }

    public String getModelName() {
        return ModelName;
    }
}
