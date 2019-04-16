package cn.harmonycloud.dao.imp;

import cn.harmonycloud.beans.Node;
import cn.harmonycloud.dao.NodeDAO;
import cn.harmonycloud.tools.DataUtil;
import cn.harmonycloud.tools.HttpClientResult;
import cn.harmonycloud.tools.HttpClientUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @classname：NodeServiceImp
 * @author：WANGYUZHONG
 * @date：2019/4/10 18:00
 * @description:TODO
 * @version:1.0
 **/
@Service
public class NodeDaoImp implements NodeDAO {

    private final static Logger LOGGER = LoggerFactory.getLogger(NodeDaoImp.class);

    @Value("${DataCenterHostIP}")
    private String hostIp;

    @Value("${DataCenterPort}")
    private String port;

    @Override
    public List<String> getPodNameListOfHost(String clusterIp, String namespace, String serviceName, String hostName) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", clusterIp);
        params.put("namespace",namespace);
        params.put("serviceName",serviceName);
        params.put("hostName",hostName);
        String url = "http://"+ hostIp + ":" + port + "/node/service/pods";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get pod list data failed!");
            return null;
        }

        String podListStr = httpClientResult.getContent();
        LOGGER.debug("PodList is:" + podListStr);
        List<String> strings = new ArrayList<>();

        JSONArray jsonArray = JSONArray.parseArray(podListStr);

        for (int i=0; i<jsonArray.size(); i++) {
            String podName = jsonArray.getJSONObject(i).getString("podName");
            strings.add(podName);
        }

        return strings;
    }

    @Override
    public List<Node> getNodeList(String clusterIp) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", clusterIp);
        String url = "http://"+ hostIp + ":" + port + "/node/nodes";
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

        return DataUtil.jsonStringtoListObject(httpClientResult.getContent());
    }

    @Override
    public double getNodeCpuTotal(String clusterIp, String hostName) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", clusterIp);
        params.put("hostName", hostName);
        String url = "http://"+ hostIp + ":" + port + "/node/cpuTotal";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get node cpu total data failed!");
            return 0;
        }
        JSONObject jsonObject = DataUtil.jsonStringtoObject(httpClientResult.getContent());
        double cpuTotal = jsonObject.getIntValue("cpuTotal");
        return cpuTotal;
    }

    @Override
    public double getNodeMemTotal(String clusterIp, String hostName) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", clusterIp);
        params.put("hostName", hostName);
//        String url = "http://"+ hostIp + ":" + port + "/node/memTotal";
        String url = "http://10.10.101.115:8043/node/memTotal";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get node memory total data failed!");
            return 0;
        }
        JSONObject jsonObject = DataUtil.jsonStringtoObject(httpClientResult.getContent());
        double memTotal = jsonObject.getIntValue("memTotal");
        return memTotal;
    }

    @Override
    public double getLastPeriodMaxCpuUsage(String masterIp, String hostName, String startTime, String endTime) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", masterIp);
        params.put("hostName", hostName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
//        String url = "http://"+ hostIp + ":" + port + "/node/lastPeriodMaxCpuUsage";
        String url = "http://10.10.101.115:8043/node/lastPeriodMaxCpuUsage";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get node cpu usage max data failed!");
            return 0;
        }
        return Double.valueOf(httpClientResult.getContent());
    }

    @Override
    public double getLastPeriodMaxMemUsage(String masterIp, String hostName, String startTime, String endTime) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterIp", masterIp);
        params.put("hostName", hostName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
//        String url = "http://"+ hostIp + ":" + port + "/node/lastPeriodMaxMemUsage";
        String url = "http://10.10.101.115:8043/node/lastPeriodMaxMemUsage";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doGet(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("get node memory usage max data failed!");
            return 0;
        }
        return Double.valueOf(httpClientResult.getContent());
    }

    public static void main(String[] args) {
        String clusterIp = "10.10.102.25";
        String namespace = "kube-system";
        String serviceName = "";
        String hostName = "10.10.101.65-share";
        String startTime = "2019-04-15 17:34:56";
        String endTime = "2019-04-15 17:44:56";

        NodeDaoImp nodeDaoImp = new NodeDaoImp();


        //测试getNodeList接口
//        List<Node> nodes = nodeDaoImp.getNodeList(clusterIp);
//        for (Node node:nodes) {
//            System.out.println(node.toString());
//        }

        //测试getNodeMemTotal接口
//        double memTotal = nodeDaoImp.getNodeMemTotal(clusterIp,hostName);
//        System.out.println(memTotal);

        //测试getLastPeriodMaxCpuUsage接口
//        double cpuMax = nodeDaoImp.getLastPeriodMaxCpuUsage(clusterIp,hostName,startTime,endTime);
//        System.out.println(cpuMax);


        //测试getLastPeriodMaxMemUsage接口
//        double memMax = nodeDaoImp.getLastPeriodMaxMemUsage(clusterIp,hostName,startTime,endTime);
//        System.out.println(memMax);

    }
}
