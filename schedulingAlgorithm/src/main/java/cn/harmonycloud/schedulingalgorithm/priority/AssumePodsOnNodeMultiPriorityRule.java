//package cn.harmonycloud.schedulingalgorithm.priority;
//
//import cn.harmonycloud.schedulingalgorithm.basic.Cache;
//import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
//import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
//import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public interface AssumePodsOnNodeMultiPriorityRule extends DefaultMultiPriorityRule {
//    /**
//     * cannot be paralleled
//     */
//    default int getNodeMultiPriority(Node node, Map<String, List<Pod>> hostPodMap, Cache cache) {
//        List<Pod> backupOfNodePodList = cache.getNodeMapPodList().get(node.getNodeName());
//        List<Pod> assumePodList = new ArrayList<>(backupOfNodePodList);
//        if (hostPodMap.containsKey(node.getNodeName())) {
//            List<Pod> pods1 = hostPodMap.get(node.getNodeName());
//            assumePodList.addAll(pods1);
//            cache.getNodeMapPodList().put(node.getNodeName(), assumePodList);
//            int priority = getAssumePodsOnNodePriority(node, hostPodMap, cache);
//            cache.getNodeMapPodList().put(node.getNodeName(), backupOfNodePodList);
//            return priority;
//        } else {
//            return 0;
//        }
//    }
//
//    int getAssumePodsOnNodePriority(Node node, Map<String, List<Pod>> hostPodMap, Cache cache);
//}
