package cn.harmonycloud.dao;

import cn.harmonycloud.beans.HttpClientResult;
import cn.harmonycloud.entry.ForecastNode;
import cn.harmonycloud.entry.NowNode;
import cn.harmonycloud.tools.Constant;

import com.alibaba.fastjson.JSON;

import java.util.List;

import static cn.harmonycloud.tools.HttpSend.sendGet;
import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;

public class NodeDAO {

    public NodeDAO() {
    }

    private static class HolderClass {
        private final static NodeDAO instance = new NodeDAO();
    }

    public static NodeDAO getInstance() {
        return HolderClass.instance;
    }

//    public static String getForecastNode(String startTime,String endTime) {
//        Map<String, String> params = new HashMap<>();
//        params.put("startTime", startTime);
//        params.put("endTime", endTime);
//        params.put("id", "node");
//        String url = "http://"+ Constant.HOST+":"+Constant.PORT+"/forecast/forecastValues";
//        HttpClientResult httpClientResult = null;
//        try {
//            httpClientResult = HttpClientUtils.doGet(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (null == httpClientResult || httpClientResult.getCode() != 200) {
//            System.out.println("get index data failed!");
//            return null;
//        }
//
//        return httpClientResult.getContent();
//    }

    public static String getForecastNode(String startTime, String endTime) {

        String params="id=node"+"&startTime="+startTime+"&endTime="+endTime;
        String url = "http://" + Constant.HOST + ":" + Constant.PORT + "/forecast/forecastValues";

        return sendGet(url,params);
    }

    public static List<ForecastNode> getForecastNodeList() {
        String servicesStr = getForecastNode("2019-03-04%2017:00:00","2019-03-04%2018:00:00");

        if (null == servicesStr) {
            System.out.println("get forecast node data failed!");
            return null;
        }
        List<ForecastNode> list = JSON.parseArray(servicesStr, ForecastNode.class);
        return list;
    }

    public static List<NowNode> getNowNodeList() {
        String servicesStr = sendGet("http://"+ Constant.HOST+":"+Constant.PORT+"/nowNode","");

        if (null == servicesStr) {
            System.out.println("get now node data failed!");
            return null;
        }
        List<NowNode> list = JSON.parseArray(servicesStr, NowNode.class);
        return list;
    }

    public static void main(String[] args) {

        String returnValue = JSON.toJSONString(getNowNodeList(), WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty);
        System.out.println(returnValue);
//        System.out.println(String.valueOf(getPodNumberByServiceLoad("online","wordpress",56)));

    }
}
