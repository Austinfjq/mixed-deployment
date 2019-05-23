package cn.harmonycloud.schedulingalgorithm.presort.impl;

import static org.junit.Assert.assertTrue;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.basic.FakeCache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
public class DecreasingSortRuleTest {

    private DecreasingSortRule decreasingSortRule = new DecreasingSortRule();

    @Test
    public void decreasingSortRuleTest() {
        Cache cache = new FakeCache();
        cache.fetchSingleCacheData();
        List<Pod> pods = new ArrayList<>();
        Pod pod;
        pod = new Pod(1, "wordpress", "wordpress-mysql");
        pods.add(pod);
        pod = new Pod(1, "wordpress", "wordpress-wp");
        pods.add(pod);
        pod = new Pod(2, "wordpress", "wordpress-wp");
        pods.add(pod);
        pod = new Pod(2, "hadoop", "hadoop-datanode");
        pods.add(pod);
        cache.getPortrait(pods);
        System.out.println("预处理前：" + pods.stream().map(p -> p.getOperation() + " " + p.getServiceName()).collect(Collectors.toList()));
        List<Pod> sortedPods = decreasingSortRule.sort(pods, cache);
        System.out.println("预处理后：" + sortedPods.stream().map(p -> p.getOperation() + " " + p.getServiceName()).collect(Collectors.toList()));
        System.out.println("Done.");
    }
}
