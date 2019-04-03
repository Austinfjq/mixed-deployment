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

    public String getEffect() {
        return effect;
    }

    public static TaintEffect getEffectObject(String o) {
        if (TaintEffectNoSchedule.effect.equals(o)) {
            return TaintEffectNoSchedule;
        } else if (TaintEffectPreferNoSchedule.effect.equals(o)) {
            return TaintEffectPreferNoSchedule;
        } else if (TaintEffectNoExecute.effect.equals(o)) {
            return TaintEffectNoExecute;
        } else {
            return null;
        }
    }
}
