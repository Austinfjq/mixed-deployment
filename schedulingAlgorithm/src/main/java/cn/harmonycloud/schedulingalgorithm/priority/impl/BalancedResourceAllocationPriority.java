package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Resource;
import cn.harmonycloud.schedulingalgorithm.priority.DefaultPriorityRule;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BalancedResourceAllocationPriority implements DefaultPriorityRule {
    private int operation;

    public BalancedResourceAllocationPriority(int operation) {
        this.operation = operation;
    }

    @Override
    public Integer priority(Pod pod, Node node, Cache cache) {
        if (node == null) {
            return 0;
        }
        Resource allocatable = RuleUtil.getNodeAllocatableResource(node);
        Resource requested = RuleUtil.getRequestedAfterOp(pod, node, operation);
        int score;
        // do not check any more
        // TO - DO Check if the pod has volumes and this could be added to scorer function for balanced resource allocation.
//        if (len(pod.Spec.Volumes) >= 0 && utilfeature.DefaultFeatureGate.Enabled(features.BalanceAttachedNodeVolumes) && nodeInfo.TransientInfo != nil) {
//            score = balancedResourceScorer(requested, allocatable, true, nodeInfo.TransientInfo.TransNodeInfo.RequestedVolumes, nodeInfo.TransientInfo.TransNodeInfo.AllocatableVolumesCount);
//        } else {
        score = balancedResourceScorer(requested, allocatable, false, 0, 0);
//        }
        return score;
    }

    private int balancedResourceScorer(Resource requested, Resource allocatable, boolean includeVolumes, int requestedVolumes, int allocatableVolumes) {
        double cpuFraction = fractionOfCapacity(requested.getMilliCPU(), allocatable.getMilliCPU());
        double memoryFraction = fractionOfCapacity(requested.getMemory(), allocatable.getMemory());
        // This to find a node which has most balanced CPU, memory and volume usage.
//        if (includeVolumes /*&& utilfeature.DefaultFeatureGate.Enabled(features.BalanceAttachedNodeVolumes)*/ && allocatableVolumes > 0) {
//            double volumeFraction = (double) requestedVolumes / (double) allocatableVolumes;
//            if (cpuFraction >= 1 || memoryFraction >= 1 || volumeFraction >= 1) {
//                // if requested >= capacity, the corresponding host should never be preferred when adding and should be preferred when deleting.
//                return operation == Constants.OPERATION_ADD ? 0 : GlobalSetting.PRIORITY_MAX_SCORE;
//            }
//            // Compute variance for all the three fractions.
//            double mean = (cpuFraction + memoryFraction + volumeFraction) / 3;
//            double variance = (((cpuFraction - mean) * (cpuFraction - mean)) + ((memoryFraction - mean) * (memoryFraction - mean)) + ((volumeFraction - mean) * (volumeFraction - mean))) / 3;
//            return (int) ((1 - variance) * GlobalSetting.PRIORITY_MAX_SCORE);
//        }
        if (cpuFraction >= 1 || memoryFraction >= 1) {
            return 0;
        }
        // Upper and lower boundary of difference between cpuFraction and memoryFraction are -1 and 1
        // respectively. Multiplying the absolute value of the difference by 10 scales the value to
        // 0-10 with 0 representing well balanced allocation and 10 poorly balanced. Subtracting it from
        // 10 leads to the score which also scales from 0 to 10 while 10 representing well balanced.
        double diff = Math.abs(cpuFraction - memoryFraction);
        return (int) ((1 - diff) * GlobalSetting.PRIORITY_MAX_SCORE);
    }

    private double fractionOfCapacity(long requested, long capacity) {
        if (capacity == 0) {
            return 1;
        }
        return (double) requested / (double) capacity;
    }

//    @Override
//    public int getNodeMultiPriority(Node node, Map<String, List<Pod>> hostPodMap, Cache cache) {
//        Resource finalResource = RuleUtil.getNodeFinalResource(node, hostPodMap);
//        Resource allocatable = RuleUtil.getNodeAllocatableResource(node);
//        return balancedResourceScorer(finalResource, allocatable, false, 0, 0);
//    }
}
