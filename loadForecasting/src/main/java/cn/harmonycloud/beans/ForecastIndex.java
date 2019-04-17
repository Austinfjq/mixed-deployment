package cn.harmonycloud.beans;

/**
 * @classname：ForecastIndex
 * @author：WANGYUZHONG
 * @date：2019/4/16 16:40
 * @description:TODO
 * @version:1.0
 **/
public class ForecastIndex {

    private String indexName;
    private boolean status;
    private String description;


    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
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
        return "ForecastIndex{" +
                "indexName='" + indexName + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
