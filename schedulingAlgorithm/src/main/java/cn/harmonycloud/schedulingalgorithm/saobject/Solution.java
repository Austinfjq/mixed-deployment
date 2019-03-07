package cn.harmonycloud.schedulingalgorithm.saobject;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.priority.DefaultPriorityRule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Solution {
    private static final DefaultGreedyAlgorithm defaultGreedyAlgorithm = new DefaultGreedyAlgorithm();
    private static final Random random = new Random();
    private Cache cache;
    private List<Pod> pods;
    private List<String> hosts;

    public Solution(Cache cache, List<Pod> pods, List<String> hosts) {
        this.cache = cache;
        this.pods = pods;
        this.hosts = hosts;
    }

    public static Solution getRandomSolution(Cache cache, List<Pod> pods) {
        // 随机生成符合predicate rules的可行解
        List<String> hosts = new ArrayList<>();
        for (Pod pod : pods) {
            String host = findNode(pod, cache);
            hosts.add(host);
        }
        return new Solution(cache, pods, hosts);
    }

    private static String findNode(Pod pod, Cache cache) {
        // TODO 如何尽量避免调用runAllPredicates，但是又想
        long nodeNum = cache.getNodeList().size();
        Set<Long> failedSet = new HashSet<>();
        boolean ok = false;
        while (!ok) {
            int index = random.nextInt();
        }
        return null;
    }

    public static int getDeltaScore(Solution oldS, Solution newS) {
        // TODO
        return 0;
    }

    public Solution neighbour() {
        // TODO
        return null;
    }

}
