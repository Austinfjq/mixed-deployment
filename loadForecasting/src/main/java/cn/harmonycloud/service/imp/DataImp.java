package cn.harmonycloud.service.imp;

import cn.harmonycloud.beans.ForecastCell;
import cn.harmonycloud.beans.Node;
import cn.harmonycloud.beans.Service;
import cn.harmonycloud.service.IData;
import cn.harmonycloud.tools.HttpClientResult;
import cn.harmonycloud.tools.HttpClientUtils;
import cn.harmonycloud.tools.PropertyFileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
        String url = "http://"+ PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/node/nodes";
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
        if (null == podListStr || podListStr.equals("")) {
            LOGGER.error("this cluster not have any node!");
            return nodes;
        }
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
        String url = "http://"+ PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/service/onlineServices";
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

        if (null == serviceListStr || serviceListStr.equals("")) {
            LOGGER.error("this cluster not have any online service!");
            return services;
        }
        JSONArray jsonArray = JSONArray.parseArray(serviceListStr);

        for (int i=0; i<jsonArray.size(); i++) {
            String clusterIP = jsonArray.getJSONObject(i).getString("clusterIp");
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
        String url = "http://"+ PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/forecastResultCells";
        String result = null;
        try {
            result = HttpClientUtils.sendPut(url, forecastResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == result || !(result.equals("true"))) {
            LOGGER.error("sava forecast result data failed!");
            return false;
        }
        return true;
    }

    @Override
    public String getIndexHistoryData(String ID, int type, String index, String startTime, String endTime) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",ID);
        jsonObject.put("type",type);
        jsonObject.put("index",index);
        jsonObject.put("startTime",startTime);
        jsonObject.put("endTime",endTime);


        String params = jsonObject.toJSONString();

        String url = "http://"+ PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/indexData";

        String result = null;
        try {
            result = HttpClientUtils.sendPost(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == result || result.equals("")) {
            LOGGER.error("get index data failed!");
            return null;
        }

        return result;
    }

    @Override
    public ForecastCell getForecastCell(String ID, int type, String forecastingIndex) {
        Map<String,String> params = new HashMap<>();
        params.put("ID",ID);
        params.put("type",String.valueOf(type));
        params.put("indexName",forecastingIndex);

        String url = "http://"+ PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/forecast/forecastCell";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get forecastCell data failed!");
            return null;
        }

        if (httpClientResult.getContent().equals("")) {
            LOGGER.error("forecastCell is null!");
            return null;
        }

        ForecastCell forecastCell = new ForecastCell();
        JSONObject jsonObject = JSONObject.parseObject(httpClientResult.getContent());

        forecastCell.setID(jsonObject.getString("cellId"));
        forecastCell.setType(jsonObject.getIntValue("type"));
        forecastCell.setForecastingIndex(jsonObject.getString("forecastingIndex"));
        forecastCell.setTimeInterval(jsonObject.getIntValue("timeInterval"));
        forecastCell.setNumberOfPerPeriod(jsonObject.getIntValue("numberOfPerPeriod"));
        forecastCell.setForecastingModel(jsonObject.getString("forecastingModel"));
        forecastCell.setModelParams(jsonObject.getString("modelParams"));
        forecastCell.setForcastingEndTime(jsonObject.getString("forcastingEndTime"));

        return forecastCell;
    }

    @Override
    public boolean savaForecastModel(String ID, int type, String forecastingIndex, String forecastingModel, String modelParams) {
        Map<String,String> params = new HashMap<>();
        params.put("ID",ID);
        params.put("type",String.valueOf(type));
        params.put("indexName",forecastingIndex);
        params.put("forecastingModel",forecastingModel);
        params.put("modelParams",modelParams);

        String url = "http://"+ PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/forecast/forecastCellModel";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doPut(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("save forecastCell model data failed!");
            return false;
        }

        return Boolean.valueOf(httpClientResult.getContent());
    }

    @Override
    public boolean updateIndexLastForecastTime(String ID, int type, String forecastingIndex, String lastForecastTime) {
        Map<String,String> params = new HashMap<>();
        params.put("ID",ID);
        params.put("type",String.valueOf(type));
        params.put("indexName",forecastingIndex);
        params.put("lastForecastTime",lastForecastTime);

        String url = "http://"+ PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/forecast/forecastCellEndTime";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doPut(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("save forecastCell forecast endtime data failed!");
            return false;
        }

        return Boolean.valueOf(httpClientResult.getContent());
    }

    public static void main(String[] args) {
        String id = "md-system&data-center";
        int type = 0;
        String index = "memUsage";
        String startTime = "2019-04-23 00:00:00";
        String endTime = "2019-4-24 00:00:00";
        String model = "HoltWinters";
        String modelParams = "[{'alpha',0.1,'deta',0.2,'gama',0.5}]";





        DataImp dataImp = new DataImp();
//        dataImp.savaForecastModel(id,type,index,model,modelParams);

        dataImp.updateIndexLastForecastTime(id,type,index,endTime);

//        System.out.println(dataImp.getIndexHistoryData(id,type,index,startTime,endTime));
    }
}
