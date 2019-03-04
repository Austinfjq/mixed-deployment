package cn.harmonycloud.strategy;

import cn.harmonycloud.beans.NodeLoad;
import cn.harmonycloud.beans.ServiceLoad;

/**
 * @author wangyuzhong
 * @date 19-1-21 下午4:16
 * @Despriction
 */
public abstract class AbstractServiceStrategy {
    private String name;
    private String className;
    private boolean status;
    private String description;

    public AbstractServiceStrategy() {
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
        return "AbstractServiceStrategy{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }

    public abstract boolean evaluate(ServiceLoad serviceLoad);
}
