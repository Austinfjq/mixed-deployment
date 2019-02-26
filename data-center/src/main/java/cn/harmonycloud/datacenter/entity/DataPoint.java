package cn.harmonycloud.datacenter.entity;

import java.util.Objects;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:21
 *@Modify By:
 */

public class DataPoint {
    private double value;
    private String time;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DataPoint() {
    }

    public DataPoint(double value, String time) {
        this.value = value;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataPoint dataPoint = (DataPoint) o;
        return Double.compare(dataPoint.value, value) == 0 &&
                Objects.equals(time, dataPoint.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, time);
    }

    @Override
    public String toString() {
        return "DataPoint{" +
                "value=" + value +
                ", time='" + time + '\'' +
                '}';
    }
}
