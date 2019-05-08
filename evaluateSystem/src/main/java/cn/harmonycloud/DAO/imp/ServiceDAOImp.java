package cn.harmonycloud.DAO.imp;

import cn.harmonycloud.DAO.ServiceDAO;
import cn.harmonycloud.beans.Service;
import cn.harmonycloud.tools.DataUtil;
import cn.harmonycloud.tools.HttpClientResult;
import cn.harmonycloud.tools.HttpClientUtils;
import cn.harmonycloud.tools.PropertyFileUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyuzhong
 * @date 19-1-9 下午4:50
 * @Despriction
 */
public class ServiceDAOImp implements ServiceDAO {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceDAOImp.class);

    @Override
    public List<Service> getAllOnlineService(String masterIp) {
        Map<String, String> params = new HashMap<>();
        params.put("clusterIp", masterIp);
        String url = "http://" + PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/service/onlineServices";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = HttpClientUtils.doGet(url, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get onlineServices data failed!");
            return null;
        }
        return DataUtil.jsonStringtoListObject(httpClientResult.getContent());
    }//接口正确

    @Override
    public double getServiceIndexValue(String queryStr) {
        Map<String, String> params = new HashMap<>();
        params.put("queryString", queryStr);
        String url = "http://" + PropertyFileUtil.getValue("DataProcessHostIP") + ":" + PropertyFileUtil.getValue("DataProcessPort") + "/queryData";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = HttpClientUtils.doPost(url, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {

            LOGGER.error("get service index data failed!");
            return 0;
        }

        JSONObject jsonObject = JSONObject.parseObject(httpClientResult.getContent());

        if (!"success".equals(jsonObject.getString("status"))) {
            LOGGER.error("query service index value failed!");
        }

        return Double.valueOf(jsonObject.getJSONObject("data").getJSONObject("result").getString(""));

    }//postman有数据，dopost拿不到

    @Override
    public double getServiceNormalResponseTime(String masterIp, String namespace, String serviceName) {
        Map<String, String> params = new HashMap<>();
        params.put("masterIp", masterIp);
        params.put("namespace", namespace);
        params.put("serviceName", serviceName);
        String url = "http://" + PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/evaluatesystem/servicenormalresponsetime";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = HttpClientUtils.doGet(url, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            System.out.println("get service normal response time failed!");
            return 0;
        }

        return Double.valueOf(httpClientResult.getContent());
    }//postman没有数据

    @Override
    public double getServiceNormalErrorRate(String masterIp, String namespace, String serviceName) {
        Map<String, String> params = new HashMap<>();
        params.put("masterIp", masterIp);
        params.put("namespace", namespace);
        params.put("serviceName", serviceName);
        String url = "http://" + PropertyFileUtil.getValue("DataCenterHostIP") + ":" + PropertyFileUtil.getValue("DataCenterPort") + "/evaluatesystem/servicenormalerrorrate";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = HttpClientUtils.doGet(url, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            System.out.println("get service normal error rate failed!");
            return 0;
        }

        return Double.valueOf(httpClientResult.getContent());
    }//postman没有数据


    public static void main(String[] args) {
        ServiceDAOImp test1 = new ServiceDAOImp();
        System.out.println(test1.getAllOnlineService("10.10.102.25"));
        String str="sum(rate(node_cpu_seconds_total[5m]))by(kubernetes_pod_host_ip,kubernetes_pod_node_name)";
        System.out.println(test1.getServiceIndexValue(str));
    }
}