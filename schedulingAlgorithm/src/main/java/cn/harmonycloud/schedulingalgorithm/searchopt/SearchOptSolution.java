package cn.harmonycloud.schedulingalgorithm.searchopt;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithmIgnoreResourcePriority;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithmOnlyResourcePriority;
import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 搜索优化算法中的解
 * 暂未支持多线程
 */
public class SearchOptSolution {
    private static final DefaultGreedyAlgorithm defaultGreedyAlgorithm = new DefaultGreedyAlgorithm();
    private static final GreedyAlgorithmIgnoreResourcePriority greedyAlgorithmIgnoreResourcePriority = new GreedyAlgorithmIgnoreResourcePriority();
    private static final GreedyAlgorithmOnlyResourcePriority greedyAlgorithmOnlyResourcePriority = new GreedyAlgorithmOnlyResourcePriority();
    private static final Random random = new Random();
    /**
     * 待调度的pods
     */
    private List<Pod> pods;
    /**
     * 为每个pod分配的节点的名字
     */
    private List<String> hosts;
    /**
     * 本解中分配节点发生变化的pod的index
     * 如果是初始解，indexOfChange==null
     */
    private Integer indexOfChange;
    /**
     * neighbor生成的新Solution时，假定第k个pod的host变化了，
     * 那么cacheBeforeChange表示调度了除第k个以外所有pod之后的缓存
     * 此缓存再调度了第k个pod到指定节点上之后，就是Solution最后的缓存
     * 为了方便计算deltaScore
     */
    private Cache cacheBeforeChange;

    public SearchOptSolution(Cache cacheBeforeChange, List<Pod> pods, List<String> hosts, Integer indexOfChange) {
        this.cacheBeforeChange = cacheBeforeChange;
        this.pods = pods;
        this.hosts = hosts;
        this.indexOfChange = indexOfChange;
    }

    /**
     * 生成初始解
     */
    public static SearchOptSolution getInitialSolution(Cache cache, List<Pod> pods) {
        // 使用隔离的新缓存
        Cache laterCache = cache.clone();
        // 为pod随机生成符合predicate rules的可行解
        List<String> hosts = new ArrayList<>();
        for (Pod pod : pods) {
            String host = getRandomNode(pod, laterCache);
            if (host == null) {
                return null;
            }
            laterCache.updateCache(pod, host);
            hosts.add(host);
        }
        return new SearchOptSolution(null, pods, hosts, null);
    }

    public static SearchOptSolution getInitialShuffleSolution(Cache cache, List<Pod> pods) {
        // 使用隔离的新缓存
        Cache laterCache = cache.clone();
        // 为pod随机生成符合predicate rules的可行解，随机改变调度的顺序
        List<Pod> shuffledPods = new ArrayList<>(pods);
        Collections.shuffle(shuffledPods);
        Map<Pod, String> map = new HashMap<>();
        for (Pod pod : shuffledPods) {
            String host = getRandomNode(pod, laterCache);
            if (host == null) {
                return null;
            }
            laterCache.updateCache(pod, host);
            map.put(pod, host);
        }
        // 仍按原来的顺序放置hosts
        List<String> hosts = new ArrayList<>();
        pods.forEach(p -> hosts.add(map.get(p)));
        return new SearchOptSolution(null, pods, hosts, null);
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
                if (tryTimes > nodeNum) {
                    break;
                }
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
     * SearchOptSolution 给随机一个pod，随机换一个node
     *
     * @return 邻近解，它的hosts可能与this.hosts相同
     */
    public SearchOptSolution neighbour(Cache initialCache) {
        int randomInt = random.nextInt(pods.size());
        // 重新生成缓存
        Cache cache = initialCache.clone();
        for (int i = 0; i < pods.size(); i++) {
            if (i != randomInt) {
                cache.updateCache(pods.get(i), hosts.get(i));
            }
        }
        // 生成新节点，不计入cacheBeforeChange
        String node = getRandomNode(pods.get(randomInt), cache);
        // 修改节点列表，生成解
        List<String> neighbour = new ArrayList<>(this.hosts);
        neighbour.set(randomInt, node);
        return new SearchOptSolution(cache, pods, neighbour, randomInt);
    }

    /**
     * 计算评分差
     * 严格地说，分数差应该是两个调度方案的每个分配的得分之和的差
     * 这里简单计算：如果newS变异的是第k个pod，假定调度顺序是先调度别的所有pod，最后调度第k个pod，从这个调整过的顺序来看，oldS和newS只有最后一次调度时分数会不同，只计算这个部分。（实际上，同一个调度方案，调度分配相同，调度顺序不同，每个分配的得分之和可能会有不同）
     *
     * @param oldS 上次的解
     * @param newS 新变异后的解
     * @return 得分差 newScore - oldScore
     */
    public static int getDeltaScore(SearchOptSolution oldS, SearchOptSolution newS) {
        Cache cache = newS.cacheBeforeChange;
        int indexOfChange = newS.indexOfChange;
        Pod pod = newS.pods.get(indexOfChange);
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(cache.getNodeMap().get(oldS.getHosts().get(indexOfChange)));
        nodes.add(cache.getNodeMap().get(newS.getHosts().get(indexOfChange)));
        List<HostPriority> scores = defaultGreedyAlgorithm.priorities(pod, nodes, cache);
        return scores.get(1).getScore() - scores.get(0).getScore();
    }

    @Deprecated
    public int getSimpleSumScore(Cache cache) {
        return getScoreByGreedyAlgorithm(cache, defaultGreedyAlgorithm);
    }

    public int getScoreWithFinalResource(Cache cache) {
        int scoreIgnoreResource = getScoreByGreedyAlgorithm(cache, greedyAlgorithmIgnoreResourcePriority);
        Cache tmp = cache.clone();
        for (int i = 0; i < pods.size(); i++) {
            Pod pod = pods.get(i);
            tmp.updateCache(pod, hosts.get(i));
        }
        Pod emptyPod = new Pod(1, null, null);
        emptyPod.setCpuRequest(0D);
        emptyPod.setMemRequest(0D);
        List<Node> nodes = tmp.getNodeList();
        long resourceScore = greedyAlgorithmIgnoreResourcePriority.getIgnoredPriorityRuleConfigs().stream()
                .filter(config -> !config.getWeight().equals(0))
                .mapToLong(config -> config.getWeight() * config.getPriorityRule().priority(emptyPod, nodes, tmp).stream().mapToLong(Integer::longValue).sum())
                .sum();
        // Normalize
        resourceScore = resourceScore * pods.size() / tmp.getNodeList().size();
        return scoreIgnoreResource + (int) resourceScore;
//        return (int) resourceScore;
    }

    private int getScoreByGreedyAlgorithm(Cache cache, GreedyAlgorithm ga) {
        Cache tmp = cache.clone();
        int sum = 0;
        for (int i = 0; i < pods.size(); i++) {
            Pod pod = pods.get(i);
            Node[] node = new Node[]{tmp.getNodeMap().get(hosts.get(i))};
            sum += ga.priorities(pod, Arrays.asList(node), tmp).get(0).getScore();
            tmp.updateCache(pod, hosts.get(i));
        }
        return sum;
    }

    public List<String> getHosts() {
        return hosts;
    }
}
