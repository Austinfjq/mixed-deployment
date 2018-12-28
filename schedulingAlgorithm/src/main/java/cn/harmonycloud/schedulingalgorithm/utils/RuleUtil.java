package cn.harmonycloud.schedulingalgorithm.utils;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.affinity.Taint;
import cn.harmonycloud.schedulingalgorithm.affinity.Toleration;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Resource;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RuleUtil {
    public static Resource getRequestedAfterOp(Pod pod, Node node, int op) {
        Resource requested = new Resource();
        // TODO 合计pod下各个container需要的cpu mem，或者直接拿到

        if (op == Constants.OPERATION_ADD) {
//        requested.setMilliCPU(node.getCpuUsage() + requested.getMilliCPU());
//        requested.setMemory(node.getMemUsage() + requested.getMemory());
//        requested.setOthers(node.get..() + requested.get..());
        } else {
//        requested.setMilliCPU(node.getCpuUsage() - requested.getMilliCPU());
//        requested.setMemory(node.getMemUsage() - requested.getMemory());
//        requested.setOthers(node.get..() - requested.get..());
        }
        return requested;
    }

    private static final String LabelZoneRegion = "failure-domain.beta.kubernetes.io/region";
    private static final String LabelZoneFailureDomain = "failure-domain.beta.kubernetes.io/zone";
    public static String getZoneKey(Node node) {
        Map<String, String> labels = new HashMap<>(); // TODO get labels of node
        if (labels == null) {
            return null;
        }
        String region = labels.get(LabelZoneRegion);
        String failureDomain = labels.get(LabelZoneFailureDomain);
        if (StringUtils.isEmpty(region) && StringUtils.isEmpty(failureDomain)) {
            return null;
        }

        // We include the null character just in case region or failureDomain has a colon
        // (We do assume there's no null characters in a region or failureDomain)
        // As a nice side-benefit, the null character is not printed by fmt.Print or glog
        return region + ":\\x00:" + failureDomain;
    }

    public static List<Integer> normalizeReduce(Pod pod, List<Node> nodes, Cache cache, List<Integer> mapResult, int maxPriority, boolean reverse) {
        int maxCount = 0;
        for (int i = 0; i < mapResult.size(); i++) {
            if (mapResult.get(i) > maxCount) {
                maxCount = mapResult.get(i);
            }
        }
        if (maxCount == 0) {
            if (reverse) {
                for (int i = 0; i < mapResult.size(); i++) {
                    mapResult.set(i, maxPriority);
                }
            }
            return mapResult;
        }

        for (int i = 0; i < mapResult.size(); i++) {
            int score = mapResult.get(i);

            score = maxPriority * score / maxCount;
            if (reverse) {
                score = maxPriority - score;
            }

            mapResult.set(i, score);
        }
        return mapResult;
    }

    public static boolean tolerationsTolerateTaint(List<Toleration> tolerations, Taint taint) {
        return tolerations.stream().anyMatch(toleration -> toleratesTaint(toleration, taint));
    }

    private static boolean toleratesTaint(Toleration toleration, Taint taint) {
        if (Objects.equals(toleration.getEffect(), taint.getEffect())) {
            return false;
        }
        if (Objects.equals(toleration.getKey(), taint.getKey())) {
            return false;
        }
        if (toleration.getOperator() == null) {
            // operator == null 当作 operator == TolerationOperator.Equal
            return Objects.equals(toleration.getValue(), taint.getValue());
        }
        switch (toleration.getOperator()) {
            case Equal:
                return Objects.equals(toleration.getValue(), taint.getValue());
            case Exists:
                return true;
            default:
                return false;
        }
    }
}
