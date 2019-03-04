package cn.harmonycloud.dao;

import cn.harmonycloud.beans.HttpClientResult;
import cn.harmonycloud.entry.ForecastNode;
import cn.harmonycloud.entry.NowNode;
import cn.harmonycloud.tools.HttpClientUtils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeDAO {


    public NodeDAO() {
    }

    private static class HolderClass {
        private final static NodeDAO instance = new NodeDAO();
    }

    public static NodeDAO getInstance() {
        return HolderClass.instance;
    }

    public static String getData(String url) {
//        Map<String, String> params = new HashMap<>();
//        params.put("startTime", startTime);
//        params.put("endTime", endTime);
//        String url = "http://localhost:8080/evaluatesystem/nodes";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = HttpClientUtils.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            System.out.println("get index data failed!");
            return null;
        }

        return httpClientResult.getContent();
    }


    public static List<ForecastNode> getForecastNodeList() {
        String servicesStr = getData("http://localhost:8080/evaluatesystem/nodes");

        if (null == servicesStr) {
            System.out.println("get forecast node data failed!");
            return null;
        }
        List<ForecastNode> list = JSON.parseArray(servicesStr, ForecastNode.class);
        return list;
    }

    public static List<NowNode> getNowNodeList() {
        String servicesStr = getData("http://localhost:8080/evaluatesystem/nodes");

        if (null == servicesStr) {
            System.out.println("get now node data failed!");
            return null;
        }
        List<NowNode> list = JSON.parseArray(servicesStr, NowNode.class);
        return list;
    }

    public static void main(String[] args) {

//        System.out.println(String.valueOf(getPodNumberByServiceLoad("online","wordpress",56)));

    }
}
