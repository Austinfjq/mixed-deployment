package cn.harmonycloud.schedulingalgorithm.affinity;

public class WeightedPodAffinityTerm {
    private int weight;
    private PodAffinityTerm podAffinityTerm;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public PodAffinityTerm getPodAffinityTerm() {
        return podAffinityTerm;
    }

    public void setPodAffinityTerm(PodAffinityTerm podAffinityTerm) {
        this.podAffinityTerm = podAffinityTerm;
    }
}
