package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.affinity.Taint;
import cn.harmonycloud.schedulingalgorithm.affinity.TaintEffect;
import cn.harmonycloud.schedulingalgorithm.affinity.Toleration;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.priority.PriorityRule;
import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TaintTolerationPriority implements PriorityRule {
    @Override
    public List<Integer> priority(Pod pod, List<Node> nodes, Cache cache) {
        List<Integer> mapResult = (GlobalSetting.PARALLEL ? nodes.parallelStream() : nodes.stream())
                .map(node -> computeTaintTolerationPriorityMap(pod, node, cache))
                .collect(Collectors.toList());
        // reduce
        return computeTaintTolerationPriorityReduce(pod, nodes, cache, mapResult);
    }

    private int computeTaintTolerationPriorityMap(Pod pod, Node node, Cache cache) {
        if (node == null) {
            return 0;
        }
        List<Toleration> tolerations = Arrays.asList(pod.getToleration());
        List<Toleration> tolerationsPreferNoSchedule = getAllTolerationPreferNoSchedule(tolerations);
        List<Taint> taints = Arrays.asList(node.getTaintsArray());
        return countIntolerableTaintsPreferNoSchedule(taints, tolerationsPreferNoSchedule);
    }

    private List<Integer> computeTaintTolerationPriorityReduce(Pod pod, List<Node> nodes, Cache cache, List<Integer> mapResult) {
        return RuleUtil.normalizeReduce(pod, nodes, cache, mapResult, GlobalSetting.PRIORITY_MAX_SCORE, true);
    }

    private List<Toleration> getAllTolerationPreferNoSchedule(List<Toleration> tolerations) {
        return tolerations.stream().filter(t -> t.getEffect() == null || t.getEffect().getEffect().isEmpty() || t.getEffect() == TaintEffect.TaintEffectPreferNoSchedule).collect(Collectors.toList());
    }

    private int countIntolerableTaintsPreferNoSchedule(List<Taint> taints, List<Toleration> tolerations) {
        int intolerableTaints = 0;
        for (Taint taint : taints) {
            if (taint.getEffect() != TaintEffect.TaintEffectPreferNoSchedule) {
                continue;
            }
            if (RuleUtil.tolerationsTolerateTaint(tolerations, taint)) {
                intolerableTaints++;
            }
        }
        return intolerableTaints;
    }
}