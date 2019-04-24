package cn.harmonycloud.schedulingalgorithm.searchopt;

import cn.harmonycloud.schedulingalgorithm.algorithm.greedyalgorithm.GreedyAlgorithm;
import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SolutionSet {
    private static Random random = new Random();

    public List<SolutionAndScore> solutionAndScores;
    public SolutionAndScore best;

    public List<SolutionAndScore> getSolutionAndScores() {
        return solutionAndScores;
    }

    public void setSolutionAndScores(List<SolutionAndScore> solutionAndScores) {
        this.solutionAndScores = solutionAndScores;
    }

    public SolutionSet(List<SolutionAndScore> solutionAndScores, SolutionAndScore best) {
        this.solutionAndScores = solutionAndScores;
        this.best = best;
    }

    /**
     * 获取初始解集合
     *
     * @param cache   缓存
     * @param pods    待调度pod列表
     * @param setSize 集合大小
     * @return 初始解集合
     */
    public static SolutionSet getInitialSolutionSet(Cache cache, List<Pod> pods, int setSize) {
        List<SolutionAndScore> solutionAndScores = new ArrayList<>();
        for (int i = 0; i < setSize; i++) {
            SearchOptSolution solution = SearchOptSolution.getInitialShuffleSolution(cache, pods);
            if (solution == null) {
                throw new RuntimeException("getInitialShuffleSolution() fail!");
            }
            solutionAndScores.add(new SolutionAndScore(solution, solution.getScoreWithFinalResource(cache)));
        }
        return new SolutionSet(solutionAndScores, null);
    }

    /**
     * 自然选择
     * 算子：保留最佳，随机锦标赛
     *
     * @param cache   缓存
     * @param setSize 新解集合大小
     * @param reserveBest 保留最佳
     * @return 自然选择后的新解集合
     */
    public SolutionSet natureSelect(Cache cache, int setSize, boolean reserveBest) {
        // 计算生存概率，最大值等
        long sum = solutionAndScores.stream().mapToLong(sas -> {
            if (sas.score == null) {
                sas.score = sas.solution.getScoreWithFinalResource(cache); // 终态资源总评分就是适应度
            }
            return sas.score;
        }).sum();
        int size = solutionAndScores.size();
        double[] p = new double[size]; // 选中概率
        double[] q = new double[size]; // 累积概率
        int maxIndex = -1;
        int maxScore = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            int score = solutionAndScores.get(i).score;
            if (score > maxScore) {
                maxScore = score;
                maxIndex = i;
            }
            p[i] = score / (double) sum;
            if (i == 0) {
                q[i] = p[i];
            } else {
                q[i] = q[i - 1] + p[i];
            }
        }

        // 自然选择
        List<SolutionAndScore> list = new ArrayList<>();
        // 1. 最佳保留选择，并更新最佳解
        this.best = this.solutionAndScores.get(maxIndex);
        if (reserveBest) {
            list.add(this.best);
        }
        // 2. 随机锦标赛选择 Stochastic Tournament
        for (int i = 0; i < setSize - list.size(); i++) {
            SolutionAndScore a = this.solutionAndScores.get(DOUtils.binaryLessThan(q, random.nextDouble()));
            SolutionAndScore b = this.solutionAndScores.get(DOUtils.binaryLessThan(q, random.nextDouble()));
            SolutionAndScore better = a.score > b.score ? a : b;
            list.add(better.clone());
        }
        return new SolutionSet(list, this.best);
    }

    /**
     * 单点染色体交叉
     * 算子：单点交叉算子
     *
     * @param cache              缓存
     * @param relativeToleration 近亲容忍率 0～1
     * @param crossProbability   交叉概率 0～1
     * @return 单点染色体交叉的解集合
     */
    public SolutionSet crossover(Cache cache, double relativeToleration, double crossProbability) {
        // 随机改变解集合顺序后，每相邻两个解交叉
        List<SolutionAndScore> shuffledSAS = new ArrayList<>(this.solutionAndScores);
        Collections.shuffle(shuffledSAS);
        for (int i = 0; i < shuffledSAS.size() / 2; i++) {
            // 随机概率决定是否交叉
            if (random.nextDouble() > crossProbability) {
                continue;
            }
            // 判断是否是近亲
            SearchOptSolution a = shuffledSAS.get(2 * i).solution;
            SearchOptSolution b = shuffledSAS.get(2 * i + 1).solution;
            if (isRelative(a, b, relativeToleration)) {
                continue;
            }
            // 交叉
            int crossPoint = random.nextInt(a.getHosts().size() < b.getHosts().size() ? a.getHosts().size() : b.getHosts().size());
            List<String> hostsA = a.getHosts();
            List<String> hostsB = b.getHosts();
            List<String> hostsANew = new ArrayList<>();
            List<String> hostsBNew = new ArrayList<>();
            hostsANew.addAll(hostsA.subList(0, crossPoint));
            hostsANew.addAll(hostsB.subList(crossPoint, b.getHosts().size()));
            hostsBNew.addAll(hostsB.subList(0, crossPoint));
            hostsBNew.addAll(hostsA.subList(crossPoint, a.getHosts().size()));
            // 交叉后不是可行解时，不保存
            if (!isValid(cache, a.getPods(), hostsANew) || !isValid(cache, b.getPods(), hostsBNew)) {
                continue;
            }
            a.setHosts(hostsANew);
            b.setHosts(hostsBNew);
            shuffledSAS.get(2 * i).score = null;
            shuffledSAS.get(2 * i + 1).score = null;
        }
        return new SolutionSet(shuffledSAS, null);
    }

    private static boolean isRelative(SearchOptSolution a, SearchOptSolution b, double toleration) {
        if (a.getHosts().size() != b.getHosts().size()) {
            return false;
        }
        int theSameNum = 0;
        for (int i = 0; i < a.getHosts().size(); i++) {
            if (a.getHosts().get(i).equals(b.getHosts().get(i))) {
                theSameNum++;
            }
        }
        return 1.0D * theSameNum / a.getHosts().size() > toleration;
    }

    private static boolean isValid(Cache initial, List<Pod> pods, List<String> hosts) {
        Cache tmp = initial.clone();
        for (int i = 0; i < pods.size(); i++) {
            boolean predicateResult = SearchOptSolution.defaultGreedyAlgorithm.runAllPredicates(pods.get(i), tmp.getNodeMap().get(hosts.get(i)), tmp);
            if (!predicateResult) {
                return false;
            }
            tmp.updateCache(pods.get(i), hosts.get(i));
        }
        return true;
    }

    /**
     * 基本位变异（Simple Mutation）
     *
     * @param mutateProbability 变异概率
     * @param mutateBitNumber   变异位数量
     * @return 变异后的解集合
     */
    public SolutionSet simpleMutate(Cache cache, double mutateProbability, int mutateBitNumber) {
        List<SolutionAndScore> list = new ArrayList<>(this.solutionAndScores);
        for (int i = 0; i < list.size(); i++) {
            // 随机概率决定是否变异
            if (random.nextDouble() > mutateProbability) {
                continue;
            }
            SolutionAndScore sas = list.get(i);
            // 这里简化为 mutateBitNumber 次的 neighbour()
            for (int k = 0; k < mutateBitNumber; k++) {
                sas.solution = sas.solution.neighbour(cache);
            }
            sas.score = null;
        }
        return new SolutionSet(list, null);
    }
}
