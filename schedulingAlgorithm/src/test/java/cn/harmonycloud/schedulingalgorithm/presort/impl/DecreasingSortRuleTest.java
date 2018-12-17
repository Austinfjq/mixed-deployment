package cn.harmonycloud.schedulingalgorithm.presort.impl;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecreasingSortRuleTest {
    @Test
    public void test() throws Exception {
        Cache cache = new Cache();
        Map<String, Service> serviceMap = new HashMap<>();
        Field field = Cache.class.getDeclaredField("serviceMap");
        field.setAccessible(true);
        field.set(cache, serviceMap);
        Service s1 = new Service();
        s1.setNamespace("ns1");
        s1.setServiceName("s1");
        s1.setIntensiveType(Constants.INTENSIVE_TYPE_CPU);
        s1.setCpuCosume("4.0");
        s1.setMemCosume("200.0");
        s1.setUPNetIOCosume("100.0");
        s1.setDownNetIOCosume("100.0");
        serviceMap.put(DOUtils.getServiceFullName(s1), s1);
        Service s2 = new Service();
        s2.setNamespace("ns1");
        s2.setServiceName("s2");
        s2.setIntensiveType(Constants.INTENSIVE_TYPE_MEMORY);
        s2.setCpuCosume("1.0");
        s2.setMemCosume("1000.0");
        s2.setUPNetIOCosume("100.0");
        s2.setDownNetIOCosume("100.0");
        serviceMap.put(DOUtils.getServiceFullName(s2), s2);
        Service s3 = new Service();
        s3.setNamespace("ns1");
        s3.setServiceName("s3");
        s3.setIntensiveType(Constants.INTENSIVE_TYPE_UP_NET_IO);
        s3.setCpuCosume("1.0");
        s3.setMemCosume("200.0");
        s3.setUPNetIOCosume("1000.0");
        s3.setDownNetIOCosume("100.0");
        serviceMap.put(DOUtils.getServiceFullName(s3), s3);
        Service s4 = new Service();
        s4.setNamespace("ns1");
        s4.setServiceName("s4");
        s4.setIntensiveType(Constants.INTENSIVE_TYPE_UP_NET_IO);
        s4.setCpuCosume("1.0");
        s4.setMemCosume("200.0");
        s4.setUPNetIOCosume("2000.0");
        s4.setDownNetIOCosume("100.0");
        serviceMap.put(DOUtils.getServiceFullName(s4), s4);
        List<Pod> pods = new ArrayList<>();
        Pod p1 = new Pod();
        pods.add(p1);
        p1.setNamespace("ns1");
        p1.setServiceName("s1");
        p1.setOperation(Constants.OPERATION_ADD);
        Pod p2 = new Pod();
        pods.add(p2);
        p2.setNamespace("ns1");
        p2.setServiceName("s2");
        p2.setOperation(Constants.OPERATION_ADD);
        Pod p3 = new Pod();
        pods.add(p3);
        p3.setNamespace("ns1");
        p3.setServiceName("s3");
        p3.setOperation(Constants.OPERATION_ADD);
//        Pod p4 = new Pod();
//        pods.add(p4);
//        p4.setNamespace("ns1");
//        p4.setServiceName("s3");
//        p4.setOperation(Constants.OPERATION_DELETE);
        Pod p5 = new Pod();
        pods.add(p5);
        p5.setNamespace("ns1");
        p5.setServiceName("s4");
        p5.setOperation(Constants.OPERATION_ADD);

        DecreasingSortRule rule = new DecreasingSortRule();
        List<Pod> sorted = rule.sort(pods, cache);
        for (Pod pod : sorted) {
            System.out.println(DOUtils.getServiceFullName(pod) + ", " + pod.getOperation());
        }
    }
}
