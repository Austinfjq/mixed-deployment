package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Container;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Quantity;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class CheckNodeMemoryPressurePredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // Check if a pod can be scheduled on a node reporting memory pressure condition.
        // Currently, no BestEffort should be placed on a node under memory pressure as it gets automatically evicted by kubelet.
        // BestEffort qos类型的pod不允许被调度到内存压力节点上，因为会被kubelet自动驱逐
        boolean podBestEffort = isPodBestEffort(pod); // TODO
        if (!podBestEffort) {
            return true;
        }

        String[] conditions = node.getCondition().split(",");
        String memoryPressureStr = conditions[0].trim();

        return !"true".equals(memoryPressureStr);
    }

    private boolean isPodBestEffort(Pod pod) {
        return allZero(pod.getCpuRequest(), pod.getMemRequest(), pod.getCpuLimit(), pod.getMemLimit());
//        for (Container container : pod.getContainers()) {
//            for (Map.Entry<String, Quantity> entry : container.getRequests().entrySet()) {
//                if (!isSupportedQoSComputeResource(entry.getKey())) {
//                    continue;
//                }
//                if (entry.getValue().isZero()){
//                    continue;
//                }
//                return false;
//            }
//            for (Map.Entry<String, Quantity> entry : container.getLimits().entrySet()) {
//                if (!isSupportedQoSComputeResource(entry.getKey())) {
//                    continue;
//                }
//                if (entry.getValue().isZero()){
//                    continue;
//                }
//                return false;
//            }
//        }
//        return true;
    }

    private boolean allZero(Double ... ds) {
        for (Double d : ds) {
            if (!Objects.equals(d, 0D)) {
                return false;
            }
        }
        return true;
    }

    private String[] supportedQoSComputeResources = new String[]{"cpu", "memory"};
    private boolean isSupportedQoSComputeResource(String name) {
        return Arrays.asList(supportedQoSComputeResources).contains(name);
    }
}
