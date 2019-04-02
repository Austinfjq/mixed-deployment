package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Resource;
import cn.harmonycloud.schedulingalgorithm.priority.DefaultPriorityRule;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

public class RequestedPriority implements DefaultPriorityRule {
    private int operation;

    public RequestedPriority(int operation) {
        this.operation = operation;
    }

    @Override
    public Integer priority(Pod pod, Node node, Cache cache) {
        if (node == null) {
            return 0;
        }
        Resource allocatable = RuleUtil.getNodeAllocatableResource(node);
        Resource requested = RuleUtil.getRequestedAfterOp(pod, node, operation);
        return (int) resourceScorer(requested, allocatable);
    }

//    @Override
//    public int getNodeMultiPriority(Node node, Map<String, List<Pod>> hostPodMap, Cache cache) {
//        Resource finalResource = RuleUtil.getNodeFinalResource(node, hostPodMap);
//        Resource allocatable = RuleUtil.getNodeAllocatableResource(node);
//        return (int) resourceScorer(finalResource, allocatable);
//    }

    private long resourceScorer(Resource requested, Resource allocatable) {
        return (requestedScore(requested.getMilliCPU(), allocatable.getMilliCPU()) +
                requestedScore(requested.getMemory(), allocatable.getMemory())) / 2;
    }

    private long requestedScore(long requested, long capacity) {
        // leastRequestedScore
        if (operation == Constants.OPERATION_ADD) {
            if (capacity == 0) {
                return 0;
            }
            if (requested > capacity) {
                return 0;
            }
            return ((capacity - requested) * GlobalSetting.PRIORITY_MAX_SCORE) / capacity;
        }
        // mostRequestedScore
        else {
            if (requested < 0) {
                return 0;
            }
            if (capacity == 0) {
                return GlobalSetting.PRIORITY_MAX_SCORE;
            }
            if (requested > capacity) {
                return GlobalSetting.PRIORITY_MAX_SCORE;
            }
            return (requested * GlobalSetting.PRIORITY_MAX_SCORE) / capacity;
        }
    }
}
