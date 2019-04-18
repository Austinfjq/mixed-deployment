package cn.harmonycloud.scheduled;

import cn.harmonycloud.service.IOfflineRegulateControl;
import cn.harmonycloud.service.IOnlineRegulateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: psy
 * @Date: 2019/4/17 17:33
 * @Description:
 */
@Component
public class ScheduledTask {
    private final static Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);


    private static final int refreshTime = 600000;

    @Autowired
    private IOnlineRegulateControl iOnlineRegulateControl;

    @Autowired
    private IOfflineRegulateControl iOfflineRegulateControl;

    @org.springframework.scheduling.annotation.Scheduled(fixedRate = refreshTime)
    public void Task() {

        LOGGER.info("start timer task!");
        Thread onlineThread = new Thread(() -> iOnlineRegulateControl.process());
        onlineThread.start();

        Thread offlineThread = new Thread(() -> iOfflineRegulateControl.process());
        offlineThread.start();
    }
}
