package cn.harmonycloud.datacenter.entity.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午4:14
 * @Despriction
 */
public class ForecastCell {

    private int id;
    private int type; //0代表service，1代表node
    private String cellId; //能唯一标识一个service或者node
    private String forecastingIndex; //对该service的那个指标进行预测
    private int timeInterval;   //数据的时间间隔
    private int numberOfPerPeriod;  //在一个周期中的数据个数
    private String forecastingModel; //预测该指标的预测模型,默认是null
    private String modelParams; //预测模型的参数,json格式，如果forecastingModel为空，则该项也为空
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date forecastingEndTime; //上次预测到的时间点,如果是空则表示还没有预测

    public ForecastCell() {
    }

    public ForecastCell(int id, int type, String cellId, String forecastingIndex, int timeInterval, int numberOfPerPeriod, String forecastingModel, String modelParams, Date forecastingEndTime) {
        this.id = id;
        this.type = type;
        this.cellId = cellId;
        this.forecastingIndex = forecastingIndex;
        this.timeInterval = timeInterval;
        this.numberOfPerPeriod = numberOfPerPeriod;
        this.forecastingModel = forecastingModel;
        this.modelParams = modelParams;
        this.forecastingEndTime = forecastingEndTime;
    }

    public ForecastCell(int type, String cellId, String forecastingIndex, int timeInterval, int numberOfPerPeriod, String forecastingModel, String modelParams, Date forecastingEndTime) {
        this.type = type;
        this.cellId = cellId;
        this.forecastingIndex = forecastingIndex;
        this.timeInterval = timeInterval;
        this.numberOfPerPeriod = numberOfPerPeriod;
        this.forecastingModel = forecastingModel;
        this.modelParams = modelParams;
        this.forecastingEndTime = forecastingEndTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
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

    public Date getForecastingEndTime() {
        return forecastingEndTime;
    }

    public void setForecastingEndTime(Date forecastingEndTime) {
        this.forecastingEndTime = forecastingEndTime;
    }

    @Override
    public String toString() {
        return "ForecastCell{" +
                "id=" + id +
                ", type=" + type +
                ", cellId='" + cellId + '\'' +
                ", forecastingIndex='" + forecastingIndex + '\'' +
                ", timeInterval=" + timeInterval +
                ", numberOfPerPeriod=" + numberOfPerPeriod +
                ", forecastingModel='" + forecastingModel + '\'' +
                ", modelParams='" + modelParams + '\'' +
                ", forecastingEndTime=" + forecastingEndTime +
                '}';
    }
}
