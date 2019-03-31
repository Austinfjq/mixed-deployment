//package cn.harmonycloud.schedulingalgorithm.priority;
//
//import cn.harmonycloud.schedulingalgorithm.basic.Cache;
//import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
//import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
//import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//public interface DefaultMultiPriorityRule extends MultiPriorityRule {
//    int getNodeMultiPriority(Node node, Map<String, List<Pod>> hostPodMap, Cache cache);
//
//    default Integer multiPriority(List<Pod> pods, List<String> hosts, Cache cache) {
//        // use operation of Pod instead of Rule
//        // TODO put this line into parameter
//        Map<String, List<Pod>> hostPodMap = RuleUtil.getHostsToPodsMap(pods, hosts);
//        return cache.getNodeList().stream().mapToInt(node -> getNodeMultiPriority(node, hostPodMap, cache)).sum();
//    }
//
//    default Integer deltaMultiPriority(List<Pod> pods, List<String> oldHosts, List<String> newHosts, Cache cache) {
//        // use operation of Pod instead of Rule
//        Set<String> relativeNodes = new HashSet<>(oldHosts);
//        relativeNodes.addAll(newHosts);
//        Map<String, List<Pod>> hostPodMapOld = RuleUtil.getHostsToPodsMap(pods, oldHosts);
//        Map<String, List<Pod>> hostPodMapNew = RuleUtil.getHostsToPodsMap(pods, newHosts);
//        int finalScore = 0;
//        for (String node : relativeNodes) {
//            int deltaNodeScore = getNodeMultiPriority(cache.getNodeMap().get(node), hostPodMapNew, cache) - getNodeMultiPriority(cache.getNodeMap().get(node), hostPodMapOld, cache);
//            finalScore += deltaNodeScore;
//        }
//        return finalScore;
//    }
//}
