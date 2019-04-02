package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRule;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SelectorSpreadPriority implements PriorityRule {
    private static final double zoneWeighting = 2.0 / 3.0;
    private int operation;

    public SelectorSpreadPriority(int op) {
        operation = op;
    }

    @Override
    public List<Integer> priority(Pod pod, List<Node> nodes, Cache cache) {
        List<Integer> mapResult = (GlobalSetting.PARALLEL ? nodes.parallelStream() : nodes.stream())
                .map(node -> calculateSpreadPriorityMap(pod, node, cache))
                .collect(Collectors.toList());
        // reduce
        return calculateSpreadPriorityReduce(pod, nodes, cache, mapResult);
    }

    private int calculateSpreadPriorityMap(Pod pod, Node node, Cache cache) {
        if (node == null) {
            return 0;
        }
        // 直接匹配服务名，不用service.Spec.Selector
        List<Pod> nodePods = cache.getNodeMapPodList().get(node.getNodeName());
        return (int) nodePods.stream().filter(p -> p.getServiceName() != null && DOUtils.getServiceFullName(p).equals(DOUtils.getServiceFullName(pod))).count();
//        // TO-DO data: selectors of all services
//        List<Selector> selectors = new ArrayList<>();
//        // instead: pod所属的services, Controllers, replicaSets, StatefulSets，对每一个都：xxx -> selectors.add(new InternalSelector(xxx.Spec.Selector));
//        // k8s source code: selectors = getSelectors(pod, s.serviceLister, s.controllerLister, s.replicaSetLister, s.statefulSetLister)
//        if (selectors.isEmpty()) {
//            return 0;
//        }
//        int count = 0;
//        List<Pod> nodePods = cache.getNodeMapPodList().get(node.getNodeName());
//        for (Pod nodePod : nodePods) {
//            if (Objects.equals(nodePod.getNamespace(), pod.getNamespace())) {
//                continue;
//            }
//            // TO DO : existingPod.getDeletionTimestamp
////            if (nodePod.getDeletionTimestamp() != null) {
////                continue;
////            }
//            for (Selector selector : selectors) {
//                // TO DO existingPod.Labels
//                Map<String, String> labels = new HashMap<>();
//                if (selector.matches(labels)) {
//                    count++;
//                    break;
//                }
//            }
//        }
//        return count;
    }

    private List<Integer> calculateSpreadPriorityReduce(Pod pod, List<Node> nodes, Cache cache, List<Integer> mapResult) {
        Map<String, Integer> countsByZone = new HashMap<>();
        int maxCountByZone = 0;
        int maxCountByNodeName = 0;
        for (int i = 0; i < mapResult.size(); i++) {
            int count = mapResult.get(i);
            if (count > maxCountByNodeName) {
                maxCountByNodeName = count;
            }
            String zoneID = RuleUtil.getZoneKey(nodes.get(i));
            if (zoneID == null) {
                continue;
            }
            int old = countsByZone.getOrDefault(zoneID, 0);
            countsByZone.put(zoneID, old + count);
        }
        for (String zoneID : countsByZone.keySet()) {
            if (countsByZone.get(zoneID) > maxCountByZone) {
                maxCountByZone = countsByZone.get(zoneID);
            }
        }

        boolean haveZones = !countsByZone.isEmpty();
        for (int i = 0; i < mapResult.size(); i++) {
            double fScore = GlobalSetting.PRIORITY_MAX_SCORE;
            if (maxCountByNodeName > 0) {
                if (operation == Constants.OPERATION_ADD) {
                    fScore *= (maxCountByNodeName - mapResult.get(i)) / (double) maxCountByNodeName;
                } else {
                    fScore *= mapResult.get(i) / (double) maxCountByNodeName;
                }
            }
            if (haveZones) {
                String zoneID = RuleUtil.getZoneKey(nodes.get(i));
                if (zoneID != null) {
                    double zoneScore = GlobalSetting.PRIORITY_MAX_SCORE;
                    if (maxCountByZone > 0) {
                        if (operation == Constants.OPERATION_ADD) {
                            zoneScore = GlobalSetting.PRIORITY_MAX_SCORE * (maxCountByZone - countsByZone.get(zoneID)) / (double) maxCountByZone;
                        } else {
                            zoneScore = GlobalSetting.PRIORITY_MAX_SCORE * countsByZone.get(zoneID) / (double) maxCountByZone;
                        }
                    }
                    fScore = (fScore * (1.0 - zoneWeighting)) + (zoneWeighting * zoneScore);
                }
            }
            mapResult.set(i, (int) fScore);
        }
        return mapResult;
    }
}
