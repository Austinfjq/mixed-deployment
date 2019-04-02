package cn.harmonycloud.datapreprocess;

import cn.harmonycloud.entry.PodData;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.harmonycloud.metric.Constant.*;
import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;


public class GetPodData {

    private static void initFromApi(ArrayList<PodData> p) {

        KubernetesClient client = K8sClient.getClient();
        PodList podList = client.pods().list();
        ServiceList svcList = client.services().list();

        for (Pod d : podList.getItems()) {

            PodData pod = new PodData();

            pod.setPodName(d.getMetadata().getName());
            pod.setNamespace(d.getMetadata().getNamespace());
            pod.setPodIp(d.getStatus().getPodIP());
            pod.setNodeName(d.getSpec().getNodeName());
            pod.setLocateNodeIP(d.getStatus().getHostIP());
            pod.setState(d.getStatus().getPhase());
            pod.setNodeSelector(d.getSpec().getNodeSelector());
            pod.setDeletionStamp(d.getMetadata().getDeletionTimestamp());
            pod.setLabels(d.getMetadata().getLabels());

            if (pod.getNamespace().equals("hadoop")) {
                pod.setOnlineType("offline");
            } else if (d.getMetadata().getLabels().get("service_attribute") != null) {
                pod.setOnlineType(d.getMetadata().getLabels().get("service_attribute"));
            } else {
                pod.setOnlineType("online");
            }

//            for (OwnerReference item : d.getMetadata().getOwnerReferences()) {
//                pod.setResourceKind(item.getKind());
//                pod.setResourceName(item.getName());
//                pod.setOwnerReferencesUid(item.getUid());
//            }

            for (ContainerStatus item : d.getStatus().getContainerStatuses()) {
                pod.setImageName(item.getImage());
            }


            if (d.getMetadata().getLabels() != null
                    && d.getMetadata().getLabels().containsKey("daemon")) {

                for (Service svc : svcList.getItems()) {
                    if (svc.getMetadata().getLabels() != null
                            && svc.getMetadata().getLabels().containsKey("daemon")) {

                        if (d.getMetadata().getLabels().get("daemon")
                                .equals(svc.getMetadata().getLabels().get("daemon"))) {
                            pod.setServiceName(svc.getMetadata().getName());
                        }
                    }
                }

            }


            //get PersistentVolumeClaimNames
            ArrayList<String> persistentClaim = new ArrayList<>();
            for (Volume v : d.getSpec().getVolumes()) {
                if (v.getPersistentVolumeClaim() != null) {
                    persistentClaim.add(v.getPersistentVolumeClaim().getClaimName());
                } else {
                    persistentClaim.add("null");
                }
            }
            pod.setPersistentVolumeClaimNames(persistentClaim);


            //get containers
            Map<String, String> containers = new HashMap<>();
            for (Container c : d.getSpec().getContainers()) {
                if (c.getResources().getRequests() != null) {
                    containers.put("resourcesRequests", c.getResources().getRequests().toString());
                }
                if (c.getResources().getLimits() != null) {
                    containers.put("resourcesLimits", c.getResources().getLimits().toString());
                }
                if (c.getImage() != null) {
                    containers.put("image", c.getImage());
                }

                if (c.getPorts() != null) {

                    ArrayList<String> portList = new ArrayList<>();
                    Map<String, String> temp = new HashMap<>();
                    for (ContainerPort cp : c.getPorts()) {
                        temp.clear();
                        temp.put("containerPort", String.valueOf(cp.getContainerPort()));
                        temp.put("hostPort", String.valueOf(cp.getHostPort()));
                        temp.put("hostIP", cp.getHostIP());
                        temp.put("protocol", cp.getProtocol());
                        portList.add(temp.toString());
                    }
                    containers.put("ports", portList.toString());

                }
            }
            pod.setContainers(containers);

            //get affinity
            if (d.getSpec().getAffinity() != null) {
                Map<String, String> temp = new HashMap<>();

                if (d.getSpec().getAffinity().getAdditionalProperties() != null) {
                    temp.put("AdditionalProperties", d.getSpec().getAffinity().getAdditionalProperties().toString());
                }

                //get podAffinity
                if (d.getSpec().getAffinity().getPodAffinity() != null) {
                    temp.put("podAffinity", d.getSpec().getAffinity().getPodAffinity().toString());
                }

                //get podAntiAffinity
                if (d.getSpec().getAffinity().getPodAntiAffinity() != null) {
                    temp.put("podAntiAffinity", d.getSpec().getAffinity().getPodAntiAffinity().toString());
                }

                //get nodeAffinity
                if (d.getSpec().getAffinity().getNodeAffinity() != null) {
                    temp.put("nodeAffinity", d.getSpec().getAffinity().getNodeAffinity().toString());
                }

                pod.setAffinity(temp);
            }


            //get toleration
            ArrayList<Map<String, String>> toleration = new ArrayList<>();
            for (Toleration t : d.getSpec().getTolerations()) {
                Map<String, String> temp = new HashMap<>();
                temp.clear();
                if (t.getValue() != null)
                    temp.put("value", t.getValue());

                if (t.getEffect() != null)
                    temp.put("effect", t.getEffect());

                if (t.getKey() != null)
                    temp.put("key", t.getKey());

                if (t.getOperator() != null)
                    temp.put("operator", t.getOperator());

                if (t.getAdditionalProperties() != null)
                    temp.put("additionalProperties", t.getAdditionalProperties().toString());

                if (t.getTolerationSeconds() != null)
                    temp.put("tolerationSeconds", t.getTolerationSeconds().toString());
                toleration.add(temp);
            }
            pod.setToleration(toleration);

            for (OwnerReference item : d.getMetadata().getOwnerReferences()) {
                String ownerKind = item.getKind();
                String ownerNameTemp = item.getName();
                pod.setResourceKind(ownerKind);

                if (ownerKind.equals("ReplicaSet")) {
                    String[] ownerNameList = ownerNameTemp.split("-");
                    String ownerName = "";
                    for (int i = 0; i < ownerNameList.length - 1; i++) {
                        ownerName += ownerNameList[i];
                        if (i < ownerNameList.length - 2) {
                            ownerName += "-";
                        }
                    }
                    pod.setResourceName(ownerName);
                    if (!ownerName.equals("")) {
                        pod.setResourceKind("Deployment");
                    }
                } else {
                    pod.setResourceName(ownerNameTemp);
                }
            }

            p.add(pod);
        }
    }

