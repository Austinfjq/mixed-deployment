package cn.harmonycloud;


import cn.harmonycloud.beans.LoadConfigFile;
import cn.harmonycloud.dao.NodeDAO;
import cn.harmonycloud.dao.ServiceDAO;
import cn.harmonycloud.entry.*;
import cn.harmonycloud.tools.Constant;
import cn.harmonycloud.tools.HttpSend;
import cn.harmonycloud.tools.Write2ES;
import com.alibaba.fastjson.JSON;

import java.util.*;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;


public class ScenarioExecutor {
    public static ArrayList<Result> results = new ArrayList<>();

    public static ArrayList<Result> getResults() {

        List<ForecastService> forecastServiceList = ServiceDAO.getForecastServiceList();
        List<NowService> nowServiceList = ServiceDAO.getNowServiceList();

        List<ForecastNode> forecastNodeList = NodeDAO.getForecastNodeList();
        List<NowNode> nowNodeList = NodeDAO.getNowNodeList();

        ArrayList<Config> configList = LoadConfigFile.run();

        Collections.sort(nowServiceList, new Comparator<NowService>() {
            @Override
            public int compare(NowService o1, NowService o2) {
//                return (String.valueOf(o1.getCpuUsage()).compareTo( String.valueOf(o2.getCpuUsage()) ));
                return (o1.getCpuUsage().compareTo(o2.getCpuUsage()));
            }
        });

        //执行node分析的线程

        NodeTaskThread nodeTaskThread = new NodeTaskThread(forecastNodeList, nowNodeList,
                forecastServiceList, nowServiceList, results);

        Thread nodeThread = new Thread(nodeTaskThread);

        nodeThread.start();


        //执行service分析的线程

        ServiceTaskThread serviceTaskThread = new ServiceTaskThread(forecastNodeList, nowNodeList,
                forecastServiceList, nowServiceList, results);

        Thread serviceThread = new Thread(serviceTaskThread);

        serviceThread.start();

        return results;
    }

//    public static String write2Es(ArrayList<Result> results) {
//        Gson g = new GsonBuilder().serializeNulls().create();
//        String returnValue = g.toJson(results);
////        HttpSend.sendPost("http://hostname:port/NodeSave", returnValue);
//        return returnValue;
//    }

    public static void run() {
        Write2ES.run(getResults(), "schedulePods");
        String returnValue = JSON.toJSONString(getResults(), WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty);
        HttpSend.sendPost("POST", "http://" + Constant.URL_HOST + ":" + Constant.URL_PORT + "/" + "schedulepod", returnValue);
    }

    public static void main(String[] args) {
        String returnValue = JSON.toJSONString(getResults(), WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty);
        System.out.println(returnValue);
//        System.out.println(Write2ES.run(getResults(), "schedulePods"));
    }

}
