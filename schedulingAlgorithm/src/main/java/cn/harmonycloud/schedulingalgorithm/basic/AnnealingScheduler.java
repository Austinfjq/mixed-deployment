package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.DefaultGreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Container;
import cn.harmonycloud.schedulingalgorithm.dataobject.ContainerPort;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.saobject.Solution;
import cn.harmonycloud.schedulingalgorithm.utils.ExecuteUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnnealingScheduler implements Scheduler {
    private final static Logger LOGGER = LoggerFactory.getLogger(AnnealingScheduler.class);

    // TODO use real cache finally
    private Cache cache = new FakeCache();

    /**
     * maxCount 控制退火时间，作用相当于降温系数
     */
    private final static long maxCount = 10000;
    /**
     * constant 控制收敛速度，作用相当于初温
     */
    private final static double constant = 0.5;
    /**
     * logSimpleSum 记录解的简单评分和，不准确仅供参考，耗时for debug
     */
    private final static boolean logSimpleSum = true;

    public static void main(String[] args) {
        GlobalSetting.LOG_DETAIL = false;
        List<Pod> podList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            podList.add(new Pod(1, "wordpress", "wordpress-wp"));
        }
        Scheduler scheduler = new AnnealingScheduler();
        scheduler.schedule(podList);
    }

    @Override
    public void schedule(List<Pod> schedulingRequests) {
        LOGGER.info("start simulated Annealing scheduling!");
        try {
            // 1. 更新缓存监控数据
            cache.fetchCacheData();
            // 2. 获取应用画像信息
            cache.getPortrait(schedulingRequests);

            // 贪心算法
            Solution greedySolution = simulatedGreedySchedule(schedulingRequests);
            LOGGER.info("GreedyScheduler simple sum score = " + greedySolution.getSimpleSumScore(cache) + "; hosts=" + greedySolution.getHosts());
            // 退火算法
            Solution annealingSolution = annealing(schedulingRequests);
            // 比较解
            LOGGER.info("--------------------------------------------------------------------------------");
            LOGGER.info("Schedule Result:");
            LOGGER.info("--------------------------------------------------------------------------------");
            LOGGER.info("\nGreedyScheduler simple sum score    = " + greedySolution.getSimpleSumScore(cache));
            LOGGER.info("\nAnnealingScheduler simple sum score = " + annealingSolution.getSimpleSumScore(cache));
            LOGGER.info("Node List=" + cache.getNodeList().stream().map(Node::getNodeName).collect(Collectors.toList()));
            LOGGER.info("\nNode Number List of GreedyScheduler   = " + getNodePodNumberList(cache.getNodeList(), greedySolution.getHosts().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))));
            LOGGER.info("\nNode Number List of AnnealingScheduler= " + getNodePodNumberList(cache.getNodeList(), annealingSolution.getHosts().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))));
            LOGGER.info("Finish.");
        } catch (Exception e) {
            LOGGER.debug("schedule Exception:");
            e.printStackTrace();
        }
    }

    private String getNodePodNumberList(List<Node> nodes, Map<String, Long> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < nodes.size(); i++) {
            Long num = map.get(nodes.get(i).getNodeName());
            sb.append(num == null ? "0" : num);
            if (i + 1 != nodes.size()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private Solution simulatedGreedySchedule(List<Pod> schedulingRequests) {
        // 对比GreedyScheduler
        GreedyScheduler greedyScheduler = new GreedyScheduler();
        greedyScheduler.cache = this.cache.clone();
        GreedyAlgorithm greedyAlgorithm = new DefaultGreedyAlgorithm();
        List<Pod> sortedPods = greedyAlgorithm.presort(new ArrayList<>(schedulingRequests), greedyScheduler.cache);
        List<String> hosts = new ArrayList<>();
        for (int i = 0; i < sortedPods.size(); i++) {
            HostPriority host = greedyScheduler.scheduleOne(sortedPods.get(i), true);
            hosts.add(host.getHostname());
        }
        return new Solution(null, sortedPods, hosts, -1);
    }

    /**
     * 模拟退火计算最优解
     *
     * @param pods 调度请求pods
     * @return 模拟退火得到的解
     */
    private Solution annealing(List<Pod> pods) {
        Random random = new Random();
        // 初始解
        Solution solution = Solution.getInitialSolution(cache, pods);
        LOGGER.info("Initial solution=" + solution.getHosts() + (logSimpleSum ? (", score=" + solution.getSimpleSumScore(cache)) : ""));
        // 开始搜索
        long count = 0;
        while (count < maxCount) {
            // 随机选取邻近状态
            Solution newSolution = solution.neighbour(cache);
            // 计算分数差
            int deltaScore = Solution.getDeltaScore(solution, newSolution);
            LOGGER.info("New solution=" + solution.getHosts() + (logSimpleSum ? (", score=" + solution.getSimpleSumScore(cache)) : ""));
            // 新分数更优，转移
            if (deltaScore > 0) {
                solution = newSolution;
                LOGGER.info("deltaScore > 0");
            }
            // 旧分数更优，按照Metropolis准则判断是否转移
            else if (random.nextDouble() < getP(deltaScore, count)) {
                solution = newSolution;
                LOGGER.info("deltaScore <= 0, but still update");
            }
            count++;
            LOGGER.info("count=" + count + " / " + "maxCount=" + maxCount);
        }
        return solution;
    }

    /**
     * 旧分数更优时的转移概率
     *
     * @return 转移概率
     */
    private double getP(int deltaScore, long count) {
        return Math.exp(deltaScore * count * constant / maxCount);
    }
}
