package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PodFitsResourcesPredicate implements PredicateRule {
    private final static Logger LOGGER = LoggerFactory.getLogger(PodFitsResourcesPredicate.class);

    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // in prometheus: kube_node_status_allocatable{node="pc"}

        if (1 + cache.getNodeMapPodList().get(node.getNodeName()).size() > node.getAllocatablePods().longValue()) {
            if (GlobalSetting.LOG_DETAIL) {
                LOGGER.info("allocatable pods not enough");
            }
            return false;
        }
        if (pod.getCpuRequest().equals(0D) && pod.getMemRequest().equals(0D)) {
            return true;
        }
        if (node.getAllocatableCpuCores() < pod.getCpuRequest() + node.getCpuUsage()) {
            if (GlobalSetting.LOG_DETAIL) {
                LOGGER.info("cpu cores not enough");
            }
            return false;
        }
        if (node.getAllocatableMem() < pod.getMemRequest() + node.getMemUsage()) {
            if (GlobalSetting.LOG_DETAIL) {
                LOGGER.info("memory not enough");
            }
            return false;
        }

        return true;
    }
}
