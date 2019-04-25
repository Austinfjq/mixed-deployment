package cn.harmonycloud.schedulingalgorithm.utils;

import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.NodeCondition;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.ApiResponse;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.Pair;
import io.kubernetes.client.ProgressResponseBody;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.models.V1Node;
import io.kubernetes.client.models.V1NodeCondition;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class K8sClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(K8sClient.class);
    private final static ExecutorService executorService = Executors.newCachedThreadPool();

    private final String namespace;
    private final ApiClient client;
    private final CoreV1Api api;

    private final Map<String, V1Node> nodeMap;
    private final Map<String, V1Service> serviceMap;
    private final Map<String, V1Pod> podMap;

    public K8sClient(String namespace) {
        this.namespace = namespace;
        try {
            client = Config.defaultClient();
        } catch (IOException e) {
            throw new RuntimeException("K8sClient initialize fail!", e);
        }
        Configuration.setDefaultApiClient(client);
        api = new CoreV1Api();
        nodeMap = new ConcurrentHashMap<>();
        serviceMap = new ConcurrentHashMap<>();
        podMap = new ConcurrentHashMap<>();
        executorService.execute(new NodeWatcher());
        executorService.execute(new ServiceWatcher());
        executorService.execute(new PodWatcher());
    }

    public V1Node getNode(String nodeName) {
        return nodeMap.get(nodeName);
    }

    public V1Service getService(String serviceName) {
        return serviceMap.get(serviceName);
    }

    public V1Pod getPod(String podName) {
        return podMap.get(podName);
    }

    public List<V1Node> listNodes() {
        return new ArrayList<>(nodeMap.values());
    }

    public List<V1Service> listServices() {
        return new ArrayList<>(serviceMap.values());
    }

    public List<V1Pod> listPods() {
        return new ArrayList<>(podMap.values());
    }

    private static void demo() throws Exception {
        K8sClient k8sClient = new K8sClient("test");
//        metricTest(k8sClient);
        while (true) {
            Thread.sleep(1000 * 5);
            System.out.println(Arrays.toString(k8sClient.nodeMap.keySet().toArray()));
            System.out.println(Arrays.toString(k8sClient.serviceMap.keySet().toArray()));
            System.out.println(Arrays.toString(k8sClient.podMap.keySet().toArray()));
            System.out.println(new Gson().toJson(k8sClient.listNodes().get(0).getStatus().getAllocatable().get("memory")));
        }
    }

    private class NodeWatcher implements Runnable {
        @Override
        public void run() {
            String resourceVersion = null;
            while (true) {
                try {
                    try (Watch<V1Node> watch = Watch.createWatch(
                            client,
                            api.listNodeCall(Boolean.TRUE, null, null, null, null, null, resourceVersion, null, Boolean.TRUE, null, null),
                            new TypeToken<Watch.Response<V1Node>>() {
                            }.getType())) {
                        while (watch.hasNext()) {
                            Watch.Response<V1Node> item = watch.next();
                            LOGGER.info("K8s client watcher: resource=Node, type=" + item.type + ", name=" + item.object.getMetadata().getName());
                            // update node cache
                            if ("DELETED".equalsIgnoreCase(item.type)) {
                                // DELETED
                                nodeMap.remove(item.object.getMetadata().getName());
                            } else {
                                // ADDED, MODIFIED, ERROR
                                nodeMap.put(item.object.getMetadata().getName(), item.object);
                            }
                            // update resourceVersion
                            resourceVersion = item.object.getMetadata().getResourceVersion();
                        }
                    }
                } catch (Throwable e) {
                    processWatcherThrowable(e);
                }
            }
        }
    }

    private class ServiceWatcher implements Runnable {
        @Override
        public void run() {
            String resourceVersion = null;
            while (true) {
                try {
                    try (Watch<V1Service> watch = Watch.createWatch(
                            client,
                            api.listNamespacedServiceCall(namespace, Boolean.TRUE, null, null, null, null, null, resourceVersion, null, Boolean.TRUE, null, null),
                            new TypeToken<Watch.Response<V1Service>>() {
                            }.getType())) {
                        while (watch.hasNext()) {
                            Watch.Response<V1Service> item = watch.next();
                            LOGGER.info("K8s client watcher: resource=Service, type=" + item.type + ", name=" + item.object.getMetadata().getName());
                            // update service cache
                            if ("DELETED".equalsIgnoreCase(item.type)) {
                                // DELETED
                                serviceMap.remove(item.object.getMetadata().getName());
                            } else {
                                // ADDED, MODIFIED, ERROR
                                serviceMap.put(item.object.getMetadata().getName(), item.object);
                            }
                            // update resourceVersion
                            resourceVersion = item.object.getMetadata().getResourceVersion();
                        }
                    }
                } catch (Throwable e) {
                    processWatcherThrowable(e);
                }
            }
        }
    }

    private class PodWatcher implements Runnable {
        @Override
        public void run() {
            String resourceVersion = null;
            while (true) {
                try {
                    try (Watch<V1Pod> watch = Watch.createWatch(
                            client,
                            api.listNamespacedPodCall(namespace, Boolean.TRUE, null, null, null, null, null, resourceVersion, null, Boolean.TRUE, null, null),
                            new TypeToken<Watch.Response<V1Pod>>() {
                            }.getType())) {
                        while (watch.hasNext()) {
                            Watch.Response<V1Pod> item = watch.next();
                            LOGGER.info("K8s client watcher: resource=Pod, type=" + item.type + ", name=" + item.object.getMetadata().getName());
                            // update pod cache
                            if ("DELETED".equalsIgnoreCase(item.type)) {
                                // DELETED
                                podMap.remove(item.object.getMetadata().getName());
                            } else {
                                // ADDED, MODIFIED, ERROR
                                podMap.put(item.object.getMetadata().getName(), item.object);
                            }
                            // update resourceVersion
                            resourceVersion = item.object.getMetadata().getResourceVersion();
                        }
                    }
                } catch (Throwable e) {
                    processWatcherThrowable(e);
                }
            }
        }
    }

    private void processWatcherThrowable(Throwable e) {
        if (!(e.getCause() instanceof SocketTimeoutException)) {
            e.printStackTrace();
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static void metricTest(K8sClient k8sClient) throws ApiException {
        ApiClient apiClient = k8sClient.client;
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/apis/metrics/v1alpha1/nodes";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
                "application/json", "application/yaml", "application/vnd.kubernetes.protobuf", "application/json;stream=watch", "application/vnd.kubernetes.protobuf;stream=watch"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
                "*/*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "BearerToken" };
        com.squareup.okhttp.Call call = apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        ApiResponse r = apiClient.execute(call, localVarReturnType);
        System.out.println(r);
    }

    public static Node v1NodeToNode(V1Node v1Node) {
        Node node = new Node();
        // 正确的数据应该是类似于v1Node.getStatus().getAllocatable().get("cpu").toSuffixedString()，但是data-center给的数据就是下面这个错的。。。
        node.setAllocatableCpuCores(v1Node.getStatus().getAllocatable().get("cpu").getNumber().doubleValue());
        node.setAllocatableMem(v1Node.getStatus().getAllocatable().get("memory").getNumber().doubleValue());
        node.setAllocatablePods(v1Node.getStatus().getAllocatable().get("pods").getNumber().doubleValue());
        List<V1NodeCondition> list = v1Node.getStatus().getConditions().stream().filter(c -> "Ready".equalsIgnoreCase(c.getType()) != "True".equalsIgnoreCase(c.getStatus())).collect(Collectors.toList());
        node.setNodeConditions(list.isEmpty() ? null : new NodeCondition(list.get(0).getType(), list.get(0).getStatus()));
        node.setCpuCores(v1Node.getStatus().getCapacity().get("cpu").getNumber().doubleValue());
//        todo node.setCpuUsage(v1Node.getStatus().get);
        // 集群似乎没有部署metrics server和heapster，需要访问cAdvisor
        throw new RuntimeException("not supported");
    }

    public static Service v1ServiceToService(V1Service v1Service) {
        throw new RuntimeException("not supported");
    }

    public static Pod v1PodToPod(V1Pod v1Pod) {
        throw new RuntimeException("not supported");
    }

    public static void main(String[] args) throws Exception {
        throw new RuntimeException("not supported");
    }
}
