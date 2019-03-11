package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
//import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.priority.DefaultPriorityRule;

import java.util.List;
//import net.sf.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;

public class NodePreferAvoidPodsPriority implements DefaultPriorityRule {
//    private int operation;
//    public NodePreferAvoidPodsPriority(int op) {
//        operation = op;
//    }
    @Override
    public Integer priority(Pod pod, Node node, Cache cache) {
        // 已弃用
        return 0;
//        if (node == null) {
//            return 0;
//        }
//        if (operation == Constants.OPERATION_ADD) {
//            return calculateNodePreferAvoidPodsPriorityMap(pod, node, cache);
//        } else {
//            return Constants.PRIORITY_MAX_SCORE - calculateNodePreferAvoidPodsPriorityMap(pod, node, cache);
//        }
    }

    @Override
    public Integer multiPriority(List<Pod> pods, List<String> hosts, Cache cache) {
        return 0;
    }

    @Override
    public Integer deltaMultiPriority(List<Pod> pods, List<String> oldHosts, List<String> newHosts, Cache cache) {
        return 0;
    }

//    private int calculateNodePreferAvoidPodsPriorityMap(Pod pod, Node node, Cache cache) {
////        var controllerRef *metav1.OwnerReference
////        TO DO controllerRef = metav1.GetControllerOf(pod)
//        OwnerReference controllerRef = new OwnerReference();
////        if (controllerRef == null) return Constants.PRIORITY_MAX_SCORE;
//        AvoidPods avoids = getAvoidPodsFromNodeAnnotations(new HashMap<>()/*TO DO node.Annotations*/);
//        if (avoids == null) {
//            return Constants.PRIORITY_MAX_SCORE;
//        }
//        for (PreferAvoidPodsEntry avoid : avoids.preferAvoidPods) {
//            if (Objects.equals(avoid.podSignature.podController.kind, controllerRef.kind) && Objects.equals(avoid.podSignature.podController.uid, controllerRef.uid)) {
//                return 0;
//            }
//        }
//        return Constants.PRIORITY_MAX_SCORE;
//    }
//
//    private static final String preferAvoidPodsAnnotationKey = "scheduler.alpha.kubernetes.io/preferAvoidPods";
//
//    private AvoidPods getAvoidPodsFromNodeAnnotations(Map<String, String> annotations) {
//        AvoidPods avoidPods = new AvoidPods();
//        if (!annotations.isEmpty() && annotations.containsKey(preferAvoidPodsAnnotationKey)) {
//            try {
//                avoidPods = (AvoidPods) JSONObject.toBean(JSONObject.fromObject(annotations.get(preferAvoidPodsAnnotationKey)), AvoidPods.class);
//            } catch (Exception e) {
//                return null;
//            }
//        }
//        return avoidPods;
//    }
//
//    class AvoidPods {
//        List<PreferAvoidPodsEntry> preferAvoidPods;
//    }
//
//    class PreferAvoidPodsEntry {
//        PodSignature podSignature;
//    }
//
//    class PodSignature {
//        OwnerReference podController;
//    }
//
//    class OwnerReference {
//        String kind;
//        String uid;
//    }
}
