package cn.harmonycloud.schedulingalgorithm.searchopt;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SolutionSet {
    private static Random random = new Random();

    public List<SolutionAndScore> solutionAndScores;

    public List<SolutionAndScore> getSolutionAndScores() {
        return solutionAndScores;
    }

    public void setSolutionAndScores(List<SolutionAndScore> solutionAndScores) {
        this.solutionAndScores = solutionAndScores;
    }

    public SolutionSet(List<SolutionAndScore> solutionAndScores) {
        this.solutionAndScores = solutionAndScores;
    }

    public static SolutionSet getInitialSolutionSet(Cache cache, List<Pod> pods, int setSize) {
        List<SolutionAndScore> solutionAndScores = new ArrayList<>();
        for (int i = 0; i < setSize; i++) {
            SearchOptSolution solution = SearchOptSolution.getInitialShuffleSolution(cache, pods);
            if (solution == null) {
                throw new RuntimeException("getInitialShuffleSolution() fail!");
            }
            solutionAndScores.add(new SolutionAndScore(solution, solution.getScoreWithFinalResource(cache)));
        }
        return new SolutionSet(solutionAndScores);
    }

    public SolutionSet natureSelect(Cache cache, int setSize) {
        long sum = solutionAndScores.stream().mapToLong(sas -> {
            if (sas.score == null) {
                sas.score = sas.solution.getScoreWithFinalResource(cache); // 终态资源评分就是适应度
            }
            return sas.score;
        }).sum();
        int size = solutionAndScores.size();
        double[] p = new double[size]; // 选中概率
        double[] q = new double[size]; // 累积概率
        for (int i = 0; i < size; i++) {
            p[i] = solutionAndScores.get(i).score / (double) sum;
            if (i == 0) {
                q[i] = p[i];
            } else {
                q[i] = q[i - 1] + p[i];
            }
        }
        List<SolutionAndScore> list = new ArrayList<>();
        random.doubles().limit(setSize).forEach(d -> {
            int i = DOUtils.binaryLessThan(q, d);
            list.add(this.solutionAndScores.get(i));
        });
        return new SolutionSet(list);
    }
}
