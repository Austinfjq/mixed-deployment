package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.searchopt.SearchOptSolution;
import cn.harmonycloud.schedulingalgorithm.searchopt.SolutionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GeneticScheduler implements Scheduler {
    private final static Logger LOGGER = LoggerFactory.getLogger(AnnealingScheduler.class);

    // use fake cache for test
    private Cache cache = new FakeCache();

    @Override
    public void schedule(List<Pod> schedulingRequests) {
        LOGGER.info("start simulated Annealing scheduling!");
        try {
            // 1. 更新缓存监控数据
            cache.fetchSingleCacheData();
            // 2. 获取应用画像信息
            cache.getPortrait(schedulingRequests);

            SearchOptSolution solution = genetic(schedulingRequests);
        } catch (Exception e) {
            LOGGER.debug("schedule Exception:");
            e.printStackTrace();
        }
    }

    private SearchOptSolution genetic(List<Pod> pods) {
        // TODO
        return null;
    }
}
