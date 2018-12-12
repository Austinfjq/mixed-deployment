package cn.harmonycloud.schedulingalgorithm;

import static org.junit.Assert.assertTrue;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void queueConsumingTest() {
        ConcurrentLinkedQueue<Double> queue = new ConcurrentLinkedQueue<>();
        final Semaphore semaphore = new Semaphore(0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S ");
        Runnable r = () -> {
            Random random = new Random();
            List<Double> list = new ArrayList<>();
            while (true) {
                try {
                    Thread.sleep(1000 * (random.nextInt(3) + 3));
                    System.out.println(df.format(new Date()) + " Producer: Wake up !! ");
                } catch (Exception e) {
                    System.out.println(df.format(new Date()) + " Producer: Exception " + e);
                }
                list.clear();
                int n = (random.nextInt(5)) + 2;
                for (int i = 0; i < n; i++) {
                    list.add(random.nextDouble() % 100);
                }
                queue.addAll(list);
                System.out.println(df.format(new Date()) + " Producer: release !! " + n + ", " +list.size());
                semaphore.release();
            }
        };
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();

        List<Double> list = new ArrayList<>();
        while (true) {
            try {
                System.out.println(df.format(new Date()) + " Consumer: Waiting for release... ");
                semaphore.acquire();
                System.out.println(df.format(new Date()) + " Consumer: Semaphore acquire! ");
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    Double d = queue.poll();
                    if (d != null) {
                        list.add(d);
                    }
                }
                try {
                    Thread.sleep(1000 * (2));
                } catch (Exception e) {
                }
                System.out.println(df.format(new Date()) + " Consumer: Finish scheduling: "+ size + ", but in fact remain: " + queue.size());
                if (!list.isEmpty()) {
                    System.out.println(list.toString());
                    list.clear();
                }
            } catch (Exception e) {
                System.out.println(df.format(new Date()) + " Consumer: Exception " + e);
                return; //TODO
            }
        }
    }
}
