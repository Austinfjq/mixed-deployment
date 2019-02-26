//package cn.harmonycloud.models;
//
//import cn.harmonycloud.entry.DataPoint;
//import cn.harmonycloud.entry.DataSet;
//
//import java.util.Comparator;
//import java.util.Iterator;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
///**
// * @author wangyuzhong
// * @date 18-12-27 下午3:18
// * @Despriction
// */
//public class ForecastModel {
//
//    private double minTimeValue;
//
//    private int periods;
//
//    private double alpha;
//
//    private double beta;
//
//    private double gamma;
//
//    private int countsPerPeriod;
//
//    private DataSet observeValues; //原始数据
//
//    private DataSet baseValues;  //基础序列值
//
//    private DataSet trendValues; //趋势序列值
//
//    private DataSet seasonalIndex; //季节序列值
//
//
//    public ForecastModel(DataSet dataPoints){
//        this.observeValues = dataPoints;
//    }
//
//
//    public ForecastModel(DataSet dataPoints,double alpha,double beta,double gamma){
//
//    }
//
//    public void init() {
//        observeValues.sort();
//        Iterator<DataPoint> iterator = this.observeValues.iterator();
//
//        this.minTimeValue = iterator.next().getTimeValue();
//
//        this.periods = this.observeValues.size()/this.countsPerPeriod;
//    }
//
//    private double calculateInitialLevel() {
//
//        if (observeValues.size()<this.countsPerPeriod) {
//            throw new IllegalArgumentException("Not have enough data!");
//        }
//        double sum = 0;
//        int i = 0;
//
//        Iterator<DataPoint> iterator = observeValues.iterator();
//        while(i < this.countsPerPeriod) {
//            sum += iterator.next().getValue();
//            i++;
//        }
//
//        return sum / countsPerPeriod;
//    }
//
//
//    public double calculateInitialTrend() {
//
//        double sum = 0;
//        int i = 0;
//
//        Iterator<DataPoint> iterator = this.observeValues.iterator();
//        DataSet firstPeriodDataSet = new DataSet();
//        DataSet secondPeriodDataSet = new DataSet();
//
//        while (i<this.countsPerPeriod) {
//            DataPoint dp = iterator.next();
//            firstPeriodDataSet.add(dp);
//            i++;
//        }
//
//        while (i<2*this.countsPerPeriod) {
//            DataPoint dp = iterator.next();
//            secondPeriodDataSet.add(dp);
//            i++;
//        }
//
//        Iterator<DataPoint> firstPeriodIretator = firstPeriodDataSet.iterator();
//        Iterator<DataPoint> secondPeriodIterator = secondPeriodDataSet.iterator();
//
//        for (int j = 0; j < this.countsPerPeriod; j++) {
//            DataPoint firstDataPoint = firstPeriodIretator.next();
//            DataPoint secondDataPoint = secondPeriodIterator.next();
//            sum += (secondDataPoint.getValue() - firstDataPoint.getValue());
//        }
//        return sum / (this.countsPerPeriod * this.countsPerPeriod);
//    }
//
//
//    private double[] calculateSeasonalIndices() {
//
//        //每个季节的平均值
//        double[] seasonalAverage = new double[this.periods];
//
//        //季节初始值
//        double[] seasonalIndices = new double[period];
//
//        //平均观察值　(实际值/对应季节的平均值)
//        double[] averagedObservations = new double[y.length];
//
//        //计算每个季节的平均值
//        for (int i = 0; i < seasons; i++) {
//            for (int j = 0; j < period; j++) {
//                seasonalAverage[i] += y[(i * period) + j];
//            }
//            seasonalAverage[i] /= period;
//        }
//
//        //计算平均观察值
//        for (int i = 0; i < seasons; i++) {
//            for (int j = 0; j < period; j++) {
//                averagedObservations[(i * period) + j] = y[(i * period) + j] / seasonalAverage[i];
//            }
//        }
//
//        //计算季节初始值　是所有季节对应的平均观察值的和
//        for (int i = 0; i < period; i++) {
//            for (int j = 0; j < seasons; j++) {
//                seasonalIndices[i] += averagedObservations[(j * period) + i];
//            }
//            seasonalIndices[i] /= seasons;
//        }
//
//        return seasonalIndices;
//    }
//
//    public DataSet forecast(int m) {
//
//    }
//
//    public void sort()
//    {
//
//        SortedMap<Double,DataPoint> sortedMap = new TreeMap<Double,DataPoint>(new Comparator<Double>()
//        {
//            public int compare( Double o1, Double o2 )
//            {
//                return o1.compareTo(o2);
//            }
//        } );
//        // Add each element in the array list to the sorted map.
//        Iterator<DataPoint> it = observeValues.iterator();
//        while ( it.hasNext() )
//        {
//            // By putting each DataPoint in the list, it will
//            // automatically sort them by key
//            DataPoint dp = it.next();
//            sortedMap.put(
//                    new Double(dp.getTimeValue()), dp );
//        }
//
//        observeValues.clear();
//
//        observeValues.addAll( sortedMap.values() );
//    }
//}
