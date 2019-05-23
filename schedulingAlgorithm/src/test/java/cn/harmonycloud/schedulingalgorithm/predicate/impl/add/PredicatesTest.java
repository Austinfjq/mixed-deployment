package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.basic.FakeCache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PredicatesTest {
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


        List<PredicateRule> predicateRules = new ArrayList<>();
        predicateRules.add(new CheckNodeConditionPredicate());
        predicateRules.add(new PodFitsResourcesPredicate());
        predicateRules.add(new PodFitsHostPortsPredicate());
        predicateRules.add(new PodMatchNodeSelectorPredicate());
        predicateRules.add(new PodToleratesNodeTaintsPredicate());
//        弃用predicateRules.add(new CheckVolumeBindingPredicate());
        predicateRules.add(new CheckNodeMemoryPressurePredicate());
        predicateRules.add(new CheckNodePIDPressurePredicate());
        predicateRules.add(new CheckNodeDiskPressurePredicate());
        predicateRules.add(new MatchInterPodAffinityPredicate());

        for (PredicateRule rule : predicateRules) {
            cache.getNodeList().forEach(node -> {
                boolean res = rule.predicate(pod, node, cache);
                if (!res) {
                    LOGGER.info(rule.getClass().getName() + node.getNodeName() + " predicate false for " + DOUtils.getServiceFullName(pod) + " in " + RuleUtil.getLastName(rule.toString()));
                } else {
                    LOGGER.info(rule.getClass().getName() + node.getNodeName() + " predicate success for " + DOUtils.getServiceFullName(pod));
                }
            });
        }
    }
}
