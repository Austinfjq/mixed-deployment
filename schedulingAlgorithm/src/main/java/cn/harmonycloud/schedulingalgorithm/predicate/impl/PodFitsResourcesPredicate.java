package cn.harmonycloud.schedulingalgorithm.predicate.impl;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;

public class PodFitsResourcesPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        Service service = cache.getServiceMap().get(DOUtils.getServiceFullName(pod));
        // TODO search in prometheus: kube_node_status_allocatable{node="pc"}

        // if len(nodeInfo.Pods())+1 > allowedPodNumber return false;

        // if podRequest.MilliCPU == 0 && podRequest.Memory == 0 return true;

        // if allocatable.MilliCPU < podRequest.MilliCPU+nodeInfo.RequestedResource().MilliCPU return false;

        // if allocatable.Memory < podRequest.Memory+nodeInfo.RequestedResource().Memory return false;

        return true;
    }
}
