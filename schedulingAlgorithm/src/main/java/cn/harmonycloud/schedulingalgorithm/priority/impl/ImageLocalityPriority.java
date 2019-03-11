package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
//import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.priority.DefaultPriorityRule;

import java.util.List;
import java.util.Map;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

public class ImageLocalityPriority implements DefaultPriorityRule {
    @Override
    public Integer priority(Pod pod, Node node, Cache cache) {
        // 已弃用
        return 0;
//        return imageLocalityPriorityMap(pod, node, cache);
    }

//    private int imageLocalityPriorityMap(Pod pod, Node node, Cache cache) {
//        if (node == null) {
//            return 0;
//        }
//        List<Container> containers = new ArrayList<>();// TO DO get containers
//        int totalNumNodes = cache.getNodeMap().size();
//        return calculatePriority(sumImageScores(node, containers, totalNumNodes));
//    }
//
//    private static final int mb = 1024 * 1024;
//    private static final int minThreshold = 23 * mb;
//    private static final int maxThreshold = 1000 * mb;
//
//    private int calculatePriority(int sumScores) {
//        if (sumScores < minThreshold) {
//            sumScores = minThreshold;
//        } else if (sumScores > maxThreshold) {
//            sumScores = maxThreshold;
//        }
//        return Constants.PRIORITY_MAX_SCORE * (sumScores - minThreshold) / (maxThreshold - minThreshold);
//    }
//
//    class Container {
//        String name;
//        String image;
//    }
//
//    class ImageStateSummary {
//        int size;
//        int numNodes;
//    }
//
//    private int sumImageScores(Node node, List<Container> containers, int totalNumNodes) {
//        int sum = 0;
//        Map<String, ImageStateSummary> imageStates = new HashMap<>(); // TO DO get imageStates of node : imageStates := nodeInfo.ImageStates()
//        for (Container container : containers) {
//            if (imageStates.containsKey(normalizedImageName(container.image))) {
//                sum += scaledImageScore(imageStates.get(normalizedImageName(container.image)), totalNumNodes);
//            }
//        }
//        return sum;
//    }
//
//    private static final String DefaultImageTag = "latest";
//
//    private String normalizedImageName(String name) {
//        if (name.lastIndexOf(":") <= name.lastIndexOf("/")) {
//            name = name + ":" + DefaultImageTag;
//        }
//        return name;
//    }
//
//    private int scaledImageScore(ImageStateSummary imageState, int totalNumNodes) {
//        double spread = (double) imageState.numNodes / (double) totalNumNodes;
//        return (int) ((double) imageState.size / spread);
//    }
}
