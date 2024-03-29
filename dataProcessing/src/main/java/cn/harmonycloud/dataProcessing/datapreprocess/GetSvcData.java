package cn.harmonycloud.dataProcessing.datapreprocess;

import cn.harmonycloud.dataProcessing.entry.PodData;
import cn.harmonycloud.dataProcessing.entry.ServiceData;
import cn.harmonycloud.dataProcessing.metric.Metric;
import cn.harmonycloud.dataProcessing.tools.K8sClient;
import cn.harmonycloud.dataProcessing.tools.ReadUrl;
import cn.harmonycloud.dataProcessing.tools.SetValue;
import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.ArrayList;
import java.util.List;

import static cn.harmonycloud.dataProcessing.metric.Constant.PROMETHEUS_SERVICE_CONFIG_PATH;
import static com.alibaba.fastjson.serializer.SerializerFeature.*;


public class GetSvcData {
    private static void initFromApi(ArrayList<ServiceData> s) {

        KubernetesClient client = K8sClient.getClient();
        ServiceList svcList = client.services().list();
        PodList podList = client.pods().list();

        for (Service d : svcList.getItems()) {

            //过滤其他namespace
            ArrayList<String> namespaceFilter = new ArrayList<>();
            namespaceFilter.add("kube-public");
            namespaceFilter.add("kube-system");
            namespaceFilter.add("md-system");
            namespaceFilter.add("monitoring");
            if (d.getMetadata().getNamespace() != null &&
                    namespaceFilter.contains(d.getMetadata().getNamespace())) {
                continue;
            }

            ServiceData svc = new ServiceData();
            svc.setServiceName(d.getMetadata().getName());
            svc.setNamespace(d.getMetadata().getNamespace());
            svc.setClusterIP(d.getSpec().getClusterIP());
            svc.setServiceType(d.getSpec().getType());

            for (ServicePort item : d.getSpec().getPorts()) {
                svc.setNodePort(String.valueOf(item.getNodePort()));
            }

            //get svc online type
            if (svc.getNamespace().equals("hadoop")) {
                svc.setOnlineType("offline");
            } else {
                svc.setOnlineType("online");
            }

            //get svc pod list nums
            ArrayList<String> podNameList = new ArrayList<>();
            if (d.getMetadata().getLabels() != null
                    && d.getMetadata().getLabels().containsKey("app")) {

                for (Pod pod : podList.getItems()) {
                    if (pod.getMetadata().getLabels() != null
                            && pod.getMetadata().getLabels().containsKey("app")
                            && d.getMetadata().getNamespace().equals(pod.getMetadata().getNamespace())
                            && d.getMetadata().getLabels().get("app").equals(pod.getMetadata().getLabels().get("app"))) {

                        podNameList.add(pod.getMetadata().getName());
                    }
                }

            }
            svc.setPodList(podNameList);

            if (podNameList.size() != 0) {
                svc.setPodNums((long) podNameList.size());
            } else {
                svc.setPodNums(1l);
            }

            //get svc ownername kind
            if (podNameList.size() != 0) {
                ArrayList<PodData> tempList = GetPodData.run();
                for (PodData item : tempList) {
                    if (podNameList.get(0).equals(item.getPodName())) {
                        svc.setResourceKind(item.getResourceKind());
                        svc.setResourceName(item.getResourceName());
                        break;
                    }
                }
            }

            s.add(svc);
        }
    }

    public static ArrayList<ServiceData> run() {

        ArrayList<ServiceData> serviceList = new ArrayList<>();
        initFromApi(serviceList);

        //get cpu mem disk requestBytes responseBytes
        ArrayList<PodData> podList = GetPodData.run();
        for (ServiceData svc : serviceList) {
            Double cpuUsage = 0.0;
            Double memUsage = 0.0;
            Double diskUsage = 0.0;
            Double requestBytes = 0.0;
            Double responseBytes = 0.0;
            Long netErrors = 0l;
            String volumeType = "";
            Double volumeUsage = 0.0;

            if (svc.getPodList().size() != 0) {
                for (String podName : svc.getPodList()) {
                    for (PodData pod : podList) {
                        if (pod.getPodName().equals(podName)) {
                            cpuUsage += pod.getCpuUsage();
                            memUsage += pod.getMemUsage();
                            diskUsage += pod.getVolumeUsage();
                            requestBytes += pod.getReceiveBytes();
                            responseBytes += pod.getResponseBytes();
                            netErrors += pod.getNetErrors();
                            volumeType = pod.getVolumeType();
                            volumeUsage += pod.getVolumeUsage();
                        }
                    }
                }
            }
            svc.setCpuUsage(cpuUsage);
            svc.setMemUsage(memUsage);
            svc.setDiskUsage(diskUsage);
            svc.setRequestBytes(requestBytes);
            svc.setResponseBytes(responseBytes);
            svc.setNetErrors(netErrors);
            svc.setVolumeType(volumeType);
            svc.setVolumeUsage(volumeUsage);
        }


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

                        for (ServiceData svcItem : serviceList) {
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

    public static void main(String[] args) {
        String returnValue = JSON.toJSONString(run(), WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty);
        System.out.println(returnValue);
    }

}
