package cn.harmonycloud.dataProcessing;

import cn.harmonycloud.dataProcessing.datapreprocess.GetNodeData;
import cn.harmonycloud.dataProcessing.datapreprocess.GetPodData;
import cn.harmonycloud.dataProcessing.datapreprocess.GetSvcData;
import cn.harmonycloud.dataProcessing.metric.Constant;
import cn.harmonycloud.dataProcessing.tools.Write2ES;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class DataProcessingApplication {

    @EventListener(ApplicationReadyEvent.class)
    public void run() {

        Integer cacheTime = Integer.parseInt(Constant.STEP_TIME);
        //延迟时间，时间单位为毫秒
        Integer delay = 0;
        //执行时间，时间单位为毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //要执行的操作
                Write2ES.run(GetNodeData.run(), "nodes");
                Write2ES.run(GetPodData.run(), "pods");
                Write2ES.run(GetSvcData.run(), "services");

            }
        }, delay, cacheTime);
    }


    public static void main(String[] args) {

        SpringApplication.run(DataProcessingApplication.class, args);

    }

}
