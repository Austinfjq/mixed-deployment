package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;

public class PodFitsResourcesPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // in prometheus: kube_node_status_allocatable{node="pc"}

        if (1 + cache.getNodeMapPodList().get(node.getNodeName()).size() > node.getAllocatablePods().longValue()) {
            return false;
        }
        if (pod.getCpuRequest().equals(0D) && pod.getMemRequest().equals(0D)) {
            return true;
        }
        if (node.getAllocatableCpuCores() < pod.getCpuRequest() + node.getCpuUsage()) {
            return false;
        }
        if (node.getAllocatableMem() < pod.getMemRequest() + node.getMemUsage()) {
            return false;
        }

        return true;
    }
}
