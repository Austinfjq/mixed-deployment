package cn.harmonycloud.schedulingalgorithm.selecthost.impl;

import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.selecthost.SelectHostRule;

import java.util.ArrayList;
import java.util.List;

public class RoundRobinSelectHighest implements SelectHostRule {
    private static int lastNodeIndex = 0;

    @Override
    public HostPriority selectHost(List<HostPriority> hostPriorityList) {
        if (hostPriorityList.isEmpty()) {
            return null;
            // TODO how to pend the pod
        }
        List<Integer> maxScoreIndices = findMaxScores(hostPriorityList);
        int selectedIndex = lastNodeIndex % maxScoreIndices.size();
        lastNodeIndex++;
        return hostPriorityList.get(maxScoreIndices.get(selectedIndex));
    }

    private List<Integer> findMaxScores(List<HostPriority> hostPriorityList) {
        double maxScore = Double.MIN_VALUE;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < hostPriorityList.size(); i++) {
            Double score = hostPriorityList.get(i).getWeightedScore();
            if (score != null) {
                if (score > maxScore) {
                    maxScore = score;
                    indices.clear();
                    indices.add(i);
                } else if (score == maxScore) {
                    indices.add(i);
                }
            }
        }
        return indices;
    }
}
