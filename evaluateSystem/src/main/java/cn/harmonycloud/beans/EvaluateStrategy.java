package cn.harmonycloud.beans;

/**
 * @author wangyuzhong
 * @date 19-1-21 上午11:07
 * @Despriction
 */
public class EvaluateStrategy {

    private String name;
    private String className;
    private boolean status;
    private String description;

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
        return "EvaluateStrategy{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }

}