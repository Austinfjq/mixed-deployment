package cn.harmonycloud;

import cn.harmonycloud.entry.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StrategyTaskThread implements Runnable {
    private List<ForecastNode> forecastNodeList;
    private List<NowNode> nowNodeList;
    private List<ForecastService> forecastServiceList;
    private List<NowService> nowServiceList;
    private ArrayList<Result> results;
    private Map<String, Long> podAddList;
    private Map<String, Long> podDelList;

    public StrategyTaskThread(List<ForecastNode> forecastNodeList, List<NowNode> nowNodeList, List<ForecastService> forecastServiceList, List<NowService> nowServiceList, ArrayList<Result> results, Map<String, Long> podAddList, Map<String, Long> podDelList) {
        this.forecastNodeList = forecastNodeList;
        this.nowNodeList = nowNodeList;
        this.forecastServiceList = forecastServiceList;
        this.nowServiceList = nowServiceList;
        this.results = results;
        this.podAddList = podAddList;
        this.podDelList = podDelList;

    }

    @Override
    public void run() {

//        if (podDelList.containsKey("offline") || podDelList.containsKey("online")) {
        if (podDelList.containsKey("offline")) {
            boolean delOffline = podDelList.containsKey("offline");
//            long podNum = delOffline ? podDelList.get("offline") : podDelList.get("online");

            long podNum = podDelList.get("offline");
            for (int i = 0; i < podNum; i++) {
                for (NowService nowService : nowServiceList) {
//                    if ((nowService.isOffline() && delOffline) || (!nowService.isOffline() && !delOffline)) {

                    if (nowService.isOffline() && delOffline && nowService.getNamespace().equals("hadoop")) {
                        Result resultPod = new Result(1, nowService.getNamespace(),
                                nowService.getServiceName(), "1");
                        results.add(resultPod);
                        nowServiceList.remove(nowService);
                    }
                }
            }
        } else if (podAddList.containsKey("offline") || podAddList.containsKey("online")) {

            boolean addOffline = podAddList.containsKey("offline");
            long podNum = addOffline ? podAddList.get("offline") : podAddList.get("online");

            for (int i = 0; i < podNum; i++) {
                for (NowService nowService : nowServiceList) {
                    if ((nowService.isOffline() && addOffline) || (!nowService.isOffline() && !addOffline)) {
//                    if (!nowService.isOffline() && !addOffline) {
                        if (nowService.getNamespace().equals("hadoop") || nowService.getNamespace().equals("wordpress")) {
                            Result resultPod = new Result(0, nowService.getNamespace(),
                                    nowService.getServiceName(), "1");
                            results.add(resultPod);
                            nowServiceList.remove(nowService);
                        }
                    }
                }
            }
        }
    }
}
