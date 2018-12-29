package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.affinity.Taint;
import cn.harmonycloud.schedulingalgorithm.affinity.TaintEffect;
import cn.harmonycloud.schedulingalgorithm.affinity.Toleration;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

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
                .allMatch(taint -> RuleUtil.tolerationsTolerateTaint(tolerations, taint));
    }
}