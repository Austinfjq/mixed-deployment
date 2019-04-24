package cn.harmonycloud.schedulingalgorithm.utils;

import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Node;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

//    public static void main(String[] args) throws Exception {
//        demo();
//    }

    private static void demo() throws Exception {
        K8sClient k8sClient = new K8sClient("test");
        while (true) {
            Thread.sleep(1000 * 5);
            System.out.println(Arrays.toString(k8sClient.nodeMap.keySet().toArray()));
            System.out.println(Arrays.toString(k8sClient.serviceMap.keySet().toArray()));
            System.out.println(Arrays.toString(k8sClient.podMap.keySet().toArray()));
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
}
