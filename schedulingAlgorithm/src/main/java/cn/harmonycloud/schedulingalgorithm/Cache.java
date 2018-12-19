package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import cn.harmonycloud.schedulingalgorithm.utils.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对service, node, pod的缓存
 */
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

    /**
     * 每轮调度开始时调用，获取数据，覆盖上一轮的旧数据
     */
    @SuppressWarnings("unchecked")
    public void fetchCacheData() {
        List<Service> serviceList = (List<Service>)(Object) fetchOne(Constants.URI_GET_SERVICE, Service.class);
        List<Pod> podList = (List<Pod>)(Object) fetchOne(Constants.URI_GET_POD, Pod.class);
        List<Node> nodeList = (List<Node>)(Object) fetchOne(Constants.URI_GET_NODE, Node.class);
        serviceMap = new HashMap<>();
        if (serviceList != null) {
            serviceList.forEach(s -> serviceMap.put(DOUtils.getServiceFullName(s), s));
        }
        podMap = new HashMap<>();
        if (podList != null) {
            podList.forEach(p -> podMap.put(DOUtils.getServiceFullName(p), p));
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
            String timeSeries = jsonObject.optString("timeSeries");
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

    /**
     * 同一轮调度内不再使用fetchCacheData()更新数据，updateCache()方法对缓存进行增量维护，更节省时间
     * @param pod 被调度的pod
     * @param host 为pod选择的节点
     */
    public void updateCache(Pod pod, String host) {
        // TODO 有新数据需要缓存时，需要一并更改这里
        boolean isAdd = pod.getOperation() == Constants.OPERATION_ADD;
        // 更新 pod, podMap
        if (isAdd) {
            pod.setNodeName(host);
            podMap.put(DOUtils.getPodFullName(pod), pod);
        } else {
            podMap.remove(DOUtils.getPodFullName(pod));
        }
        // 更新 service下pod列表、service占用的资源
        Service service = serviceMap.get(DOUtils.getServiceFullName(pod));
        List<String> podList = new ArrayList<>(Arrays.asList(service.getPodList()));
        if (isAdd) {
            podList.add(DOUtils.getPodFullName(pod));
        } else {
            podList.remove(DOUtils.getPodFullName(pod));
        }
        service.setPodList(podList.toArray(new String[0]));
        service.setCpuUsage(String.valueOf(Double.valueOf(service.getCpuUsage()) + (isAdd ? 1 : -1) * Double.valueOf(service.getCpuCosume())));
        service.setMemUsage(String.valueOf(Double.valueOf(service.getMemUsage()) + (isAdd ? 1 : -1) * Double.valueOf(service.getMemCosume())));
        // TODO update up down net io
        // 更新 node占用的资源 ps:似乎没有node总资源，TODO node下pod列表
        Node node = nodeMap.get(host);
        node.setCpuUsage(String.valueOf(Double.valueOf(node.getCpuUsage()) + (isAdd ? 1 : -1) * Double.valueOf(service.getCpuCosume())));
        node.setMemUsage(String.valueOf(Double.valueOf(node.getMemUsage()) + (isAdd ? 1 : -1) * Double.valueOf(service.getMemCosume())));
    }
}
