package cn.harmonycloud.schedulingalgorithm.algorithm;

import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.utils.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache {
    private Map<String, Service> serviceMap;
    private Map<String, Pod> podMap;
    private Map<String, Node> nodeMap;
    private List<Node> nodeList;

    public Map<String, Service> getServiceMap() {
        return serviceMap;
    }

    public Map<String, Pod> getPodMap() {
        return podMap;
    }

    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void updateCache() {
        // TODO: 更新serviceMap podMap nodeMap
        // 更新nodeList
        nodeList = new ArrayList<>(nodeMap.values());
    }

    @SuppressWarnings("unchecked")
    public void fetchCacheData() {
        List<Service> serviceList = (List<Service>)(Object) fetchOne(Constants.URI_GET_SERVICE, Service.class);
        List<Pod> podList = (List<Pod>)(Object) fetchOne(Constants.URI_GET_POD, Pod.class);
        List<Node> nodeList = (List<Node>)(Object) fetchOne(Constants.URI_GET_NODE, Node.class);
        serviceMap = new HashMap<>();
        if (serviceList != null) {
            serviceList.forEach(s -> serviceMap.put(s.getNamespace()+"-"+s.getServiceName(), s));
        }
        podMap = new HashMap<>();
        if (podList != null) {
            podList.forEach(p -> podMap.put(p.getNamespace()+"-"+p.getServiceName(), p));
        }
        nodeMap = new HashMap<>();
        if (nodeList != null) {
            nodeList.forEach(n -> nodeMap.put(n.getNodeName(), n));
        }
        this.nodeList = nodeList;
    }

    private List<Object> fetchOne(String uri, Class clazz) {
        List<Object> result = new ArrayList<>();
        // TODO 从HttpEntity InputStream中读json会不会更快？
        String jsonStr = HttpUtil.post(uri);
        try {
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            String timeSeries = jsonObject.optString("timeSeries", null);
            if (timeSeries == null) {
                return null;
            }
            JSONArray jsonArray = JSONArray.fromObject(timeSeries);
            if (jsonArray == null) {
                return null;
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                try {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Object object = JSONObject.toBean(jsonObject1, clazz);
                    result.add(object);
                } catch (Exception e) {
                    // TODO ... but we can continue here
                }
            }
            return result;
        } catch (Exception e) {
            return result;
        }
    }
}
