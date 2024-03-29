package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.affinity.Taint;
import cn.harmonycloud.schedulingalgorithm.affinity.TaintEffect;
import cn.harmonycloud.schedulingalgorithm.affinity.Toleration;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

import java.util.Arrays;
import java.util.List;

public class PodToleratesNodeTaintsPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // These two lists do not have null elements.
        if (pod.getToleration() == null || pod.getToleration().length == 0 || node.getTaintsArray() == null || node.getTaintsArray().length == 0) {
            return true;
        }
        List<Toleration> tolerations = Arrays.asList(pod.getToleration());
        List<Taint> taints = Arrays.asList(node.getTaintsArray());
        // t.Effect == v1.TaintEffectNoSchedule || t.Effect == v1.TaintEffectNoExecute
        return taints.stream().filter(t -> t.getEffectObject() == TaintEffect.TaintEffectNoSchedule
                || t.getEffectObject() == TaintEffect.TaintEffectNoExecute)
                .allMatch(taint -> RuleUtil.tolerationsTolerateTaint(tolerations, taint));
    }
}
