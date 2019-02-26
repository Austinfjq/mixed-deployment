package cn.harmonycloud.entry;

import java.util.Date;
import java.util.Map;

public class Config {

    private String time;
    private Map<String, Long> podAddList;
    private Map<String, Long> podDelList;

    public Config() {
    }

    public String getTime() {
        return time;
    }

    public Map<String, Long> getPodAddList() {
        return podAddList;
    }

    public Map<String, Long> getPodDelList() {
        return podDelList;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPodAddList(Map<String, Long> podAddList) {
        this.podAddList = podAddList;
    }

    public void setPodDelList(Map<String, Long> podDelList) {
        this.podDelList = podDelList;
    }
}
