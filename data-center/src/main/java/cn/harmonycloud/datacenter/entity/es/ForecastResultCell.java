package cn.harmonycloud.datacenter.entity.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import static cn.harmonycloud.datacenter.tools.Constant.FORECAST_RESULT_CELL_INDEX;
import static cn.harmonycloud.datacenter.tools.Constant.FORECAST_RESULT_CELL_TYPE;

/**
 * @author wangyuzhong
 * @date 18-12-14 上午11:16
 * @Despriction
 */
@Document(indexName = FORECAST_RESULT_CELL_INDEX, type = FORECAST_RESULT_CELL_TYPE)
public class ForecastResultCell {
    @Id
    private String id;
    private String forecastResultCellID;
    private String forecastingIndex;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private String date;
    @Field(type = FieldType.Double)
    private double value;

    public ForecastResultCell(String forecastResultCellID, String forecastingIndex, String date, double value) {
        this.forecastResultCellID = forecastResultCellID;
        this.forecastingIndex = forecastingIndex;
        this.date = date;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForecastResultCellID() {
        return forecastResultCellID;
    }

    public void setForecastResultCellID(String forecastResultCellID) {
        this.forecastResultCellID = forecastResultCellID;
    }

    public String getForecastingIndex() {
        return forecastingIndex;
    }

    public void setForecastingIndex(String forecastingIndex) {
        this.forecastingIndex = forecastingIndex;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ForecastResultCell{" +
                "id='" + id + '\'' +
                ", forecastResultCellID='" + forecastResultCellID + '\'' +
                ", forecastingIndex='" + forecastingIndex + '\'' +
                ", date='" + date + '\'' +
                ", value=" + value +
                '}';
    }

}
