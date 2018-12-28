package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;

public class CheckNodeMemoryPressurePredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // Check if a pod can be scheduled on a node reporting memory pressure condition.
        // Currently, no BestEffort should be placed on a node under memory pressure as it gets automatically evicted by kubelet.
        // BestEffort qos类型的pod不允许被调度到内存压力节点上，因为会被kubelet自动驱逐
        String condition = node.getCondition();
        // TODO 需要拿到v1.Pod.Spec.Containers，读取container.Resources.Requests来实现isPodBestEffort(pod)
        boolean podBestEffort = false; // TODO
        if (!podBestEffort) {
            return true;
        }

        boolean memoryPressure = false; // TODO how to parse condition
        if (memoryPressure) {
            return false;
        }
        return true;
    }
}
