package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SimulatedAnnealingScheduler implements Scheduler {
    private final static Logger LOGGER = LoggerFactory.getLogger(SimulatedAnnealingScheduler.class);

    private Cache cache;

    @Override
    public void schedule(List<Pod> schedulingRequests) {
        LOGGER.info("start schedule!");
        try {
            // 1. 更新缓存监控数据
            cache.fetchCacheData();
            // 2. 获取应用画像信息
            cache.getPortrait(schedulingRequests);
            // TODO 
        } catch (Exception e) {
            LOGGER.debug("schedule Exception:");
            e.printStackTrace();
        }
    }
}
