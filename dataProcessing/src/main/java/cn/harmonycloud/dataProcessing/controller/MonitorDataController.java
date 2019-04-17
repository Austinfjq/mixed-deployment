package cn.harmonycloud.dataProcessing.controller;

import cn.harmonycloud.dataProcessing.metric.Metric;
import cn.harmonycloud.dataProcessing.model.MonitorNode;
import cn.harmonycloud.dataProcessing.model.MonitorService;
import cn.harmonycloud.dataProcessing.tools.K8sClient;
import cn.harmonycloud.dataProcessing.tools.ReadUrl;
import cn.harmonycloud.dataProcessing.tools.SetValue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.harmonycloud.dataProcessing.metric.Constant.*;


@RestController
public class MonitorDataController {

    private static void nodeInitFromApi(ArrayList<MonitorNode> n) {
        KubernetesClient client = K8sClient.getClient();
        NodeList nodeList = client.nodes().list();
        for (Node d : nodeList.getItems()) {
            MonitorNode node = new MonitorNode();
            node.setNodeIP(d.getStatus().getAddresses().get(0).getAddress());
            n.add(node);
        }
    }

    @GetMapping(value = "monitorNode")
    public List<MonitorNode> getMonitorNode() {
        ArrayList<MonitorNode> monitorNodes = new ArrayList<>();
        nodeInitFromApi(monitorNodes);

        Metric configFile = new Metric();

        configFile.init(PROMETHEUS_NODE_CONFIG_PATH);

        String hostIp = configFile.getHostIp();
        String port = configFile.getPort();

        String urlHost = "http://" + hostIp + ":" + port + "/api/v1/query?query=";
        JsonParser parse = new JsonParser();

        ArrayList<List<String>> nodeQueryList = configFile.getQueryList();
        ArrayList<List<String>> nodeQueryKey = configFile.getQueryKey();
        ArrayList<List<String>> nodeDataType = configFile.getDataType();

        for (int i = 0; i < configFile.getQueryNum(); i++) {

            ArrayList<String> queryNodeList = new ArrayList<String>(nodeQueryList.get(i));
            ArrayList<String> valueNodeTypeList = new ArrayList<String>(nodeDataType.get(i));
            ArrayList<String> queryKey = new ArrayList<String>(nodeQueryKey.get(i));

            try {

                for (int index = 0; index < queryNodeList.size(); index++) {
                    String urlString = urlHost + queryNodeList.get(index);

                    JsonObject json = (JsonObject) parse.parse(ReadUrl.read(urlString));
                    JsonArray resultArray = json.get("data").getAsJsonObject().get("result").getAsJsonArray();

                    for (JsonElement element : resultArray) {

                        JsonObject resultItem = element.getAsJsonObject();
                        JsonObject metric = resultItem.get("metric").getAsJsonObject();
                        String nodeHostIp = metric.get(queryKey.get(0)).getAsString();
                        String nodeHostName = metric.get(queryKey.get(1)).getAsString();
                        String value = resultItem.get("value").getAsJsonArray().get(1).getAsString();

                        for (MonitorNode nodeItem : monitorNodes) {
                            if (nodeItem.getNodeIP().equals(nodeHostIp)) {
                                SetValue.run(nodeItem, valueNodeTypeList.get(index), value);
                                break;
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return monitorNodes;
    }

    private static void svcInitFromApi(ArrayList<MonitorService> s) {
        KubernetesClient client = K8sClient.getClient();
        ServiceList svcList = client.services().list();

        for (Service d : svcList.getItems()) {
            MonitorService svc = new MonitorService();
            svc.setServiceName(d.getMetadata().getName());
            svc.setNamespace(d.getMetadata().getNamespace());
            s.add(svc);
        }
    }

    @GetMapping(value = "monitorService")
    public List<MonitorService> getMonitorService() {
        ArrayList<MonitorService> serviceList = new ArrayList<>();
        svcInitFromApi(serviceList);

        Metric configFile = new Metric();
        configFile.init(PROMETHEUS_SERVICE_CONFIG_PATH);
        String hostIp = configFile.getHostIp();
        String port = configFile.getPort();
        String urlHost = "http://" + hostIp + ":" + port + "/api/v1/query?query=";
        JsonParser parse = new JsonParser();

        ArrayList<List<String>> serviceQueryList = configFile.getQueryList();
        ArrayList<List<String>> serviceQueryKey = configFile.getQueryKey();
        ArrayList<List<String>> serviceDataType = configFile.getDataType();

        for (int i = 0; i < configFile.getQueryNum(); i++) {
            ArrayList<String> queryServiceList = new ArrayList<String>(serviceQueryList.get(i));
            ArrayList<String> valueServiceTypeList = new ArrayList<String>(serviceDataType.get(i));
            ArrayList<String> queryKey = new ArrayList<String>(serviceQueryKey.get(i));
            ArrayList<String> serviceKey = new ArrayList<>();

            try {

                for (int index = 0; index < queryServiceList.size(); index++) {
                    String urlString = urlHost + queryServiceList.get(index);

                    JsonObject json = (JsonObject) parse.parse(ReadUrl.read(urlString));
                    JsonArray resultArray = json.get("data").getAsJsonObject().get("result").getAsJsonArray();

                    for (JsonElement element : resultArray) {

                        JsonObject resultItem = element.getAsJsonObject();
                        JsonObject metric = resultItem.get("metric").getAsJsonObject();
                        String value = resultItem.get("value").getAsJsonArray().get(1).getAsString();

                        //将查询的所有key的对应值取出来
                        serviceKey.clear();
                        for (int j = 0; j < queryKey.size(); j++) {
                            if (metric.has(queryKey.get(j))) {
                                serviceKey.add(metric.get(queryKey.get(j)).getAsString());
                            }
                        }

                        for (MonitorService svcItem : serviceList) {
                            if (svcItem.getKey().containsAll(serviceKey)) {
                                SetValue.run(svcItem, valueServiceTypeList.get(index), value);
                                break;
                            }
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return serviceList;
    }


    @PostMapping("/queryData")
    public String getQueryData(@RequestBody Map<String, Object> requestMap) {
        String queryString = (String) requestMap.get("queryString");
        String url = "http://" + PROMETHEUS_HOST + ":" + PROMETHEUS_PORT + "/api/v1/query?query=" + queryString;
        try {
            JSONObject result = JSONObject.parseObject(ReadUrl.read(url));
            return result.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString("");

    }

    @PostMapping("/queryNode")
    public String getQueryNode(@RequestBody Map<String, Object> requestMap) {
        String metric = (String) requestMap.get("metric");
        String nodeIP = (String) requestMap.get("nodeIP");
        String q = "";
        if (metric.equals("cpuUsage")) {
            q = "sum(rate(node_cpu_seconds_total{kubernetes_pod_host_ip=\"" + nodeIP
                    + "\"}[5m]))by(kubernetes_pod_host_ip,kubernetes_pod_node_name)";

        } else if (metric.equals("memUsage")) {
            q = "sum(node_memory_MemTotal_bytes-node_memory_MemAvailable_bytes{kubernetes_pod_host_ip=\"" + nodeIP
                    + "\"})by(kubernetes_pod_host_ip,kubernetes_pod_node_name)/sum(node_memory_MemTotal_bytes"
                    + ")by(kubernetes_pod_host_ip,kubernetes_pod_node_name)";

        } else if (metric.equals("diskUsage")) {
            q = "(sum(node_filesystem_size_bytes{kubernetes_pod_host_ip=\"" + nodeIP
                    + "\"})by(kubernetes_pod_node_name,kubernetes_pod_host_ip)-sum(node_filesystem_avail_bytes{kubernetes_pod_host_ip=\"" + nodeIP
                    + "\"})by(kubernetes_pod_node_name,kubernetes_pod_host_ip))/sum(node_filesystem_avail_bytes{kubernetes_pod_host_ip=\"" + nodeIP
                    + "\"})by(kubernetes_pod_node_name,kubernetes_pod_host_ip)";

        }

        String url = "http://" + PROMETHEUS_HOST + ":" + PROMETHEUS_PORT + "/api/v1/query?query=" + q;
        try {
            JSONObject result = JSONObject.parseObject(ReadUrl.read(url));
            return result.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString("");

    }

    @PostMapping("/queryService")
    public String getQueryService(@RequestBody Map<String, Object> requestMap) {
        String serviceName = (String) requestMap.get("serviceName");
        String namespace = (String) requestMap.get("namespace");
        String metric = (String) requestMap.get("metric");

        String q = "";
        if (metric.equals("responseTime")) {
            q = "sum(nginx_ingress_controller_response_duration_seconds_sum{"
                    + "service=\"" + serviceName
                    + "\",namespace=\"" + namespace
                    + "\",method=\"POST\"})by(service,namespace)";

        } else if (metric.equals("netErrors")) {
            q = "sum(container_network_transmit_errors_total{"
                    + "namespace=\"" + namespace
                    + "\"})by(pod_name,namespace)";

        }


        String url = "http://" + PROMETHEUS_HOST + ":" + PROMETHEUS_PORT + "/api/v1/query?query=" + q;
        try {
            JSONObject result = JSONObject.parseObject(ReadUrl.read(url));
            return result.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString("");

    }

}
