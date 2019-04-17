package cn.harmonycloud;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午2:42
 * @Despriction 定时执行的任务
 */
public class ExecuteTask {

    @Value("${SchedulePeriod}")
    private int schedulePeriod;

    public void process() {
        //start the thread of regulate all online service!
        OnlineRegulateTaskThread onlineRegulateTaskThread = new OnlineRegulateTaskThread();
        Thread onlineRegulateThread = new Thread(onlineRegulateTaskThread);
        onlineRegulateThread.start();

        //start the thread of regulate offline service!
        OfflineRegulateTaskThread offlineRegulateTaskThread = new OfflineRegulateTaskThread();
        Thread offlineRegulateThread = new Thread(offlineRegulateTaskThread);
        offlineRegulateThread.start();
    }
}
