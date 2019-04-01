package cn.harmonycloud.datapreprocess;

import cn.harmonycloud.entry.NodeData;
import cn.harmonycloud.metric.Metric;
import cn.harmonycloud.tools.K8sClient;
import cn.harmonycloud.tools.ReadUrl;
import cn.harmonycloud.tools.SetValue;
import cn.harmonycloud.tools.Write2ES;
import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.*;

import static cn.harmonycloud.metric.Constant.PROMETHEUS_NODE_CONFIG;
import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;

public class GetNodeData {

    private static void initFromApi(ArrayList<NodeData> n) {

        KubernetesClient client = K8sClient.getClient();
        NodeList nodeList = client.nodes().list();
        PodList podList = client.pods().list();

        for (Node d : nodeList.getItems()) {
            NodeData node = new NodeData();
            node.setNodeName(d.getMetadata().getName());
            node.setLabels(d.getMetadata().getLabels());
            node.setNodeIP(d.getStatus().getAddresses().get(0).getAddress());

            if (d.getSpec().getUnschedulable() != null) {
                node.setUnschedulable(d.getSpec().getUnschedulable());
            }

            if (d.getSpec().getTaints() != null) {
                node.setTaints(d.getSpec().getTaints().toString());
            }


            // get nodeContions
            Map<String, String> nodeConditions = new HashMap<>();
            for (NodeCondition c : d.getStatus().getConditions()) {
                nodeConditions.put("type", c.getType());
                nodeConditions.put("status", c.getStatus());
            }
            node.setNodeConditions(nodeConditions);

            //get used ports and podnums
            Map<String, String> portTemp = new HashMap<>();
            long podNums = 0l;

            for (Pod pod : podList.getItems()) {
                if (pod.getSpec().getNodeName() != null &&
                        pod.getSpec().getNodeName().equals(d.getMetadata().getName())) {

                    podNums++;
                    //get ports
                    for (Container c : pod.getSpec().getContainers()) {
                        if (c.getPorts() != null) {
                            for (ContainerPort cp : c.getPorts()) {
                                portTemp.put(cp.getContainerPort().toString(), cp.getProtocol());
                            }
                        }
                    }
                }
            }

            node.setPodNums(podNums);
            node.setUsedPorts(portTemp);

            n.add(node);
        }
    }

    public static ArrayList<NodeData> run() {

        ArrayList<NodeData> nodeList = new ArrayList<>();
        initFromApi(nodeList);

        Metric configFile = new Metric();
        configFile.init(PROMETHEUS_NODE_CONFIG);

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

//                        boolean flag = true;
                        JsonObject resultItem = element.getAsJsonObject();
                        JsonObject metric = resultItem.get("metric").getAsJsonObject();

                        String nodeHostIp = metric.get(queryKey.get(0)).getAsString();
                        String nodeHostName = metric.get(queryKey.get(1)).getAsString();
                        String value = resultItem.get("value").getAsJsonArray().get(1).getAsString();

                        for (NodeData nodeItem : nodeList) {
                            if (nodeItem.getNodeName().equals(nodeHostName)) {

//                                if (nodeItem.getNodeIP().equals("")) {
//                                    nodeItem.setNodeIP(nodeHostIp);
//                                }

                                SetValue.run(nodeItem, valueNodeTypeList.get(index), value);
//                                flag = false;
                                break;
                            }
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        ArrayList<List<String>> labelQueryKey = configFile.getLabelQueryKey();
        ArrayList<List<String>> labelName = configFile.getLabelName();
        ArrayList<List<String>> labelQueryList = configFile.getLabelQueryList();
        ArrayList<List<String>> labelTypeList = configFile.getLabelTypeList();

        for (int i = 0; i < configFile.getLabelQueryNum(); i++) {

            ArrayList<String> queryKey = new ArrayList<String>(labelQueryKey.get(i));
            ArrayList<String> label = new ArrayList<String>(labelName.get(i));
            ArrayList<String> queryList = new ArrayList<String>(labelQueryList.get(i));
            ArrayList<String> labelType = new ArrayList<String>(labelTypeList.get(i));

            try {

                for (int index = 0; index < queryList.size(); index++) {
                    String urlString = urlHost + queryList.get(index);

                    JsonObject json = (JsonObject) parse.parse(ReadUrl.read(urlString));
                    JsonArray resultArray = json.get("data").getAsJsonObject().get("result").getAsJsonArray();

                    for (JsonElement element : resultArray) {

                        JsonObject resultItem = element.getAsJsonObject();
                        JsonObject metric = resultItem.get("metric").getAsJsonObject();

                        String labelValue = metric.get(label.get(index)).getAsString();

                        //将查询的key的对应值取出来
                        String nodeKey = metric.get(queryKey.get(0)).getAsString();

                        for (NodeData nodeItem : nodeList) {
                            if (nodeItem.getKey().equals(nodeKey)) {
                                SetValue.run(nodeItem, labelType.get(index), labelValue);
                                break;
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return nodeList;
    }

    public static void main(String[] args) {
        String returnValue = JSON.toJSONString(run(), WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty);
        System.out.println(returnValue);
    }
}
