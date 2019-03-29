package cn.harmonycloud;

import cn.harmonycloud.datapreprocess.GetNodeData;
import cn.harmonycloud.datapreprocess.GetPodData;
import cn.harmonycloud.datapreprocess.GetSvcData;
import cn.harmonycloud.metric.Constant;
import cn.harmonycloud.tools.Write2ES;

import java.util.Timer;
import java.util.TimerTask;

public class DataProcess {

    public static Integer cacheTime = Integer.parseInt(Constant.STEP_TIME);
    //延迟时间，时间单位为毫秒
    public static Integer delay = 0;

    public static void run() {

        //执行时间，时间单位为毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //要执行的操作

//                System.out.println(Write2ES.run(GetNodeData.run(), "nodes"));
//                System.out.println(Write2ES.run(GetPodData.run(), "pods"));
//                System.out.println(Write2ES.run(GetSvcData.run(), "services"));
                Write2ES.run(GetNodeData.run(), "nodes");
                Write2ES.run(GetPodData.run(), "pods");
                Write2ES.run(GetSvcData.run(), "services");

            }
        }, delay, cacheTime);
    }

    public static void main(String args[]) {
        run();
    }

}

