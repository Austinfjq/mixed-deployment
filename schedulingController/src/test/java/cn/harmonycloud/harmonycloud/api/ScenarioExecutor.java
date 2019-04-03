package cn.harmonycloud.harmonycloud.api;


import cn.harmonycloud.harmonycloud.entry.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static cn.harmonycloud.tools.Constant.*;


/**
 * @Author: changliu
 * @Date: 2019/1/3 16:35
 * @Description:
 */
public class ScenarioExecutor {

    public static ArrayList<Result> run() {
        ArrayList<ForecastNode> forecastNodeList = GetData.getForecastNode();
        ArrayList<ForecastService> forecastServiceList = GetData.getForecastService();
        ArrayList<NowNode> nowNodeList = GetData.getNowNode();
        ArrayList<NowService> nowServiceList = GetData.getNowService();
        ArrayList<Result> results = new ArrayList<>();

        Collections.sort(nowServiceList, new Comparator<NowService>() {
            @Override
            public int compare(NowService o1, NowService o2) {
//                return (String.valueOf(o1.getCpuUsage()).compareTo( String.valueOf(o2.getCpuUsage()) ));
                return (o1.getCpuUsage().compareTo(o2.getCpuUsage()));
            }
        });

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

        for (NowNode nowNode : nowNodeList) {

            //查找离线服务
            String offlineSvcNamespace = "";
            String offlineSvcName = "";
            for (NowService nowService : nowServiceList) {
                if (nowService.isOffline()) {
                    offlineSvcNamespace = nowService.getNamespace();
                    offlineSvcName = nowService.getServiceName();
                }
            }

            if (nowNode.getCpuUsage() > CpuUsageMaxThreshold) {

                for (ForecastNode forecastNode : forecastNodeList) {
                    if (forecastNode.getHostIp().equals(nowNode.getHostIp()) &&
                            nowNode.getCpuUsage() > forecastNode.getCpuUsage()) {
                        Result resultPod = new Result(1, offlineSvcNamespace,
                                offlineSvcName, "1");
                        results.add(resultPod);
                    }
                }


            } else if (nowNode.getCpuUsage() < CpuUsageMinThreshold) {
                Result resultPod = new Result(0, offlineSvcNamespace,
                        offlineSvcName,  "1");
                results.add(resultPod);
            }


        }


        return results;
    }

    public static String write2Es(ArrayList<Result> results) {
        Gson g = new GsonBuilder().serializeNulls().create();
        String returnValue = g.toJson(results);
//        HttpSend.sendPost("http://hostname:port/NodeSave", returnValue);
        return returnValue;
    }

    public static void main(String[] args) {
        System.out.println(write2Es(run()));
    }

}
