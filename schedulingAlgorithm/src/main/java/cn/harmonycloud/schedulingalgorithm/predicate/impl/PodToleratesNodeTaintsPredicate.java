package cn.harmonycloud.schedulingalgorithm.predicate.impl;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.affinity.Taint;
import cn.harmonycloud.schedulingalgorithm.affinity.TaintEffect;
import cn.harmonycloud.schedulingalgorithm.affinity.Toleration;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.utils.CheckUtil;

import java.util.ArrayList;
import java.util.List;

public class PodToleratesNodeTaintsPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        List<Toleration> tolerations = new ArrayList<>(); // TODO from pod cache
        List<Taint> taints = new ArrayList<>(); // TODO from node cache
        // These two lists do not have null elements.
        if (taints.isEmpty()) {
            return true;
        }
        // t.Effect == v1.TaintEffectNoSchedule || t.Effect == v1.TaintEffectNoExecute
        return taints.stream().filter(t -> t.getEffect() == TaintEffect.TaintEffectNoSchedule
                || t.getEffect() == TaintEffect.TaintEffectNoExecute)
                .allMatch(taint -> TolerationsTolerateTaint(tolerations, taint));
    }

    private boolean TolerationsTolerateTaint(List<Toleration> tolerations, Taint taint) {
        return tolerations.stream().anyMatch(toleration -> ToleratesTaint(toleration, taint));
    }

    private boolean ToleratesTaint(Toleration toleration, Taint taint) {
        if (CheckUtil.equal(toleration.getEffect(), taint.getEffect())) {
            return false;
        }
        if (CheckUtil.equal(toleration.getKey(), taint.getKey())) {
            return false;
        }
        if (toleration.getOperator() == null) {
            // operator == null 当作 operator == TolerationOperator.Equal
            return CheckUtil.equal(toleration.getValue(), taint.getValue());
        }
        switch (toleration.getOperator()) {
            case Equal:
                return CheckUtil.equal(toleration.getValue(), taint.getValue());
            case Exists:
                return true;
            default:
                return false;
        }
    }
}
