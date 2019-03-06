package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.affinity.Affinity;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeAffinity;
import cn.harmonycloud.schedulingalgorithm.affinity.PodAffinity;
import cn.harmonycloud.schedulingalgorithm.affinity.PodAntiAffinity;
import cn.harmonycloud.schedulingalgorithm.affinity.Taint;
import cn.harmonycloud.schedulingalgorithm.affinity.Toleration;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Container;
import cn.harmonycloud.schedulingalgorithm.dataobject.ContainerPort;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.NodeCondition;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Resource;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import cn.harmonycloud.schedulingalgorithm.utils.HttpUtil;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对service, node, pod的缓存
 */
public class Cache {
    private final static Logger LOGGER = LoggerFactory.getLogger(Cache.class);

    private Map<String, Service> serviceMap;
    private Map<String, Pod> podMap;
    private Map<String, Node> nodeMap;
    private List<Node> nodeList;
    private Map<String, List<Pod>> nodeMapPodList;
    private Map<String, Resource> nodeForecastMap;

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

    public Map<String, List<Pod>> getNodeMapPodList() {
        return nodeMapPodList;
    }

    public Map<String, Resource> getNodeForecastMap() {
        return nodeForecastMap;
    }

    private static Gson gson = new Gson();

    /**
     * 每轮调度开始时调用，获取数据，覆盖上一轮的旧数据
     */
    @SuppressWarnings("unchecked")
    public void fetchCacheData() {
        LOGGER.info("start fetchCacheData!");
        List<Service> serviceList;
        List<Pod> podList;
        List<Node> nodeList;
        serviceList = (List<Service>) (Object) fetchOne(Constants.URI_GET_SERVICE, Service[].class);
        podList = (List<Pod>) (Object) fetchOne(Constants.URI_GET_POD, Pod[].class);
        nodeList = (List<Node>) (Object) fetchOne(Constants.URI_GET_NODE, Node[].class);

        serviceMap = new HashMap<>();
        if (serviceList != null) {
            serviceList.forEach(s -> serviceMap.put(DOUtils.getServiceFullName(s), s));
        }
        podMap = new HashMap<>();
        nodeMapPodList = new HashMap<>();
        if (podList != null) {
            podList.forEach(p -> {
                podMap.put(DOUtils.getPodFullName(p), p);
                if (nodeMapPodList.containsKey(p.getNodeName())) {
                    nodeMapPodList.get(p.getNodeName()).add(p);
                } else {
                    List<Pod> li = new ArrayList<>();
                    li.add(p);
                    nodeMapPodList.put(p.getNodeName(), li);
                }
                // 转换Affinity格式
                readAffinity(p);
                // 监控数据已提供
//                Service service = serviceMap.get(DOUtils.getServiceFullName(p));
//                if (service.getPodList() == null) {
//                    service.setPodList(new ArrayList<>());
//                }
//                service.getPodList().add(DOUtils.getPodFullName(p));
            });
        }
        nodeMap = new HashMap<>();
        nodeForecastMap = new HashMap<>();
        if (nodeList != null) {
            nodeList.forEach(n -> {
                nodeMap.put(n.getNodeName(), n);
                // 转换taints格式
                readTaints(n);
            });
            // comment out when debugging
            nodeForecastMap = fetchNodeForecast(nodeList);
        }
        this.nodeList = nodeList;
    }

    private void readAffinity(Pod pod) {
        if (pod.getAffinity() != null) {
            try {
                String nodeAffinityStr = DOUtils.k8sObjectToJson(pod.getAffinity().getNodeAffinity());
                String podAffinityStr = DOUtils.k8sObjectToJson(pod.getAffinity().getPodAffinity());
                String podAntiAffinityStr = DOUtils.k8sObjectToJson(pod.getAffinity().getPodAntiAffinity());
                NodeAffinity nodeAffinity = gson.fromJson(nodeAffinityStr, NodeAffinity.class);
                PodAffinity podAffinity = gson.fromJson(podAffinityStr, PodAffinity.class);
                PodAntiAffinity podAntiAffinity = gson.fromJson(podAntiAffinityStr, PodAntiAffinity.class);
                Affinity affinity = new Affinity();
                affinity.setNodeAffinity(nodeAffinity);
                affinity.setPodAffinity(podAffinity);
                affinity.setPodAntiAffinity(podAntiAffinity);
                pod.setAffinityObject(affinity);
            } catch (Exception e) {
                LOGGER.debug("readAffinity error");
                e.printStackTrace();
            }
        }
    }

