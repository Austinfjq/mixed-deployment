package cn.harmonycloud.schedulingalgorithm.selecthost.impl;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.selecthost.SelectHostRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RoundRobinSelectHighest implements SelectHostRule {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultGreedyAlgorithm.class);
    private static int lastNodeIndex = 0;

    @Override
    public HostPriority selectHost(List<HostPriority> hostPriorityList) {
        if (hostPriorityList.isEmpty()) {
            return null;
        }
        List<Integer> maxScoreIndices = findMaxScores(hostPriorityList);
        int selectedIndex = lastNodeIndex % maxScoreIndices.size();
        lastNodeIndex++;
        return hostPriorityList.get(maxScoreIndices.get(selectedIndex));
    }

    private List<Integer> findMaxScores(List<HostPriority> hostPriorityList) {
        int maxScore = Integer.MIN_VALUE;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < hostPriorityList.size(); i++) {
            Integer score = hostPriorityList.get(i).getScore();
            if (GlobalSetting.LOG_DETAIL) {
                LOGGER.info("score sum=" + score + ", node=" + hostPriorityList.get(i).getHostname());
            }
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
