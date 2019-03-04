package cn.harmonycloud.DAO;

import cn.harmonycloud.beans.HttpClientResult;
import cn.harmonycloud.beans.ServiceLoad;
import cn.harmonycloud.tools.HttpClientUtils;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyuzhong
 * @date 19-1-9 下午4:50
 * @Despriction
 */
public class ServiceDAO {

    public static double getServiceNormalResponseTime(String namespace,String serviceName) {
        Map<String,String> params = new HashMap<>();
        params.put("namespace",namespace);
        params.put("serviceName",serviceName);
        String url = "http://localhost:8080/evaluatesystem/servicenormalresponsetime";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            System.out.println("get service normal response time failed!");
            return 0;
        }

        return Double.valueOf(httpClientResult.getContent());
    }


    public static double getServiceNormalErrorRate(String namespace,String serviceName) {
        Map<String,String> params = new HashMap<>();
        params.put("namespace",namespace);
        params.put("serviceName",serviceName);
        String url = "http://localhost:8080/evaluatesystem/servicenormalerrorrate";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            System.out.println("get service normal error rate failed!");
            return 0;
        }

        return Double.valueOf(httpClientResult.getContent());
    }

    public static int getPodNumberByServiceLoad(String namespace, String serviceName, int requestNumber) {
        Map<String,String> params = new HashMap<>();
        params.put("namespace",namespace);
        params.put("serviceName",serviceName);
        params.put("requestNumber",String.valueOf(requestNumber));
        String url = "http://localhost:8080/evaluatesystem/needPodNumber";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            System.out.println("get service normal error rate failed!");
            return 0;
        }

        return Integer.valueOf(httpClientResult.getContent());
    }

    public static String getServicesMonitorData(String startTime, String endTime) {
        Map<String,String> params = new HashMap<>();
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        String url = "http://localhost:8080/evaluatesystem/services";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            System.out.println("get index data failed!");
            return null;
        }

        return httpClientResult.getContent();
    }

    public static List<ServiceLoad> getServiceLoadList(String startTime, String endTime) {
        String servicesStr = getServicesMonitorData(startTime,endTime);

        if (null == servicesStr) {
            System.out.println("get service load data failed!");
            return null;
        }

        List<ServiceLoad> list = JSON.parseArray(servicesStr,ServiceLoad.class);

        for (ServiceLoad serviceLoad:list) {
            double normalTimeResponse = getServiceNormalResponseTime(serviceLoad.getNamespace(),serviceLoad.getServiceName());
            serviceLoad.setNormalTimeResponse(normalTimeResponse);

            double normalErrorRate = getServiceNormalErrorRate(serviceLoad.getNamespace(),serviceLoad.getServiceName());
            serviceLoad.setNormalErrorRate(normalErrorRate);

            System.out.println(serviceLoad.toString());

        }

        return list;
    }

    public static void main(String[] args) {
        System.out.println(String.valueOf(getServiceNormalResponseTime("online","wordpress")));
        System.out.println(String.valueOf(getServiceNormalErrorRate("offline","hadoop")));
        System.out.println(String.valueOf(getPodNumberByServiceLoad("online","wordpress",56)));

    }

}