    private void readTaints(Node node) {
        String taintsStr = node.getTaints();
        Taint[] taints = gson.fromJson(taintsStr, Taint[].class);
        node.setTaintsArray(taints);
    }

    public void getPortrait(List<Pod> pods) {
        LOGGER.info("start getPortrait!");
        try {
            List<String> serviceFullNames = pods.stream().map(DOUtils::getServiceFullName).distinct().collect(Collectors.toList());
            Map<String, Service> serviceMap = getServiceMap();
            // comment out the for loop below when debugging
            for (String serviceFullName : serviceFullNames) {
                // 获取预计占用资源信息
                Map<String, String> parameters = new HashMap<>();
                String[] split = serviceFullName.split(DOUtils.NAME_SPLIT);
                parameters.put("namespace", split[0]);
                parameters.put("serviceName", split[1]);
                String result = HttpUtil.post(Constants.URI_GET_POD_CONSUME, parameters);
                JSONObject jsonObject = JSONObject.fromObject(result);
                Service service = serviceMap.get(serviceFullName);
                service.setCpuCosume(jsonObject.optString("cpuCosume"));
                service.setMemCosume(jsonObject.optString("memCosume"));
                service.setDownNetIOCosume(jsonObject.optString("DownNetIOCosume"));
                service.setUPNetIOCosume(jsonObject.optString("UPNetIOCosume"));
                // 获取对应的service的资源密集类型
                result = HttpUtil.post(Constants.URI_GET_SERVICE_TYPE, parameters);
                jsonObject = JSONObject.fromObject(result);
                service.setIntensiveType(jsonObject.optInt("serviceType"));
            }

            //查找同service下的pod，填写新pod的属性
            pods.forEach(p -> {
                Service service = getServiceMap().get(DOUtils.getServiceFullName(p));
                Pod sp = getPodMap().get(DOUtils.getPodFullName(service.getPodList().get(0), p.getNamespace()));
                p.setCpuRequest(sp.getCpuRequest());
                p.setMemRequest(sp.getMemRequest());
                p.setNodeSelector(sp.getNodeSelector());
                p.setAffinity(sp.getAffinity());
                p.setContainers(sp.getContainers());
                p.setToleration(sp.getToleration());
                // setWantPorts
                String ports = sp.getContainers().getPorts();
                ContainerPort[] wantPorts = gson.fromJson(ports, ContainerPort[].class);
                p.setWantPorts(wantPorts);

                // uncomment below when debugging
//            Affinity affinity = new Affinity();
//            affinity.setPodAntiAffinity(sp.getAffinityObject() == null ? null : sp.getAffinityObject().getPodAntiAffinity());
//            affinity.setPodAffinity(sp.getAffinityObject() == null ? null : sp.getAffinityObject().getPodAffinity());
//            String ss = "NodeAffinity(preferredDuringSchedulingIgnoredDuringExecution=[PreferredSchedulingTerm(preference=NodeSelectorTerm(matchExpressions=[NodeSelectorRequirement(key=beta.kubernetes.io/arch, operator=In, values=[amd64], additionalProperties={})], additionalProperties={}), weight=2, additionalProperties={}), PreferredSchedulingTerm(preference=NodeSelectorTerm(matchExpressions=[NodeSelectorRequirement(key=beta.kubernetes.io/arch, operator=In, values=[ppc64le], additionalProperties={})], additionalProperties={}), weight=2, additionalProperties={}), PreferredSchedulingTerm(preference=NodeSelectorTerm(matchExpressions=[NodeSelectorRequirement(key=beta.kubernetes.io/arch, operator=In, values=[s390x], additionalProperties={})], additionalProperties={}), weight=2, additionalProperties={})], requiredDuringSchedulingIgnoredDuringExecution=NodeSelector(nodeSelectorTerms=[NodeSelectorTerm(matchExpressions=[NodeSelectorRequirement(key=beta.kubernetes.io/arch, operator=In, values=[amd64, ppc64le, s390x], additionalProperties={})], additionalProperties={})], additionalProperties={}), additionalProperties={})";
//            affinity.setNodeAffinity(gson.fromJson(DOUtils.k8sObjectToJson(ss), NodeAffinity.class));
//            p.setAffinityObject(affinity);
            });
        } catch (Exception e) {
            LOGGER.debug("getPortrait error");
            e.printStackTrace();
        }
    }

