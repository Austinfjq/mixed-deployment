package cn.harmonycloud.entry;

import java.util.Date;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午4:14
 * @Despriction
 */
public class ForecastCell {

    private int id;
    private String ID; //能唯一标识一个serice或者node
    private int type;
    private String forecastingIndex; //对该service的那个指标进行预测
    private int timeInterval;   //数据的时间间隔
    private int numberOfPerPeriod;  //在一个周期中的数据个数
    private String forecastingModel; //预测该指标的预测模型,默认是null
    private String modelParams; //预测模型的参数,json格式，如果forecastingModel为空，则该项也为空
    private Date forcastingEndTime; //上次预测到的时间点,如果是空则表示还没有预测


    public ForecastCell() {
    }

    public ForecastCell(int id, String ID, int type, String forecastingIndex, int timeInterval, int numberOfPerPeriod, String forecastingModel, String modelParams, Date forcastingEndTime) {
        this.id = id;
        this.ID = ID;
        this.type = type;
        this.forecastingIndex = forecastingIndex;
        this.timeInterval = timeInterval;
        this.numberOfPerPeriod = numberOfPerPeriod;
        this.forecastingModel = forecastingModel;
        this.modelParams = modelParams;
        this.forcastingEndTime = forcastingEndTime;
    }

    public ForecastCell(String ID, int type, String forecastingIndex, int timeInterval, int numberOfPerPeriod, String forecastingModel, String modelParams, Date forcastingEndTime) {
        this.ID = ID;
        this.type = type;
        this.forecastingIndex = forecastingIndex;
        this.timeInterval = timeInterval;
        this.numberOfPerPeriod = numberOfPerPeriod;
        this.forecastingModel = forecastingModel;
        this.modelParams = modelParams;
        this.forcastingEndTime = forcastingEndTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getForecastingIndex() {
        return forecastingIndex;
    }

    public void setForecastingIndex(String forecastingIndex) {
        this.forecastingIndex = forecastingIndex;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public int getNumberOfPerPeriod() {
        return numberOfPerPeriod;
    }

    public void setNumberOfPerPeriod(int numberOfPerPeriod) {
        this.numberOfPerPeriod = numberOfPerPeriod;
    }

    public String getForecastingModel() {
        return forecastingModel;
    }

    public void setForecastingModel(String forecastingModel) {
        this.forecastingModel = forecastingModel;
    }

    public String getModelParams() {
        return modelParams;
    }

    public void setModelParams(String modelParams) {
        this.modelParams = modelParams;
    }

    public Date getForcastingEndTime() {
        return forcastingEndTime;
    }

    public void setForcastingEndTime(Date forcastingEndTime) {
        this.forcastingEndTime = forcastingEndTime;
    }

    @Override
    public String toString() {
        return "ForecastCell{" +
                "id=" + id +
                ", ID='" + ID + '\'' +
                ", type='" + type + '\'' +
                ", forecastingIndex='" + forecastingIndex + '\'' +
                ", timeInterval=" + timeInterval +
                ", numberOfPerPeriod=" + numberOfPerPeriod +
                ", forecastingModel='" + forecastingModel + '\'' +
                ", modelParams='" + modelParams + '\'' +
                ", forcastingEndTime=" + forcastingEndTime +
                '}';
    }
}
