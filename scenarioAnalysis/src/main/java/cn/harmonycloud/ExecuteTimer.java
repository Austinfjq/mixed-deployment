package cn.harmonycloud;

import cn.harmonycloud.beans.LoadConfigFile;
import cn.harmonycloud.entry.Config;
import cn.harmonycloud.tools.Write2ES;

import java.text.SimpleDateFormat;
import java.util.*;

import static cn.harmonycloud.ScenarioExecutor.getResults;

public class ExecuteTimer {
    public static void main(String[] args) {

        //解析策略配置文件，获取所有策略
        ArrayList<Config> configList = LoadConfigFile.run();

        Calendar startDate = Calendar.getInstance();

        for (Config cfg : configList) {

//            long daySpan = 24 * 60 * 60 * 1000;
            long daySpan = 5000;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + cfg.getTime());

            try {
                Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
                // 如果今天的已经过了 首次运行时间就改为明天
                if (System.currentTimeMillis() > startTime.getTime()) {
                    startTime = new Date(startTime.getTime() + daySpan);
                }

                TimerTask task1 = new TimerTask() {
                    @Override
                    public void run() {
                        StrategyExecutor.run(cfg.getPodAddList(), cfg.getPodDelList());
                    }
                };

                Timer timer = new Timer();
                timer.schedule(task1, startTime, daySpan);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
//                System.out.println("lll");
//                ScenarioExecutor.run();
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 1000;  //毫秒
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

}
