package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeScheduler implements Scheduler {
    @Test
    public void schedulerTest() {
        List<Pod> pods = genPodList();
        List<Pod> sortedPods = greedyAlgorithm.presort(pods, cache);
        sortedPods.stream().map(Pod::getServiceName).forEach(System.out::println);

        // TODO need test these two predicate rules
//        new PodMatchNodeSelectorPredicate().predicate(pod, node, cache);
//        new MatchInterPodAffinityPredicate().predicate(pod, node, cache);
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

    private GreedyAlgorithm greedyAlgorithm;
    private Cache cache;

    public FakeScheduler() {
        super();
        cache = new FakeCache();
        // TODO 自定义策略测试
        greedyAlgorithm = new DefaultGreedyAlgorithm();
    }

    /**
     * 每轮调度从待调度队列取出调度请求列表，在此执行调度
     *
     * @param schedulingRequests pod调度请求列表 必须非null非空没有null元素
     */
    @Override
    public void schedule(List<Pod> schedulingRequests) {
        // 1. 更新缓存监控数据
        cache.fetchCacheData();
        // 2. 获取应用画像信息
//        getPortrait(schedulingRequests);
        // 3. 预排序
        List<Pod> sortedPods = greedyAlgorithm.presort(schedulingRequests, cache);
        // 4. 逐个处理待调度pod
        for (int i = 0; i < sortedPods.size(); i++) {
            // 调度本轮最后一个pod后，不需再更新缓存
            if (i == sortedPods.size() - 1) {
                scheduleOne(sortedPods.get(i), false);
            } else {
                scheduleOne(sortedPods.get(i), true);
            }
        }
    }

    private void scheduleOne(Pod pod, boolean ifUpdateCache) {
        // 预选
        List<Node> predicatedNodes = greedyAlgorithm.predicates(pod, cache);
        // 优选
        List<HostPriority> hostPriorityList = greedyAlgorithm.priorities(pod, predicatedNodes, cache);
        // 挑选节点
        HostPriority selectedHost = greedyAlgorithm.selectHost(hostPriorityList, cache);
        // 调用调度执行器，只发送一个host
        scheduleExecute(pod, selectedHost.getHost());
        // 修改缓存
        if (ifUpdateCache) {
            cache.updateCache(pod, selectedHost.getHost());
        }
    }

    private void scheduleExecute(Pod pod, String host) {
        System.out.println("Execute: pod=" + DOUtils.getPodFullName(pod) + ", host=" + host);
    }
}
