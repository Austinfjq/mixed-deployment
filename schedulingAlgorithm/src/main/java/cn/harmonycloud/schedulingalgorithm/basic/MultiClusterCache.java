package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.utils.HttpUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MultiClusterCache {
    private final static Logger LOGGER = LoggerFactory.getLogger(MultiClusterCache.class);
    private static Gson gson = new Gson();

    private Map<String, Cache> cacheMap = new HashMap<>();

    public Cache get(String clusterMasterIP) {
        return cacheMap.get(clusterMasterIP);
    }

    @SuppressWarnings("unchecked")
    public void fetchMultiCacheData() {
        LOGGER.info("start fetchMultiCacheData!");
        ArrayList<Service> serviceList = (ArrayList<Service>) (Object) fetchMultiClusterOneDimension(GlobalSetting.URI_GET_SERVICE, Service[].class);
        ArrayList<Pod> podList = (ArrayList<Pod>) (Object) fetchMultiClusterOneDimension(GlobalSetting.URI_GET_POD, Pod[].class);
        ArrayList<Node> nodeList = (ArrayList<Node>) (Object) fetchMultiClusterOneDimension(GlobalSetting.URI_GET_NODE, Node[].class);

        Map<String, List<Service>> clusterToServicesMap = serviceList.stream().collect(Collectors.groupingBy(Service::getClusterMasterIP));
        Map<String, List<Pod>> clusterToPodsMap = podList.stream().collect(Collectors.groupingBy(Pod::getClusterMasterIP));
        Map<String, List<Node>> clusterToNodesMap = nodeList.stream().collect(Collectors.groupingBy(Node::getClusterMasterIP));

        Set<String> clusterMasterIPs = new HashSet<>();
        clusterMasterIPs.addAll(clusterToServicesMap.keySet());
        clusterMasterIPs.addAll(clusterToPodsMap.keySet());
        clusterMasterIPs.addAll(clusterToNodesMap.keySet());

        cacheMap.clear();
        clusterMasterIPs.forEach(ip -> {
            Cache cache = new Cache();
            cache.overWriteCache(clusterToServicesMap.get(ip), clusterToPodsMap.get(ip), clusterToNodesMap.get(ip));
            cacheMap.put(ip, cache);
        });
    }

    <T> List<Object> fetchMultiClusterOneDimension(String uri, Class<T> clazz) {
        return HttpUtil.getAndReadClass(uri, clazz);
    }
}
