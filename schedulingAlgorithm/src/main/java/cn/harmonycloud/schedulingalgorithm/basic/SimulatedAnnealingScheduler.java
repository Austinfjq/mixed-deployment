package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.saobject.Solution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class SimulatedAnnealingScheduler implements Scheduler {
    private final static Logger LOGGER = LoggerFactory.getLogger(SimulatedAnnealingScheduler.class);

    private Cache cache = new Cache();
    private GreedyScheduler greedyScheduler = new GreedyScheduler();

    @Override
    public void schedule(List<Pod> schedulingRequests) {
        LOGGER.info("start simulated Annealing scheduling!");
        try {
            // todo
//            int score = getGreedyScore(cache, schedulingRequests);

            // 1. 更新缓存监控数据
            cache.fetchCacheData();
            // 2. 获取应用画像信息
            cache.getPortrait(schedulingRequests);
            Solution solution = simulatedAnnealing(schedulingRequests, cache);
        } catch (Exception e) {
            LOGGER.debug("schedule Exception:");
            e.printStackTrace();
        }
    }

    /**
     * maxCount 控制退火时间，作用相当于降温系数
     */
    private final static long maxCount = 10000;
    /**
     * constant 控制收敛速度，作用相当于初温
     */
    private final static double constant = 0.5;

    /**
     * calScore 是否计算出每轮转移后解的得分而不是得分差
     * for debug，为 true 时会很耗时
     */
    private final static boolean calScore = false;

    /**
     * 模拟退火计算最优解
     * @param pods 调度请求pods
     * @param cache 集群状态缓存
     * @return 模拟退火得到的解
     */
    private Solution simulatedAnnealing(List<Pod> pods, Cache cache) {
        Random random = new Random();
        Solution bestSolution = null;
        // 初始解
        Solution solution = Solution.getRandomSolution(cache, pods);
        // 开始搜索
        long count = 0;
        while (count < maxCount) {
            // 随机选取邻近状态
            Solution newSolution = solution.neighbour();
            // 计算分数
            int deltaScore;
            if (calScore) {
                int oldScore = solution.getScore();
                int newScore = newSolution.getScore();
                if (bestSolution == null) {
                    bestSolution = oldScore > newScore ? solution : newSolution;
                }
                deltaScore = newScore - oldScore;
            } else {
                deltaScore = Solution.getDeltaScore(solution, newSolution);
            }
            // 新分数更优，转移
            if (deltaScore > 0) {
                solution = newSolution;
            }
            // 旧分数更优，按照Metropolis准则判断是否转移
            else if (random.nextDouble() < getP(deltaScore, count)) {
                solution = newSolution;
            }
            count++;
        }
        if (calScore) {
            if (solution != bestSolution) {
                LOGGER.info("Lost the best solution: score="+ bestSolution.getScore() + ", hosts=" + bestSolution.getHosts());
            } else {
                LOGGER.info("The result solution is the best solution during simulating annealing.");
            }
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

    private int getGreedyScore(Cache cache, List<Pod> schedulingRequests) {
        // TODO
        return 0;
    }
}