    private Map<String, Resource> fetchNodeForecast(List<Node> nodeList) {
        Map<String, Resource> map = new HashMap<>();
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = ft.format(calendar.getTime());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            String endTime = ft.format(calendar.getTime());

            Map<String, String> paras = new HashMap<>();
            paras.put("type", "1");
            paras.put("startTime", startTime);
            paras.put("endTime", endTime);
            for (Node node : nodeList) {
                Resource resource = new Resource();
                paras.put("id", node.getNodeName() + "&" + node.getNodeIP());

                paras.put("index", "cpu");
                String res = HttpUtil.get(Constants.URI_GET_NODE_FORECAST, paras);
                JSONArray jsonArray = JSONArray.fromObject(res);
                if (!jsonArray.isEmpty()) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String value = jsonObject.optString("value");
                        resource.setMilliCPU((long) (Double.valueOf(value) * 1000));
                    } catch (Exception e) {
                        // ignore
                    }
                }

                paras.put("index", "memory");
                res = HttpUtil.get(Constants.URI_GET_NODE_FORECAST, paras);
                jsonArray = JSONArray.fromObject(res);
                if (!jsonArray.isEmpty()) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String value = jsonObject.optString("value");
                        resource.setMemory(Double.valueOf(value).longValue());
                    } catch (Exception e) {
                        // ignore
                    }
                }
                map.put(node.getNodeName(), resource);
            }
        } catch (Exception e) {
            LOGGER.debug("fetchNodeForecast error");
            e.printStackTrace();
        }
        return map;
    }

    private <T> List<Object> fetchOne(String uri, Class<T> clazz) {
        List<Object> result = new ArrayList<>();
        String jsonStr = null;
        try {
            jsonStr = HttpUtil.get(uri);
        } catch (Exception e) {
            LOGGER.debug("Http request fail:\"" + uri + "\"");
            e.printStackTrace();
        }
        try {
            Object[] objects = (Object[]) gson.fromJson(jsonStr, clazz);
            Collections.addAll(result, objects);
        } catch (Exception e) {
            LOGGER.debug("Data format error:");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 同一轮调度内不再使用fetchCacheData()更新数据，updateCache()方法对缓存进行增量维护，更节省时间
     *
     * @param pod  被调度的pod
     * @param host 为pod选择的节点
     */
    public void updateCache(Pod pod, String host) {
        // 有新数据需要缓存时，需要一并更改这里
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
        List<String> podList = service.getPodList();
        if (isAdd) {
            podList.add(DOUtils.getPodFullName(pod));
        } else {
            podList.remove(DOUtils.getPodFullName(pod));
        }
        service.setPodList(podList);
//        service.setCpuUsage(String.valueOf(Double.valueOf(service.getCpuUsage()) + (isAdd ? 1 : -1) * Double.valueOf(service.getCpuCosume())));
//        service.setMemUsage(String.valueOf(Double.valueOf(service.getMemUsage()) + (isAdd ? 1 : -1) * Double.valueOf(service.getMemCosume())));
        // 更新 node占用的资源
        Node node = nodeMap.get(host);
        node.setCpuUsage(node.getCpuUsage() + (isAdd ? 1 : -1) * pod.getCpuRequest());
        node.setMemUsage(node.getMemUsage() + (isAdd ? 1 : -1) * pod.getMemRequest());
        // 更新node下pod列表，nodeMapPodList
        if (isAdd) {
            nodeMapPodList.get(host).add(pod);
        } else {
            List<Pod> li = nodeMapPodList.get(host);
            for (int i = 0; i < li.size(); i++) {
                if (DOUtils.getServiceFullName(pod).equals(DOUtils.getServiceFullName(li.get(i)))) {
                    li.remove(i);
                    break;
                }
            }
        }
    }
}
