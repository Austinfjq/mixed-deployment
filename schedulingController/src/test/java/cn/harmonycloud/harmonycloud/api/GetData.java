package cn.harmonycloud.harmonycloud.api;

import cn.harmonycloud.harmonycloud.entry.ForecastNode;
import cn.harmonycloud.harmonycloud.entry.ForecastService;
import cn.harmonycloud.harmonycloud.entry.NowNode;
import cn.harmonycloud.harmonycloud.entry.NowService;

import java.util.ArrayList;

public class GetData {
    public static ArrayList<ForecastNode> getForecastNode() {
        ArrayList<ForecastNode> forecastNode = new ArrayList<>();

        return forecastNode;
    }

    public static ArrayList<ForecastService> getForecastService() {
        ArrayList<ForecastService> forecastService = new ArrayList<>();

        return forecastService;
    }

    public static ArrayList<NowNode> getNowNode() {
        ArrayList<NowNode> nowNode = new ArrayList<>();

        return nowNode;
    }

    public static ArrayList<NowService> getNowService() {
        ArrayList<NowService> nowServices = new ArrayList<>();

        return nowServices;
    }

}
