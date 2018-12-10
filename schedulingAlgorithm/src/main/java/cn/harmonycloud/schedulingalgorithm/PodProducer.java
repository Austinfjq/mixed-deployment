package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PodProducer {
    static ConcurrentLinkedQueue<Pod> requestQueue = new ConcurrentLinkedQueue<>();

    public static boolean produce(List<Pod> podList) {
        try {
            requestQueue.addAll(podList);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
