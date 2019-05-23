package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.basic.FakeCache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.CheckNodeConditionPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.CheckNodeDiskPressurePredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.CheckNodeMemoryPressurePredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.CheckNodePIDPressurePredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.MatchInterPodAffinityPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.PodFitsHostPortsPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.PodFitsResourcesPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.PodMatchNodeSelectorPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.PodToleratesNodeTaintsPredicate;
import cn.harmonycloud.schedulingalgorithm.predicate.impl.add.PredicatesTest;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRuleConfig;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PriorityTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(PredicatesTest.class);

    @Test
    public void checkNodeConditionPredicateTest() {
        Cache cache = new FakeCache();
        cache.fetchSingleCacheData();
        List<Pod> pods = new ArrayList<>();
        Pod pod;
        pod = new Pod(1, "wordpress", "wordpress-mysql");
        pods.add(pod);
        cache.getPortrait(pods);

        new DefaultGreedyAlgorithm().priorities(pod, cache.getNodeList(), cache);
    }
}
