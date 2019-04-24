package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.priority.DefaultPriorityRule;

import java.util.List;

import com.google.gson.Gson;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NodePreferAvoidPodsPriority implements DefaultPriorityRule {
    private static final Gson gson = new Gson();
    private int operation;

    public NodePreferAvoidPodsPriority(int op) {
        operation = op;
    }

    @Override
    public Integer priority(Pod pod, Node node, Cache cache) {
        if (node == null) {
            return 0;
        }
        int score = calculateNodePreferAvoidPodsPriorityMap(pod, node, cache);
        if (operation == Constants.OPERATION_ADD) {
            return score;
        } else {
            return GlobalSetting.PRIORITY_MAX_SCORE - score;
        }
    }

    private int calculateNodePreferAvoidPodsPriorityMap(Pod pod, Node node, Cache cache) {
//        var controllerRef *metav1.OwnerReference
//        TO DO controllerRef = metav1.GetControllerOf(pod)
//        OwnerReference controllerRef = new OwnerReference();
//        if (controllerRef == null) return GlobalSetting.PRIORITY_MAX_SCORE;
//        AvoidPods avoids = getAvoidPodsFromNodeAnnotations(new HashMap<>()/*TO DO node.Annotations*/);
        AvoidPods avoids = gson.fromJson(node.getPreferAvoidPodsAnnotationKey(), AvoidPods.class);
        if (avoids == null) {
            return GlobalSetting.PRIORITY_MAX_SCORE;
        }
        for (PreferAvoidPodsEntry avoid : avoids.preferAvoidPods) {
//            if (Objects.equals(avoid.podSignature.podController.kind, controllerRef.kind) && Objects.equals(avoid.podSignature.podController.uid, controllerRef.uid)) {
//                return 0;
//            }
//            if (Objects.equals(avoid.podSignature.podController.kind, pod.getResourceKind()) && Objects.equals(avoid.podSignature.podController.uid, controllerRef.uid)) {
//                return 0;
//            }
        }
        return GlobalSetting.PRIORITY_MAX_SCORE;
    }

    private static final String preferAvoidPodsAnnotationKey = "scheduler.alpha.kubernetes.io/preferAvoidPods";

    private AvoidPods getAvoidPodsFromNodeAnnotations(Map<String, String> annotations) {
        AvoidPods avoidPods = new AvoidPods();
        if (!annotations.isEmpty() && annotations.containsKey(preferAvoidPodsAnnotationKey)) {
            try {
                avoidPods = gson.fromJson(annotations.get(preferAvoidPodsAnnotationKey), AvoidPods.class);
            } catch (Exception e) {
                return null;
            }
        }
        return avoidPods;
    }

    private class AvoidPods {
        private List<PreferAvoidPodsEntry> preferAvoidPods;

        public List<PreferAvoidPodsEntry> getPreferAvoidPods() {
            return preferAvoidPods;
        }

        public void setPreferAvoidPods(List<PreferAvoidPodsEntry> preferAvoidPods) {
            this.preferAvoidPods = preferAvoidPods;
        }
    }

    private class PreferAvoidPodsEntry {
        private PodSignature podSignature;

        public PodSignature getPodSignature() {
            return podSignature;
        }

        public void setPodSignature(PodSignature podSignature) {
            this.podSignature = podSignature;
        }
    }

    private class PodSignature {
        private OwnerReference podController;

        public OwnerReference getPodController() {
            return podController;
        }

        public void setPodController(OwnerReference podController) {
            this.podController = podController;
        }
    }

    private class OwnerReference {
        private String kind;
        private String uid;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

    }
}
