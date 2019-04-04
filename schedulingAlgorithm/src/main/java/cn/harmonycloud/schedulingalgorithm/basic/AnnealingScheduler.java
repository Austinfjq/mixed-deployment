package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.searchopt.SearchOptSolution;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnnealingScheduler implements Scheduler {
    private final static Logger LOGGER = LoggerFactory.getLogger(AnnealingScheduler.class);

    // use fake cache for test
    private Cache cache = new FakeCache();

    public static void main(String[] args) {
        showCacheResource();
        GlobalSetting.LOG_DETAIL = false;
        List<Pod> podList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            podList.add(new Pod(1, "wordpress", "wordpress-mysql"));
//        }
        for (int i = 0; i < 10; i++) {
            podList.add(new Pod(1, "wordpress", "wordpress-wp"));
        }
        Scheduler scheduler = new AnnealingScheduler();
        scheduler.schedule(podList);
        if (runManyTimes) {
            for (int i = 0; i < 20; i++) {
                Scheduler scheduler1 = new AnnealingScheduler();
                scheduler1.schedule(podList);
            }
            LOGGER.info("improvementList=" + improvementList);
            LOGGER.info("improvement=" + improvementList.stream().mapToDouble(Double::doubleValue).average().orElse(-9999D) * 100 + "%");
            LOGGER.info("UseBothImprovement=" + improvementList.stream().mapToDouble(Double::doubleValue).map(d -> d < 0 ? 0 : d).average().orElse(-9999D) * 100 + "%");
            LOGGER.info("good=" + good + ",bad=" + bad);
        }
    }

    private static boolean runManyTimes = true;
    private static List<Double> improvementList = new ArrayList<>();
    private static int good = 0;
    private static int bad = 0;

    private static void showCacheResource() {
        FakeCache c = new FakeCache();
        c.fetchSingleCacheData();
        Map map1 = c.getNodeList().stream().collect(Collectors.toMap(Node::getNodeName, n -> n.getCpuUsage() + "/" + n.getAllocatableCpuCores() + "," + n.getMemUsage() + "/" + n.getAllocatableMem()));
        Map map2 = c.getPodMap().values().stream().collect(Collectors.toMap(DOUtils::getPodFullName, p -> p.getCpuRequest() + "," + p.getMemRequest()));
        LOGGER.info("\n" + map1);
        LOGGER.info("\n" + map2);
        Map map3 = c.getNodeList().stream().collect(Collectors.toMap(Node::getNodeName, n -> n.getAllocatableCpuCores() + "-" + n.getAllocatableMem()));
        LOGGER.info("\n" + map3);
    }

    @Override
    public void schedule(List<Pod> schedulingRequests) {
        LOGGER.info("start simulated Annealing scheduling!");
        try {
            // 1. 更新缓存监控数据
            cache.fetchSingleCacheData();
            // 2. 获取应用画像信息
            cache.getPortrait(schedulingRequests);
            // 贪心算法
            SearchOptSolution greedySolution = simulatedGreedySchedule(schedulingRequests);
            LOGGER.info("GreedyScheduler simple sum score = " + greedySolution.getScoreWithFinalResource(cache) + "; hosts=" + greedySolution.getHosts());
            // 退火算法
            SearchOptSolution annealingSolution = annealing(schedulingRequests, greedySolution);
            // 记录解的改善度
            if (runManyTimes) {
                int a = annealingSolution.getScoreWithFinalResource(cache);
                int b = greedySolution.getScoreWithFinalResource(cache);
                double improve = 1.0D * (a - b) / b;
                if (a < b) bad++;
                else good++;
                improvementList.add(improve);
            }
            // 比较解
            LOGGER.info("--------------------------------------------------------------------------------");
            LOGGER.info("Schedule Result:");
            LOGGER.info("--------------------------------------------------------------------------------");
            for (int i = 0; i < 5; i++) {
                LOGGER.info("\nRandom solution score = " + getRandomSolutionResult(cache, schedulingRequests));
            }
            LOGGER.info("--------------------------------------------------------------------------------");
            LOGGER.info("\nGreedyScheduler simple sum score    = " + greedySolution.getScoreWithFinalResource(cache));
            LOGGER.info("\nAnnealingScheduler simple sum score = " + annealingSolution.getScoreWithFinalResource(cache));
            LOGGER.info("Node List=" + cache.getNodeList().stream().map(Node::getNodeName).collect(Collectors.toList()));
            LOGGER.info("\nNode Number List of GreedyScheduler   = " + getNodePodNumberList(cache.getNodeList(), greedySolution, schedulingRequests));
            LOGGER.info("\nNode Number List of AnnealingScheduler= " + getNodePodNumberList(cache.getNodeList(), annealingSolution, schedulingRequests));
            LOGGER.info("Finish.");
        } catch (Exception e) {
            LOGGER.debug("schedule Exception:");
            e.printStackTrace();
        }
    }

    private String getRandomSolutionResult(Cache cache, List<Pod> pods) {
        SearchOptSolution s = SearchOptSolution.getInitialSolution(cache, pods);
        return s == null ? "getInitialSolution() fail" : s.getScoreWithFinalResource(cache) + getNodePodNumberList(cache.getNodeList(), s, pods);
    }

    private String getNodePodNumberList(List<Node> nodes, SearchOptSolution sol, List<Pod> pods) {
        Map<String, Long> map1 = sol.getHosts().subList(0, pods.size() / 2).stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<String, Long> map2 = sol.getHosts().subList(pods.size() / 2, pods.size()).stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        StringBuilder sb = new StringBuilder();
        sb.append(" [");
        for (int i = 0; i < nodes.size(); i++) {
            Long num = map1.get(nodes.get(i).getNodeName());
            sb.append(num == null ? "0" : num);
            if (i < nodes.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]; ");
        sb.append("[");
        for (int i = 0; i < nodes.size(); i++) {
            Long num = map2.get(nodes.get(i).getNodeName());
            sb.append(num == null ? "0" : num);
            if (i < nodes.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private SearchOptSolution simulatedGreedySchedule(List<Pod> schedulingRequests) {
        // 对比GreedyScheduler
        GreedyScheduler greedyScheduler = new GreedyScheduler();
        greedyScheduler.cache = this.cache.clone();
        GreedyAlgorithm greedyAlgorithm = new DefaultGreedyAlgorithm();
//        List<Pod> sortedPods = new ArrayList<>(schedulingRequests);
        List<Pod> sortedPods = greedyAlgorithm.presort(new ArrayList<>(schedulingRequests), greedyScheduler.cache);
        List<String> hosts = new ArrayList<>();
        for (int i = 0; i < sortedPods.size(); i++) {
            HostPriority host = greedyScheduler.scheduleOne(sortedPods.get(i), true);
            if (host == null) {
                throw new RuntimeException("Cannot find a solution in simulatedGreedySchedule().");
            }
            hosts.add(host.getHostname());
        }
        return new SearchOptSolution(null, sortedPods, hosts, -1);
    }

    /**
     * 退火速度 0<rate<1
     */
    private final static double rate = 0.999;
    /**
     * 退火初始温度 需要较大
     */
    private final static double initialT = 100;
    /**
     * 退火终止温度
     */
    private final static double finalT = 0.001;

    /**
     * 模拟退火计算最优解
     *
     * @param pods 调度请求pods
     * @return 模拟退火得到的解
     */
    private SearchOptSolution annealing(List<Pod> pods, SearchOptSolution greedySolution) {
//        int greedyScore = greedySolution.getScoreWithFinalResource(cache);
        Random random = new Random();
        // 初始解
        SearchOptSolution solution = null;
//        solution = SearchOptSolution.getInitialSolution(cache, pods);
        if (solution == null) {
            try {
                solution = simulatedGreedySchedule(pods);
            } catch (Exception e) {
                throw new RuntimeException("Cannot find an initial solution.");
            }
        }
//        LOGGER.info("Initial solution=" + solution.getHosts() + ", score=" + solution.getScoreWithFinalResource(cache));
        // 开始搜索
        long count = 0;
        double temperature = initialT;
        while (temperature > finalT) {
            // 随机选取邻近状态
            SearchOptSolution newSolution = solution.neighbour(cache);
            // 计算分数差
            int deltaScore = SearchOptSolution.getDeltaScore(solution, newSolution);
//            LOGGER.info("New solution=" + solution.getHosts() + ", score=" + solution.getScoreWithFinalResource(cache));
            // 新分数更优，转移
            if (deltaScore > 0) {
                solution = newSolution;
//                LOGGER.info("deltaScore > 0");
            }
            // 旧分数更优，按照Metropolis准则判断是否转移
            else if (random.nextDouble() < getP(deltaScore, temperature)) {
                solution = newSolution;
//                LOGGER.info("deltaScore <= 0, but still update");
            }
            // 退火
            temperature *= rate;
//            LOGGER.info("count=" + ++count + ", temperature=" + temperature + "final temperature=" + finalT);
        }
        return solution;
    }

    /**
     * 旧分数更优时的转移概率
     *
     * @return 转移概率
     */
    private double getP(int deltaScore, double temperature) {
        return Math.exp(deltaScore / temperature);
    }
}
