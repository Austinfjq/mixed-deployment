package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Resource;
import cn.harmonycloud.schedulingalgorithm.priority.DefaultPriorityRule;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

import java.util.List;

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

    @Override
    public Integer multiPriority(List<Pod> pods, List<String> hosts, Cache cache) {
        // use operation of Pod instead of Rule
        // TODO multi
        return null;
    }

    @Override
    public Integer deltaMultiPriority(List<Pod> pods, List<String> oldHosts, List<String> newHosts, Cache cache) {
        // use operation of Pod instead of Rule
        // TODO multi
        return null;
    }

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
            return ((capacity - requested) * Constants.PRIORITY_MAX_SCORE) / capacity;
        }
        // mostRequestedScore
        else {
            if (requested < 0) {
                return 0;
            }
            if (capacity == 0) {
                return Constants.PRIORITY_MAX_SCORE;
            }
            if (requested > capacity) {
                return Constants.PRIORITY_MAX_SCORE;
            }
            return (requested * Constants.PRIORITY_MAX_SCORE) / capacity;
        }
    }
}
