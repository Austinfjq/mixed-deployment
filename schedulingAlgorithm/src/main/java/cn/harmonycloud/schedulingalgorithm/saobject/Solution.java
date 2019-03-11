package cn.harmonycloud.schedulingalgorithm.saobject;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {
    private static final DefaultGreedyAlgorithm defaultGreedyAlgorithm = new DefaultGreedyAlgorithm();
    private static final Random random = new Random();
    private Cache cache;
    private List<Pod> pods;
    private List<String> hosts;
    private Integer score;

    public Solution(Cache cache, List<Pod> pods, List<String> hosts) {
        this.cache = cache;
        this.pods = pods;
        this.hosts = hosts;
    }

    public static Solution getRandomSolution(Cache cache, List<Pod> pods) {
        // 为pod随机生成符合predicate rules的可行解
        List<String> hosts = new ArrayList<>();
        for (Pod pod : pods) {
            String host = getRandomNode(pod, cache);
            hosts.add(host);
        }
        return new Solution(cache, pods, hosts);
    }

    private static String getRandomNode(Pod pod, Cache cache) {
        // 为pod随机生成符合predicate rules的node
        int nodeNum = cache.getNodeList().size();
        int tryTimes = 0;
        Set<Node> failedSet = null;
        while (tryTimes <= nodeNum) {
            Node randomNode = null;
            while (randomNode == null || (failedSet != null && failedSet.contains(randomNode))) {
                // 随机生成节点，避开之前生成过的失败节点
                randomNode = cache.getNodeList().get(random.nextInt(nodeNum));
                tryTimes++;
            }
            // 测试预选规则
            boolean predicateResult = defaultGreedyAlgorithm.runAllPredicates(pod, randomNode, cache);
            if (predicateResult) {
                return randomNode.getNodeName();
            } else {
                // 每次失败后，加进失败节点集
                if (failedSet == null) {
                    failedSet = new HashSet<>();
                }
                failedSet.add(randomNode);
            }
        }
        // 循环较多次数仍未找到，可用节点比例很低或者为0，有必要对所有未测试节点执行预选，避免卡在生成随机节点的循环里
        return getRandomNodeByTraversal(pod, cache, failedSet);
    }

    private static String getRandomNodeByTraversal(Pod pod, Cache cache, Set<Node> failedSet) {
        List<Node> nodes = cache.getNodeList().stream()
                .filter(n -> !failedSet.contains(n))
                .filter(node -> defaultGreedyAlgorithm.runAllPredicates(pod, node, cache))
                .collect(Collectors.toList());
        if (nodes.isEmpty()) {
            return null;
        } else {
            return nodes.get(random.nextInt(nodes.size())).getNodeName();
        }
    }

    /**
     * Solution 给随机一个pod，随机换一个node
     * @return 邻近解，hosts可能与this.hosts相同
     */
    public Solution neighbour() {
        List<String> neighbour = new ArrayList<>(this.hosts);
        int randomInt = random.nextInt(pods.size());
        String node = getRandomNode(pods.get(randomInt), cache);
        neighbour.set(randomInt, node);
        return new Solution(cache, pods, neighbour);
    }

    public static int getDeltaScore(Solution oldS, Solution newS) {
        // TODO
        return 0;
    }

    public int getScore() {
        if (score == null) {
            // TODO calculate score and store it
        }
        return score;
    }

    public List<String> getHosts() {
        return hosts;
    }
}
