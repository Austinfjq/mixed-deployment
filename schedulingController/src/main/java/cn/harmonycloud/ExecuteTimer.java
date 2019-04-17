package cn.harmonycloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午2:40
 * @Despriction 定时任务，定时执行调控
 */

@Component
@Order(value = 2)
public class ExecuteTimer implements ApplicationRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExecuteTimer.class);

    @Value("${SchedulePeriod}")
    private int schedulePeriod;

    public void run(ApplicationArguments args) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ExecuteTask dispatcher = new ExecuteTask();
                dispatcher.process();
            }
        };

        LOGGER.info("start timer task!");
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = schedulePeriod;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

}
