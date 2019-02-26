package cn.harmonycloud;

import cn.harmonycloud.beans.LoadConfigFile;
import cn.harmonycloud.dao.NodeDAO;
import cn.harmonycloud.dao.ServiceDAO;
import cn.harmonycloud.entry.*;
import cn.harmonycloud.tools.Write2ES;

import java.util.*;

public class StrategyExecutor {
    public static ArrayList<Result> results = new ArrayList<>();

    public static ArrayList<Result> getResults(Map<String, Long> podAddList,
                                               Map<String, Long> podDelList) {

        List<ForecastService> forecastServiceList = ServiceDAO.getForecastServiceList();
        List<NowService> nowServiceList = ServiceDAO.getNowServiceList();

        List<ForecastNode> forecastNodeList = NodeDAO.getForecastNodeList();
        List<NowNode> nowNodeList = NodeDAO.getNowNodeList();

        Collections.sort(nowServiceList, new Comparator<NowService>() {
            @Override
            public int compare(NowService o1, NowService o2) {
//                return (String.valueOf(o1.getCpuUsage()).compareTo( String.valueOf(o2.getCpuUsage()) ));
                return (o1.getCpuUsage().compareTo(o2.getCpuUsage()));
            }
        });

        //执行service分析的线程

        StrategyTaskThread strategyTaskThread = new StrategyTaskThread(forecastNodeList, nowNodeList,
                forecastServiceList, nowServiceList, results,podAddList,podDelList);
        Thread strategyThread = new Thread(strategyTaskThread);
        strategyThread.start();

        return results;
    }


    public static void run(Map<String, Long> podAddList,
                           Map<String, Long> podDelList) {
        Write2ES.run(getResults(podAddList, podDelList), "schedulePod");
    }
}
