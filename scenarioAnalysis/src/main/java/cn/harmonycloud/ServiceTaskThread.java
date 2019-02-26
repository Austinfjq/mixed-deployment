package cn.harmonycloud;

import cn.harmonycloud.entry.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceTaskThread implements Runnable {
    private List<ForecastNode> forecastNodeList;
    private List<NowNode> nowNodeList;
    private List<ForecastService> forecastServiceList;
    private List<NowService> nowServiceList;
    private ArrayList<Result> results;

    public ServiceTaskThread(List<ForecastNode> forecastNodeList, List<NowNode> nowNodeList, List<ForecastService> forecastServiceList, List<NowService> nowServiceList, ArrayList<Result> results) {
        this.forecastNodeList = forecastNodeList;
        this.nowNodeList = nowNodeList;
        this.forecastServiceList = forecastServiceList;
        this.nowServiceList = nowServiceList;
        this.results = results;
    }


    @Override
    public void run() {

        for (NowService nowService : nowServiceList) {

            for (ForecastService forecastService : forecastServiceList) {

                if (nowService.getNamespace().equals(forecastService.getNamespace()) &&
                        nowService.getServiceName().equals(forecastService.getServiceName())) {

                    //service 资源过少
                    if (nowService.getPodNums() < forecastService.getPodNums()) {
                        Result resultPod = new Result(0, nowService.getNamespace(),
                                nowService.getServiceName(), String.valueOf(forecastService.getPodNums()));
                        results.add(resultPod);

                        //service 资源过多
                    } else if (nowService.getPodNums() > forecastService.getPodNums()) {
                        Result resultPod = new Result(1, nowService.getNamespace(),
                                nowService.getServiceName(), String.valueOf(forecastService.getPodNums()));
                        results.add(resultPod);

                    }

                }
            }
        }

    }
}
