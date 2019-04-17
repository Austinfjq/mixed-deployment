package cn.harmonycloud.dao.imp;

import cn.harmonycloud.beans.Service;
import cn.harmonycloud.dao.ServiceDAO;
import cn.harmonycloud.tools.DataUtil;
import cn.harmonycloud.tools.HttpClientResult;
import cn.harmonycloud.tools.HttpClientUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @classname：ServiceDaoImp
 * @author：WANGYUZHONG
 * @date：2019/4/10 15:41
 * @description:TODO
 * @version:1.0
 **/
@org.springframework.stereotype.Service
public class ServiceDaoImp implements ServiceDAO {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceDaoImp.class);

    @Value("${DataCenterHostIP}")
    private String hostIp;

    @Value("${DataCenterPort}")
    private String port;

    @Override
    public boolean addService(Service service) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", service.getMasterIp());
        params.put("namespace",service.getNamespace());
        params.put("serviceName",service.getServiceName());
        params.put("serviceType",String.valueOf(service.getServiceType()));
        String url = "http://"+ hostIp + ":" + port + "/service/service";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doPost(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("add service failed!");
            return false;
        }

        JSONObject jsonObject = DataUtil.jsonStringtoObject(httpClientResult.getContent());
        Boolean isSucceed = jsonObject.getBoolean("isSucceed");
        return isSucceed;
    }

    @Override
    public List<Service> getAllOnlineService(String masterIp) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", masterIp);
        String url = "http://"+ hostIp + ":" + port + "/service/onlineServices";
//        String url = "http://10.107.249.160:8080/service/onlineServices";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get onlineServices data failed!");
            return null;
        }

        String serviceListStr = httpClientResult.getContent();
        List<Service> services = new ArrayList<>();

        JSONArray jsonArray = JSONArray.parseArray(serviceListStr);

        for (int i=0; i<jsonArray.size(); i++) {
            String clusterIP = jsonArray.getJSONObject(i).getString("masterIp");
            String namespace = jsonArray.getJSONObject(i).getString("namespace");
            String serviceName = jsonArray.getJSONObject(i).getString("serviceName");
            int serviceType = jsonArray.getJSONObject(i).getIntValue("serviceType");
            Service service = new Service();
            service.setMasterIp(clusterIP);
            service.setNamespace(namespace);
            service.setServiceName(serviceName);
            service.setServiceType(serviceType);
            services.add(service);
        }

        return services;
    }

    @Override
    public int getServicePodNums(String masterIp, String namespace, String serviceName) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", masterIp);
        params.put("namespace", namespace);
        params.put("serviceName", serviceName);
        String url = "http://"+ hostIp + ":" + port + "/service/podNums";

        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get service now pod numbers data failed!");
            return 0;
        }

        JSONObject jsonObject = DataUtil.jsonStringtoObject(httpClientResult.getContent());
        int podNums = jsonObject.getIntValue("podNums");
        return podNums;
    }

    @Override
    public int getServiceRequestPodNums(String masterIp, String namespace, String serviceName, double requestNums) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", masterIp);
        params.put("namespace", namespace);
        params.put("serviceName", serviceName);
        params.put("requestNums", String.valueOf(requestNums));
        String url = "http://"+ hostIp + ":" + port + "/service/requestPodNums";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get service request pod numbers data failed!");
            return 0;
        }
        JSONObject jsonObject = DataUtil.jsonStringtoObject(httpClientResult.getContent());
        int podNums = jsonObject.getIntValue("podNums");
        return podNums;
    }

    @Override
    public double getLastPeriodMaxRequestNums(String masterIp, String namespace, String serviceName, String startTime, String endTime) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", masterIp);
        params.put("namespace", namespace);
        params.put("serviceName", serviceName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        String url = "http://"+ hostIp + ":" + port + "/service/lastPeriodMaxRequestNums";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get service last period request data failed!");
            return 0;
        }
        JSONObject jsonObject = DataUtil.jsonStringtoObject(httpClientResult.getContent());
        double lastPeriodMaxRequestNums = jsonObject.getIntValue("lastPeriodMaxRequestNums");
        return lastPeriodMaxRequestNums;
    }

    @Override
    public double getNextPeriodMaxRequestNums(String masterIp, String namespace, String serviceName, String startTime, String endTime) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", masterIp);
        params.put("namespace", namespace);
        params.put("serviceName", serviceName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        String url = "http://"+ hostIp + ":" + port + "/service/nextPeriodMaxRequestNums";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get service next period request data failed!");
            return 0;
        }
        JSONObject jsonObject = DataUtil.jsonStringtoObject(httpClientResult.getContent());
        double nextPeriodMaxRequestNums = jsonObject.getIntValue("lastPeriodMaxRequestNums");
        return nextPeriodMaxRequestNums;
    }

    public static void main(String[] args) {
        ServiceDaoImp serviceDaoImp = new ServiceDaoImp();

        List<Service> services = serviceDaoImp.getAllOnlineService("10.10.102.25");


    }

}
