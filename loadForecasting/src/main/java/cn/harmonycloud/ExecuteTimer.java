package cn.harmonycloud;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午2:40
 * @Despriction
 */
public class ExecuteTimer {
    public static void main(String[] args) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ExecuteTask dispatcher = new ExecuteTask();
                dispatcher.process();
            }
        };

        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 120 * 1000;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }
}
