package cn.harmonycloud.service.imp;

import cn.harmonycloud.beans.ForecastCell;
import cn.harmonycloud.beans.Node;
import cn.harmonycloud.beans.Service;
import cn.harmonycloud.service.IData;
import cn.harmonycloud.tools.HttpClientResult;
import cn.harmonycloud.tools.HttpClientUtils;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @classname：DataImp
 * @author：WANGYUZHONG
 * @date：2019/4/16 15:30
 * @description:TODO
 * @version:1.0
 **/
public class DataImp implements IData {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataImp.class);

    @Override
    public List<Node> getNodeList(String clusterIp) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", clusterIp);
//        String url = "http://"+ PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/node/nodes";
        String url = "http://10.10.101.115:8043/node/nodes";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get node list data failed!");
            return null;
        }

        String podListStr = httpClientResult.getContent();
        List<Node> nodes = new ArrayList<>();

        JSONArray jsonArray = JSONArray.parseArray(podListStr);

        for (int i=0; i<jsonArray.size(); i++) {
            String masterIP = jsonArray.getJSONObject(i).getString("clusterIp");
            String hostName = jsonArray.getJSONObject(i).getString("hostName");

            Node node = new Node();
            node.setMasterIp(masterIP);
            node.setHostName(hostName);
            nodes.add(node);
        }

        return nodes;
    }

    @Override
    public List<Service> getAllOnlineService(String masterIp) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", masterIp);
//        String url = "http://"+ PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/service/onlineServices";
        String url = "http://10.10.101.115:8043/service/onlineServices";
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
    public boolean saveForecastData(String forecastResult) {
        Map<String,String> data = new HashMap<>();
        data.put("data",forecastResult);
        //        String url = "http://"+ PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/forecast/save";
        String url = "http://10.10.101.115:8043/forecast/save";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doPost(url, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("sava forecast result data failed!");
            return false;
        }

        return Boolean.valueOf(httpClientResult.getContent());
    }

    @Override
    public String getIndexHistoryData(String ID, String type, String index, String startTime, String endTime) {
        Map<String,String> params = new HashMap<>();
        params.put("ID",ID);
        params.put("type",type);
        params.put("index",index);
        params.put("startTime",startTime);
        params.put("endTime", endTime);
//        String url = "http://"+ PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/forecast/indexData";
        String url = "http://10.10.101.115:8043/forecast/indexData";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get index data failed!");
            return null;
        }

        return httpClientResult.getContent();
    }

    @Override
    public ForecastCell getForecastCell(String ID, String type, String index) {
        return null;
    }

    @Override
    public boolean savaForecastModel(String ID, String type, String index, String forecastingModel, String modelParams) {
        return false;
    }

    @Override
    public boolean updateIndexLastForecastTime(String ID, String type, String index, String lastForecastTime) {
        return false;
    }
}
