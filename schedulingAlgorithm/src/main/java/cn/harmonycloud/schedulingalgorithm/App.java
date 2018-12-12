package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * 调度算法模块
 */
public class App {
    /**
     * 调度器，包含调度算法
     */
    private static Scheduler scheduler = new GreedyScheduler();
    /**
     * 并发队列，保存要调度的pods
     */
    private static ConcurrentLinkedQueue<Pod> requestQueue = new ConcurrentLinkedQueue<>();
    /**
     * semaphore 用于requestQueue的生产者和消费者
     * 每当requestQueue被增加一次时，消费者会被触发一次用尽队列元素。消费者不需轮询requestQueue
     */
    private static final Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) {
        startConsuming();
    }

    /**
     * TODO 接收网络请求
     *
     * @param podList 请求
     * @return 是否成功
     */
    public static boolean produce(List<Pod> podList) {
        try {
            requestQueue.addAll(podList);
            semaphore.release();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 等待队列中有pod调度请求时，调用schedule()调度
     * 调度进行期间，新加入的pod请求必须等待这轮调度结束。
     */
    private static void startConsuming() {
        List<Pod> podList = new ArrayList<>();
        while (true) {
            try {
                semaphore.acquire();
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
