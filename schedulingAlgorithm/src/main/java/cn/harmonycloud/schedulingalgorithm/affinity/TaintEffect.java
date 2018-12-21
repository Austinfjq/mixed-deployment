package cn.harmonycloud.schedulingalgorithm.affinity;

public enum TaintEffect {
    TaintEffectNoSchedule("NoSchedule"),
    TaintEffectPreferNoSchedule("PreferNoSchedule"),
    TaintEffectNoExecute("NoExecute");

    private final String effect;
    TaintEffect(final String e) {
        this.effect = e;
    }

    @Override
    public String toString() {
        return effect;
    }
}
