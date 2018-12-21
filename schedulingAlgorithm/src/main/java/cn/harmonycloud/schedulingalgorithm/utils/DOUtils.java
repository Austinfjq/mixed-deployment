package cn.harmonycloud.schedulingalgorithm.utils;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;

public class DOUtils {
    public static String NAME_SPLIT = "_";

    public static String getServiceFullName(Service service) {
        return service.getServiceName() + NAME_SPLIT + service.getNamespace();
    }

    public static String getServiceFullName(Pod pod) {
        return pod.getServiceName() + NAME_SPLIT + pod.getNamespace();
    }

    public static String getPodFullName(Pod pod) {
        return pod.getPodName() + NAME_SPLIT + pod.getNamespace();
    }
}
