package cn.harmonycloud.entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Config {

    private String time;
    private Map<String, Long> podAddList;
    private Map<String, Long> podDelList;
    private int onlineNum;
    private int offlineNum;

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

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }

    public int getOfflineNum() {
        return offlineNum;
    }

    public void setOfflineNum(int offlineNum) {
        this.offlineNum = offlineNum;
    }
}
