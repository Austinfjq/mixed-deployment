package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Resource;
import cn.harmonycloud.schedulingalgorithm.priority.DefaultPriorityRule;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityUtil;

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
        // TODO allocatable resource from node cache
        Resource allocatable = new Resource();
        Resource requested = PriorityUtil.getRequestedAfterOp(pod, node, operation);
        return resourceScorer(requested, allocatable);
    }

    private Resource getNonZeroRequests(Pod pod) {
        Resource requested = new Resource();
        // TODO 合计pod下各个container需要的cpu mem，或者直接拿到
        return requested;
    }

    private int resourceScorer(Resource requested, Resource allocatable) {
        // TODO add more
        return (requestedScore(requested.getMilliCPU(), allocatable.getMilliCPU()) +
                requestedScore(requested.getMemory(), allocatable.getMemory())) / 2;
    }

    private int requestedScore(int requested, int capacity) {
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
