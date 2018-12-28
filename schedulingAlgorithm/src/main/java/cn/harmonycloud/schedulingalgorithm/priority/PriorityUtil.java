package cn.harmonycloud.schedulingalgorithm.priority;

import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Resource;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class PriorityUtil {
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
}
