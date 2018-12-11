package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 调度算法模块
 *
 */
public class App {
    private static ConcurrentLinkedQueue<Pod> requestQueue = new ConcurrentLinkedQueue<>();
    private static Scheduler scheduler = new GreedyScheduler();

    public static void main( String[] args ) {
        startConsuming();
    }

    /**
     * TODO 接收网络请求
     * @param podList 请求
     * @return 是否成功
     */
    public static boolean produce(List<Pod> podList) {
        try {
            requestQueue.addAll(podList);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从队列中消费请求，调用schedule()调度
     * 使用消息队列可以保证上一次调度完成前，下一轮调度不会开始
     * TODO 有没有触发式的消息队列，轮询很低效
     */
    private static void startConsuming() {
        List<Pod> podList = new ArrayList<>();
        while (true) {
            try {
                int size = requestQueue.size();
                for (int i = 0; i < size; i++) {
                    Pod pod = requestQueue.poll();
                    if (pod != null) {
                        podList.add(pod);
                    }
                }
                if (!podList.isEmpty()) {
                    scheduler.schedule(podList);
                    podList.clear();
                }
            } catch (Exception e) {
                return; //TODO
            }
        }
    }
}
