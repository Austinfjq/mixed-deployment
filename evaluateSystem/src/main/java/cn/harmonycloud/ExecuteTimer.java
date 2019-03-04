package cn.harmonycloud;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时执行器,执行评估
 *
 */
public class ExecuteTimer
{
    public static void main(String[] args) {

        //解析策略配置文件，获取所有策略
        AchieveStrategy.inital();


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ExecuteTask.process();
            }
        };

        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 2 * 1000;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }
}
