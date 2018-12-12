package cn.harmonycloud.schedulingalgorithm.utils;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;

public class DOUtils {
    public static String NAME_SPLIT = "~~";

    public static String getServiceFullName(Service service) {
        return service.getNamespace() + NAME_SPLIT + service.getServiceName();
    }

    public static String getServiceFullName(Pod pod) {
        return pod.getNamespace() + NAME_SPLIT + pod.getServiceName();
    }

    public static String getPodFullName(Pod pod) {
        return pod.getNamespace() + NAME_SPLIT + pod.getPodName();
    }
}
