package cn.harmonycloud.dao;

import cn.harmonycloud.beans.HttpClientResult;
import cn.harmonycloud.entry.ForecastService;
import cn.harmonycloud.entry.NowService;
import cn.harmonycloud.tools.Constant;
import com.alibaba.fastjson.JSON;

import java.util.List;

import static cn.harmonycloud.tools.HttpSend.sendGet;
import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;

public class ServiceDAO {

    public ServiceDAO() {
    }

    private static class HolderClass {
        private final static ServiceDAO instance = new ServiceDAO();
    }

    public static ServiceDAO getInstance() {
        return ServiceDAO.HolderClass.instance;
    }


    public static String getForecastService(String startTime, String endTime) {

        String params="id=service"+"&startTime="+startTime+"&endTime="+endTime;
        String url = "http://" + Constant.HOST + ":" + Constant.PORT + "/forecast/forecastValues";

        return sendGet(url,params);
    }

    public static List<NowService> getNowServiceList() {
        String servicesStr = sendGet("http://" + Constant.HOST + ":" + Constant.PORT + "/nowService", "");

        if (null == servicesStr) {
            System.out.println("get now service data failed!");
            return null;
        }

        List<NowService> list = JSON.parseArray(servicesStr, NowService.class);

        return list;
    }


    public static List<ForecastService> getForecastServiceList() {
        String servicesStr = getForecastService("2019-03-04%2017:00:00", "2019-03-04%2018:00:00");

        if (null == servicesStr) {
            System.out.println("get forecast service data failed!");
            return null;
        }

        List<ForecastService> list = JSON.parseArray(servicesStr, ForecastService.class);

        return list;
    }


    public static void main(String[] args) {

        String returnValue = JSON.toJSONString(getNowServiceList(), WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty);
        System.out.println(returnValue);

//        System.out.println(String.valueOf(getPodNumberByServiceLoad("online","wordpress",56)));

    }
}
