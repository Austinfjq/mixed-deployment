package cn.harmonycloud.DAO;

import cn.harmonycloud.beans.HttpClientResult;
import cn.harmonycloud.beans.NodeLoad;
import cn.harmonycloud.tools.HttpClientUtils;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyuzhong
 * @date 19-1-8 下午2:55
 * @Despriction
 */

public class NodeDAO {

    public NodeDAO() {
    }

    private static class HolderClass {
        private final static NodeDAO instance = new NodeDAO();
    }

    public static NodeDAO getInstance() {
        return HolderClass.instance;
    }


    public static String getNodesMonitorData(String startTime, String endTime) {
        Map<String,String> params = new HashMap<>();
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        String url = "http://localhost:8080/evaluatesystem/nodes";
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


    public static List<NodeLoad> getNodeLoadList(String startTime, String endTime) {
        String servicesStr = getNodesMonitorData(startTime,endTime);

        if (null == servicesStr) {
            System.out.println("get service load data failed!");
            return null;
        }

        List<NodeLoad> list = JSON.parseArray(servicesStr,NodeLoad.class);

        return list;
    }
}
