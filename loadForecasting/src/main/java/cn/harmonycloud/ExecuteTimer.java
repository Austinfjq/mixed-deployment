package cn.harmonycloud;

import cn.harmonycloud.tools.PropertyFileUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午2:40
 * @Despriction 定时任务，周期性对所有需要预测的指标进行预测
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
        long intervalPeriod = Long.valueOf(PropertyFileUtil.getValue("PredectPeriod"));
        timer.scheduleAtFixedRate(task, delay, intervalPeriod);
    }
}
