package cn.harmonycloud.beans;

/**
 * @author wangyuzhong
 * @date 18-12-4 上午10:31
 * @Despriction
 */
public class DataPoint {

    //某个时刻的值
    private double value;

    //该时刻的时间戳
    private double timeValue;

    public DataPoint( double value , double timeValue)
    {
        this.value = value;
        this.timeValue = timeValue;
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(double timeValue) {
        this.timeValue = timeValue;
    }

    public DataPoint( DataPoint dataPoint )
    {
        this.value = dataPoint.getValue();

        this.timeValue = dataPoint.getTimeValue();
    }


    public boolean equals( DataPoint dp )
    {
        if ( value != dp.getValue() )
            return false;

        return !(timeValue != dp.getTimeValue());
    }

    public String toString()
    {
        String result = "(";

        result = result + "time=" + timeValue + "," + "value=" + value + ")";

        return result;
    }
}
