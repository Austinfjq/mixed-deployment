package cn.harmonycloud.beans;

/**
 * @author wangyuzhong
 * @date 18-12-14 上午11:16
 * @Despriction
 */
public class ForecastResultCell {

    private String ID;
    private String forecastingIndex;
    private String date;
    private double value;

    public ForecastResultCell() {
    }

    public ForecastResultCell(String ID, String forecastingIndex, String date, double value) {
        this.ID = ID;
        this.forecastingIndex = forecastingIndex;
        this.date = date;
        this.value = value;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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
                "ID='" + ID + '\'' +
                ", forecastingIndex='" + forecastingIndex + '\'' +
                ", date='" + date + '\'' +
                ", value=" + value +
                '}';
    }

}
