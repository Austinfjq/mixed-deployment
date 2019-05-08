package cn.harmonycloud.DAO.imp;

import cn.harmonycloud.DAO.NodeDAO;
import cn.harmonycloud.beans.Node;
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
 * @author wangyuzhong
 * @date 19-1-8 下午2:55
 * @Despriction
 */

public class NodeDaoImp implements NodeDAO {
    private final static Logger LOGGER = LoggerFactory.getLogger(NodeDaoImp.class);

    @Override
    public List<Node> getNodeList(String clusterIp) {
        Map<String,String> params = new HashMap<>();
        params.put("clusterMasterIP", clusterIp);
        String url = "http://"+ PropertyFileUtil.getValue("hostIp") + ":" + PropertyFileUtil.getValue("port") + "/node/nodes";
        //ip和port在配置文件中修改



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
            String clusterIP = jsonArray.getJSONObject(i).getString("clusterIp");
            String hostName = jsonArray.getJSONObject(i).getString("hostName");

            Node node = new Node();
            node.setMasterIp(clusterIP);
            node.setHostName(hostName);
            nodes.add(node);
        }

        return nodes;
    }

    @Override
    public double getNodeIndexValue(String queryStr) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("queryString", queryStr);
//        String url = "http://"+ PropertyFileUtil.getValue("hostIp") + ":" + PropertyFileUtil.getValue("port") + "/queryData";
        String url = "http://10.10.102.25:31244/queryData";
        String result = null;
        try {
            result =  HttpClientUtils.sendPost(url,jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == result || result.equals("")) {
            LOGGER.error("get node index value failed!");
            return -1;
        }
        LOGGER.debug(result);

        JSONObject resultJsonObject = JSONObject.parseObject(result);

        if (!"success".equals(resultJsonObject.getString("status"))) {
            LOGGER.error("query node index value failed!");
        }

        System.out.println(result);

        JSONObject dataJsonObject = resultJsonObject.getJSONObject("data");
        JSONArray rsJsonObject = dataJsonObject.getJSONArray("result");

        String value = null;
        try {
            value = rsJsonObject.getJSONObject(0).getJSONArray("value").getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Double.valueOf(value);
    }
    public static void main(String[] args) {
        NodeDaoImp test1=new NodeDaoImp();
        List<Node> nodes = new ArrayList<>();

        nodes=test1.getNodeList("10.10.102.25");
        for (int i=0;i<nodes.size();i++){
            System.out.println(nodes.get(i).toString());
        }


    }
}
