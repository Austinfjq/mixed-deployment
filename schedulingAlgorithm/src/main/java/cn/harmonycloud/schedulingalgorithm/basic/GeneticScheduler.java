package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.searchopt.SearchOptSolution;
import cn.harmonycloud.schedulingalgorithm.searchopt.SolutionAndScore;
import cn.harmonycloud.schedulingalgorithm.searchopt.SolutionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GeneticScheduler implements Scheduler {
    private final static Logger LOGGER = LoggerFactory.getLogger(AnnealingScheduler.class);

    // use fake cache for test
    private Cache cache = new FakeCache();

    @Override
    public void schedule(List<Pod> schedulingRequests) {
        LOGGER.info("start simulated Annealing scheduling!");
        try {
            // 1. 更新缓存监控数据
            cache.fetchSingleCacheData();
            // 2. 获取应用画像信息
            cache.getPortrait(schedulingRequests);

            SearchOptSolution greedySolution = AnnealingScheduler.simulatedGreedySchedule(this.cache, schedulingRequests);
            GeneticConfig config = new GeneticConfig();
            SearchOptSolution solution = genetic(schedulingRequests, config);
            int a = greedySolution.getScoreWithFinalResource(cache);
            int b = solution.getScoreWithFinalResource(cache);
            LOGGER.info("GreedyScheduler getScoreWithFinalResource = " + a + "; hosts=" + greedySolution.getHosts());
            LOGGER.info("GeneticScheduler getScoreWithFinalResource = " + b + "; hosts=" + greedySolution.getHosts());
            double improve = 1.0D * (a - b) / b;
            if (a < b) bad++;
            else good++;
            improvementList.add(improve);
        } catch (Exception e) {
            LOGGER.debug("schedule Exception:");
            e.printStackTrace();
        }
    }

    private static List<Double> improvementList = new ArrayList<>();
    private static int good = 0;
    private static int bad = 0;

    public static void main(String[] args) {
        GlobalSetting.LOG_DETAIL = false;
        List<Pod> podList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            podList.add(new Pod(1, "wordpress", "wordpress-mysql"));
//        }
        for (int i = 0; i < 10; i++) {
            podList.add(new Pod(1, "wordpress", "wordpress-wp"));
        }
        for (int i = 0; i < 20; i++) {
            GeneticScheduler geneticScheduler = new GeneticScheduler();
            geneticScheduler.schedule(podList);
            LOGGER.info("improvementList=" + improvementList);
            LOGGER.info("improvement=" + improvementList.stream().mapToDouble(Double::doubleValue).average().orElse(-9999D) * 100 + "%");
            LOGGER.info("UseBothImprovement=" + improvementList.stream().mapToDouble(Double::doubleValue).map(d -> d < 0 ? 0 : d).average().orElse(-9999D) * 100 + "%");
            LOGGER.info("good=" + good + ",bad=" + bad);
        }
    }

    class GeneticConfig {
        int setSize = 10;
        double relativeToleration = 0.8;
        double crossProbability = 0.8;
        double mutateProbability = 0.8;
        int mutateBitNumber = 5;
        boolean reserveBest = true;
        int generation = 1000;
    }

    private SearchOptSolution genetic(List<Pod> pods, GeneticConfig config) {
        SolutionSet ss = SolutionSet.getInitialSolutionSet(this.cache, pods, config.setSize);
        ss.solutionAndScores.set(0, new SolutionAndScore(AnnealingScheduler.simulatedGreedySchedule(this.cache, pods), null));
        for (int i = 0; i < config.generation; i++) {
            ss = ss.natureSelect(this.cache, config.setSize, config.reserveBest);
            ss = ss.crossover(this.cache, config.relativeToleration, config.crossProbability);
            ss = ss.simpleMutate(this.cache, config.mutateProbability, config.mutateBitNumber);
        }
        SolutionAndScore result = ss.natureSelect(this.cache, config.setSize, config.reserveBest).best;
        return result.solution;
    }
}