    public static ArrayList<PodData> run() {

        ArrayList<PodData> podList = new ArrayList<>();
        initFromApi(podList);

        Metric configFile = new Metric();
        configFile.init(PROMETHEUS_POD_CONFIG_PATH);

        String hostIp = configFile.getHostIp();
        String port = configFile.getPort();
        String urlHost = "http://" + hostIp + ":" + port + "/api/v1/query?query=";
        JsonParser parse = new JsonParser();

        ArrayList<List<String>> podQueryList = configFile.getQueryList();
        ArrayList<List<String>> podQueryKey = configFile.getQueryKey();
        ArrayList<List<String>> podDataType = configFile.getDataType();


        for (int i = 0; i < configFile.getQueryNum(); i++) {

            ArrayList<String> queryPodList = new ArrayList<String>(podQueryList.get(i));
            ArrayList<String> valuePodTypeList = new ArrayList<String>(podDataType.get(i));
            ArrayList<String> queryKey = new ArrayList<String>(podQueryKey.get(i));

            ArrayList<String> podKey = new ArrayList<>();

            try {

                for (int index = 0; index < queryPodList.size(); index++) {
                    String urlString = urlHost + queryPodList.get(index);

                    JsonObject json = (JsonObject) parse.parse(ReadUrl.read(urlString));
                    JsonArray resultArray = json.get("data").getAsJsonObject().get("result").getAsJsonArray();

                    for (JsonElement element : resultArray) {

                        JsonObject resultItem = element.getAsJsonObject();
                        JsonObject metric = resultItem.get("metric").getAsJsonObject();
                        String value = resultItem.get("value").getAsJsonArray().get(1).getAsString();

                        //将查询的所有key的对应值取出来
                        podKey.clear();
                        for (int j = 0; j < queryKey.size(); j++) {
                            if (metric.has(queryKey.get(j))) {
                                podKey.add(metric.get(queryKey.get(j)).getAsString());
                            }
                        }

                        for (PodData podItem : podList) {
                            if (podItem.getKey().containsAll(podKey)) {
                                SetValue.run(podItem, valuePodTypeList.get(index), value);
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

//                        String labelValue = metric.get(label.get(index)).getAsString();
                        String labelValue = "";
                        if (metric.has(label.get(index))) {
                            labelValue = metric.get(label.get(index)).getAsString();
                        } else {
                            break;
                        }

                        ArrayList<String> podKey = new ArrayList<>();
                        //将查询的所有key的对应值取出来
                        for (int j = 0; j < queryKey.size(); j++) {
                            if (metric.has(queryKey.get(j))) {
                                podKey.add(metric.get(queryKey.get(j)).getAsString());
                            }
                        }

                        for (PodData podItem : podList) {
                            if (podItem.getKey().containsAll(podKey)) {
                                SetValue.run(podItem, labelType.get(index), labelValue);
                                break;
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //        String time = String.valueOf(System.currentTimeMillis());
        return podList;
    }

    public static void main(String[] args) {
        String returnValue = JSON.toJSONString(run(), WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty);
        System.out.println(returnValue);
    }
}
