package cn.harmonycloud.strategy;

import cn.harmonycloud.beans.NodeLoad;

/**
 * @author wangyuzhong
 * @date 19-1-21 上午11:07
 * @Despriction
 */
public abstract class AbstractNodeStrategy {

    private String name;
    private String className;
    private double maxValue;
    private boolean status;
    private String description;

    public AbstractNodeStrategy() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AbstractNodeStrategy{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", maxValue=" + maxValue +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }

    public abstract boolean evaluate(NodeLoad nodeLoad);
}