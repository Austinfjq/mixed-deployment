package cn.harmonycloud;

import cn.harmonycloud.entry.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;


public class StrategyTaskThread implements Runnable {
    private List<ForecastNode> forecastNodeList;
    private List<NowNode> nowNodeList;
    private List<ForecastService> forecastServiceList;
    private List<NowService> nowServiceList;
    private ArrayList<Result> results;
    private int onlineNum;
    private int offlineNum;

    public StrategyTaskThread(List<ForecastNode> forecastNodeList, List<NowNode> nowNodeList, List<ForecastService> forecastServiceList, List<NowService> nowServiceList, ArrayList<Result> results, int onlineNum, int offlineNum) {
        this.forecastNodeList = forecastNodeList;
        this.nowNodeList = nowNodeList;
        this.forecastServiceList = forecastServiceList;
        this.nowServiceList = nowServiceList;
        this.results = results;
        this.onlineNum = onlineNum;
        this.offlineNum = offlineNum;

    }

    @Override
    public void run() {

        AppList appList = new AppList();
        appList.init();

//        if (podDelList.containsKey("offline") || podDelList.containsKey("online")) {
//        if (podDelList.containsKey("offline")) {
//            boolean delOffline = podDelList.containsKey("offline");
////            long podNum = delOffline ? podDelList.get("offline") : podDelList.get("online");
//
//            long podNum = podDelList.get("offline");
//            for (int i = 0; i < podNum; i++) {
//                for (NowService nowService : nowServiceList) {
////                    if ((nowService.isOffline() && delOffline) || (!nowService.isOffline() && !delOffline)) {
//
////                    if ((nowService.getOffline() == 1) && delOffline && nowService.getNamespace().equals("hadoop")) {
//                    if ((nowService.getOnlineType().equals("offline")) && delOffline) {
//                        Result resultPod = new Result(1, nowService.getNamespace(),
//                                nowService.getServiceName(), "1");
//                        results.add(resultPod);
//                        nowServiceList.remove(nowService);
//                    }
//                }
//            }
//        } else if (podAddList.containsKey("offline") ||
//                podAddList.containsKey("online")) {
//
//            boolean addOffline = podAddList.containsKey("offline");
//            long podNum = addOffline ? podAddList.get("offline") : podAddList.get("online");
//
//            for (int i = 0; i < podNum; i++) {
//                for (NowService nowService : nowServiceList) {
//                    if (((nowService.getOnlineType().equals("offline")) && addOffline) &&
//                            appList.getOfflineApp().contains(nowService.getServiceName()) ||
//                            ((nowService.getOnlineType().equals("online")) && !addOffline) &&
//                                    appList.getOnlineApp().contains(nowService.getServiceName())) {
////                    if (!nowService.isOffline() && !addOffline) {
//
//                        Result resultPod = new Result(0, nowService.getNamespace(),
//                                nowService.getServiceName(), "1");
//                        results.add(resultPod);
//                        nowServiceList.remove(nowService);
//
//                    }
//                }
//            }
//        }

        int podNum = 0;

        for (int i = 0; i < appList.getOnlineApp().size(); i++) {

            for (NowService nowService : nowServiceList) {
                if (nowService.getServiceName().equals(appList.getOnlineApp().get(i).get("name"))
                        && nowService.getNamespace().equals(appList.getOnlineApp().get(i).get("namespace"))) {
                    podNum = nowService.getPodNums() - onlineNum;
                    break;
                }
            }

            Result resultPod = new Result(podNum < 0 ? 1 : 2, appList.getOnlineApp().get(i).get("namespace"),
                    appList.getOnlineApp().get(i).get("name"), String.valueOf(abs(podNum)));//1增加，2减少
            results.add(resultPod);

        }

        for (int i = 0; i < appList.getOfflineApp().size(); i++) {

            for (NowService nowService : nowServiceList) {
                if (nowService.getServiceName().equals(appList.getOfflineApp().get(i).get("name"))
                        && nowService.getNamespace().equals(appList.getOfflineApp().get(i).get("namespace"))) {
                    podNum = nowService.getPodNums() - offlineNum;
                    break;
                }
            }

            Result resultPod = new Result(podNum < 0 ? 1 : 2, appList.getOfflineApp().get(i).get("namespace"),
                    appList.getOfflineApp().get(i).get("name"), String.valueOf(abs(podNum)));//1增加，2减少
            results.add(resultPod);

        }

    }
}
