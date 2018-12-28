package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeCache extends Cache {
    private Map<String, Service> serviceMap;
    private Map<String, Pod> podMap;
    private Map<String, Node> nodeMap;
    private List<Node> nodeList;

    public FakeCache() {
        fetchCacheData();
    }

    @Override
    public Map<String, Service> getServiceMap() {
        return serviceMap;
    }

    @Override
    public Map<String, Pod> getPodMap() {
        return podMap;
    }

    @Override
    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }

    @Override
    public List<Node> getNodeList() {
        return nodeList;
    }

    /**
     * 每轮调度开始时调用，获取数据，覆盖上一轮的旧数据
     */
    @Override
    public void fetchCacheData() {
        List<Service> serviceList = genServiceList();
        List<Pod> podList = new ArrayList<>();//genPodList();
        List<Node> nodeList = genNodeList();
        serviceMap = new HashMap<>();
        serviceList.forEach(s -> serviceMap.put(DOUtils.getServiceFullName(s), s));
        podMap = new HashMap<>();
        podList.forEach(p -> podMap.put(DOUtils.getServiceFullName(p), p));
        nodeMap = new HashMap<>();
        nodeList.forEach(n -> nodeMap.put(n.getNodeName(), n));
        this.nodeList = nodeList;
    }

    /**
     * 同一轮调度内不再使用fetchCacheData()更新数据，updateCache()方法对缓存进行增量维护，更节省时间
     *
     * @param pod  被调度的pod
     * @param host 为pod选择的节点
     */
    @Override
    public void updateCache(Pod pod, String host) {
        // TODO 有新数据需要缓存时，需要一并更改这里
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
        List<String> podList = new ArrayList<>(Arrays.asList(service.getPodList()));
        if (isAdd) {
            podList.add(DOUtils.getPodFullName(pod));
        } else {
            podList.remove(DOUtils.getPodFullName(pod));
        }
        service.setPodList(podList.toArray(new String[0]));
        service.setCpuUsage(String.valueOf(Double.valueOf(service.getCpuUsage()) + (isAdd ? 1 : -1) * Double.valueOf(service.getCpuCosume())));
        service.setMemUsage(String.valueOf(Double.valueOf(service.getMemUsage()) + (isAdd ? 1 : -1) * Double.valueOf(service.getMemCosume())));
        // TODO update up down net io
        // 更新 node占用的资源 ps:似乎没有node总资源，TODO node下pod列表
        Node node = nodeMap.get(host);
        node.setCpuUsage(String.valueOf(Double.valueOf(node.getCpuUsage()) + (isAdd ? 1 : -1) * Double.valueOf(service.getCpuCosume())));
        node.setMemUsage(String.valueOf(Double.valueOf(node.getMemUsage()) + (isAdd ? 1 : -1) * Double.valueOf(service.getMemCosume())));
    }

    private List<Service> genServiceList() {

        Service s1 = new Service();
        s1.setNamespace("ns1");
        s1.setServiceName("s1");
        s1.setIntensiveType(Constants.INTENSIVE_TYPE_CPU);
        s1.setCpuCosume("4.0");
        s1.setMemCosume("200.0");
        s1.setUPNetIOCosume("100.0");
        s1.setDownNetIOCosume("100.0");

        Service s2 = new Service();
        s2.setNamespace("ns1");
        s2.setServiceName("s2");
        s2.setIntensiveType(Constants.INTENSIVE_TYPE_MEMORY);
        s2.setCpuCosume("1.0");
        s2.setMemCosume("1000.0");
        s2.setUPNetIOCosume("100.0");
        s2.setDownNetIOCosume("100.0");

        Service s3 = new Service();
        s3.setNamespace("ns1");
        s3.setServiceName("s3");
        s3.setIntensiveType(Constants.INTENSIVE_TYPE_UP_NET_IO);
        s3.setCpuCosume("1.0");
        s3.setMemCosume("200.0");
        s3.setUPNetIOCosume("1000.0");
        s3.setDownNetIOCosume("100.0");

        Service s4 = new Service();
        s4.setNamespace("ns1");
        s4.setServiceName("s4");
        s4.setIntensiveType(Constants.INTENSIVE_TYPE_UP_NET_IO);
        s4.setCpuCosume("1.0");
        s4.setMemCosume("200.0");
        s4.setUPNetIOCosume("2000.0");
        s4.setDownNetIOCosume("100.0");

        return new ArrayList<>(Arrays.asList(s1,s2,s3,s4));
    }

    private List<Pod> genPodList() {

        Pod p1 = new Pod();
        p1.setNamespace("ns1");
        p1.setServiceName("s1");
        p1.setOperation(Constants.OPERATION_ADD);

        Pod p2 = new Pod();
        p2.setNamespace("ns1");
        p2.setServiceName("s2");
        p2.setOperation(Constants.OPERATION_ADD);

        Pod p3 = new Pod();
        p3.setNamespace("ns1");
        p3.setServiceName("s3");
        p3.setOperation(Constants.OPERATION_ADD);

//        Pod p4 = new Pod();
//        pods.add(p4);
//        p4.setNamespace("ns1");
//        p4.setServiceName("s3");
//        p4.setOperation(Constants.OPERATION_DELETE);

        Pod p5 = new Pod();
        p5.setNamespace("ns1");
        p5.setServiceName("s4");
        p5.setOperation(Constants.OPERATION_ADD);

        return new ArrayList<>(Arrays.asList(p1, p2, p3, p5));
    }

    private List<Node> genNodeList() {
        Node n1 = new Node();
        n1.setNodeName("n1");
        n1.setCpuUsage("0.2");

        Node n2 = new Node();
        n2.setNodeName("n2");
        n2.setCpuUsage("0.2");

        Node n3 = new Node();
        n3.setNodeName("n3");
        n3.setCpuUsage("0.2");

        return new ArrayList<>(Arrays.asList(n1, n2, n3));
    }
}
