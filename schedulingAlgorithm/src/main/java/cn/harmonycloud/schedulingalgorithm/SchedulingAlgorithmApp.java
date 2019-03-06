package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * 调度算法模块
 */
@SpringBootApplication
public class SchedulingAlgorithmApp {
    private final static Logger LOGGER = LoggerFactory.getLogger(SchedulingAlgorithmApp.class);

    public static void main(String[] args) {
        SpringApplication.run(SchedulingAlgorithmApp.class, args);
    }

    /**
     * 并发队列，保存要调度的pods
     */
    static ConcurrentLinkedQueue<Pod> requestQueue = new ConcurrentLinkedQueue<>();
    /**
     * 信号量，用于requestQueue的生产者和消费者
     */
    static final Semaphore semaphore = new Semaphore(0);
    /**
     * 调度器，包含调度算法
     */
    private static Scheduler scheduler = new GreedyScheduler();

    /**
     * 每次requestQueue被增加时，消费者会被触发开始一轮调度，用尽队列元素调用scheduler.schedule()
     */
    @EventListener(ApplicationReadyEvent.class)
    public void startConsuming() {
        while (true) {
            try {
                semaphore.acquire();
                // 此处一轮调度开始
                // 每轮调度进行期间，新加入的pod请求必须等待当前这轮调度结束
                LOGGER.info("acquire semaphore!");
                List<Pod> podList = new ArrayList<>();
                int size = requestQueue.size();
                for (int i = 0; i < size; i++) {
                    Pod pod = requestQueue.poll();
                    if (pod != null) {
                        podList.add(pod);
                    }
                }
                if (!podList.isEmpty()) {
                    scheduler.schedule(podList);
                }
                // 此处一轮调度结束
            } catch (Exception e) {
                LOGGER.error("startConsuming() exception: ");
                e.printStackTrace();
                return;
            }
        }
    }
}
