package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.List;

public class PodAntiAffinity {
    private PodAffinityTerm[] requiredDuringSchedulingIgnoredDuringExecution;
    private WeightedPodAffinityTerm[] preferredDuringSchedulingIgnoredDuringExecution;

    public PodAffinityTerm[] getRequiredDuringSchedulingIgnoredDuringExecution() {
        return requiredDuringSchedulingIgnoredDuringExecution;
    }

    public void setRequiredDuringSchedulingIgnoredDuringExecution(PodAffinityTerm[] requiredDuringSchedulingIgnoredDuringExecution) {
        this.requiredDuringSchedulingIgnoredDuringExecution = requiredDuringSchedulingIgnoredDuringExecution;
    }

    public WeightedPodAffinityTerm[] getPreferredDuringSchedulingIgnoredDuringExecution() {
        return preferredDuringSchedulingIgnoredDuringExecution;
    }

    public void setPreferredDuringSchedulingIgnoredDuringExecution(WeightedPodAffinityTerm[] preferredDuringSchedulingIgnoredDuringExecution) {
        this.preferredDuringSchedulingIgnoredDuringExecution = preferredDuringSchedulingIgnoredDuringExecution;
    }
}
