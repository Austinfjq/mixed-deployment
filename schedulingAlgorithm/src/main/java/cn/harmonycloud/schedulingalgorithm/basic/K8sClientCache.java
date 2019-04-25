package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.NodeForecastData;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.utils.K8sClient;
import io.kubernetes.client.models.V1Node;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Cache from K8s client
 * Data will be transformed to fit the old format from data-center.
 */
public class K8sClientCache extends Cache {
    private final K8sClient k8sClient;

    public K8sClientCache(String namespace) {
        k8sClient = new K8sClient(namespace);
    }

    @Override
    public void fetchSingleCacheData() {
        List<V1Node> v1NodeList = k8sClient.listNodes();
        List<V1Service> v1ServiceList = k8sClient.listServices();
        List<V1Pod> v1PodList = k8sClient.listPods();

        List<Node> nodeList = v1NodeList.stream().map(K8sClient::v1NodeToNode).collect(Collectors.toList());
        List<Service> serviceList = v1ServiceList.stream().map(K8sClient::v1ServiceToService).collect(Collectors.toList());
        List<Pod> podList = v1PodList.stream().map(K8sClient::v1PodToPod).collect(Collectors.toList());

        overWriteCache(serviceList, podList, nodeList);
    }
}
