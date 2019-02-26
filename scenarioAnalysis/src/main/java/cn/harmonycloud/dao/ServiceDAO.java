package cn.harmonycloud.dao;

import cn.harmonycloud.beans.HttpClientResult;
import cn.harmonycloud.entry.ForecastNode;
import cn.harmonycloud.entry.ForecastService;
import cn.harmonycloud.entry.NowService;
import cn.harmonycloud.tools.HttpClientUtils;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceDAO {

    public ServiceDAO() {
    }

    private static class HolderClass {
        private final static ServiceDAO instance = new ServiceDAO();
    }

    public static ServiceDAO getInstance() {
        return ServiceDAO.HolderClass.instance;
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

    public static List<NowService> getNowServiceList() {
        String servicesStr = getData("http://localhost:8080/evaluatesystem/nodes");

        if (null == servicesStr) {
            System.out.println("get now service data failed!");
            return null;
        }

        List<NowService> list = JSON.parseArray(servicesStr, NowService.class);

        return list;
    }


    public static List<ForecastService> getForecastServiceList() {
        String servicesStr = getData("http://localhost:8080/evaluatesystem/nodes");

        if (null == servicesStr) {
            System.out.println("get forecast service data failed!");
            return null;
        }

        List<ForecastService> list = JSON.parseArray(servicesStr, ForecastService.class);

        return list;
    }


    public static void main(String[] args) {

//        System.out.println(String.valueOf(getPodNumberByServiceLoad("online","wordpress",56)));

    }
}
