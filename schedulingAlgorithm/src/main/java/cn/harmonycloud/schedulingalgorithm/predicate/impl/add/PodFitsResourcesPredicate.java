package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;

import java.util.Objects;

public class PodFitsResourcesPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // in prometheus: kube_node_status_allocatable{node="pc"}

        if (1 + cache.getNodeMapPodList().get(node.getNodeName()).size() > Long.valueOf(node.getAllocatablePods())) {
            return false;
        }
        if (Long.valueOf(pod.getCpuRequest()).equals(0L) && Long.valueOf(pod.getMemRequest()).equals(0L)) {
            return true;
        }
        if (Long.valueOf(node.getAllocatableCpuCores()) < Long.valueOf(pod.getCpuRequest()) + Long.valueOf(node.getCpuUsage())) {
            return false;
        }
        if (Long.valueOf(node.getAllocatableMem()) < Long.valueOf(pod.getMemRequest()) + Long.valueOf(node.getMemUsage())) {
            return false;
        }

        return true;
    }
}
