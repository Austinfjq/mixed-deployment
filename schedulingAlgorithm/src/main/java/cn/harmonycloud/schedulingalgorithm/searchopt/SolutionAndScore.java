package cn.harmonycloud.schedulingalgorithm.searchopt;

public class SolutionAndScore implements Cloneable {
    public SearchOptSolution solution;
    public Integer score;

    public SolutionAndScore(SearchOptSolution solution, Integer score) {
        this.solution = solution;
        this.score = score;
    }

    @Override
    public SolutionAndScore clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
        return new SolutionAndScore(solution, score);
    }
}
