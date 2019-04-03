package cn.harmonycloud.dataProcessing.controller;

import cn.harmonycloud.dataProcessing.metric.Metric;
import cn.harmonycloud.dataProcessing.model.MonitorNode;
import cn.harmonycloud.dataProcessing.model.MonitorService;
import cn.harmonycloud.dataProcessing.tools.K8sClient;
import cn.harmonycloud.dataProcessing.tools.ReadUrl;
import cn.harmonycloud.dataProcessing.tools.SetValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static cn.harmonycloud.dataProcessing.metric.Constant.PROMETHEUS_NODE_CONFIG_PATH;
import static cn.harmonycloud.dataProcessing.metric.Constant.PROMETHEUS_SERVICE_CONFIG_PATH;

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
    public List<MonitorService> getMonitorService(){
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


}
