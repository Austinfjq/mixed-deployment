package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.common.HostPriority;
import cn.harmonycloud.schedulingalgorithm.common.Pod;

import java.util.List;

public class Utils {
    public static List<Pod> getPodsDetail(List<Pod> pods) {
        //TODO
        return null;
    }

    public static HostPriority generateZeroHostPriority(String host) {
        HostPriority hostPriority = new HostPriority();
        hostPriority.setHost(host);
        hostPriority.setScore(0);
        return hostPriority;
    }
}
