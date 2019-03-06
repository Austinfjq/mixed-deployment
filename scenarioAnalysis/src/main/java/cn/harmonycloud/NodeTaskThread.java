package cn.harmonycloud;

import cn.harmonycloud.entry.*;

import java.util.ArrayList;
import java.util.List;

import static cn.harmonycloud.tools.Constant.CpuUsageMaxThreshold;
import static cn.harmonycloud.tools.Constant.CpuUsageMinThreshold;


public class NodeTaskThread implements Runnable {

    private List<ForecastNode> forecastNodeList;
    private List<NowNode> nowNodeList;
    private List<ForecastService> forecastServiceList;
    private List<NowService> nowServiceList;
    private List<Result> results;

    public NodeTaskThread(List<ForecastNode> forecastNodeList, List<NowNode> nowNodeList, List<ForecastService> forecastServiceList, List<NowService> nowServiceList, ArrayList<Result> results) {
        this.forecastNodeList = forecastNodeList;
        this.nowNodeList = nowNodeList;
        this.forecastServiceList = forecastServiceList;
        this.nowServiceList = nowServiceList;
        this.results = results;
    }

    @Override
    public void run() {

        for (NowNode nowNode : nowNodeList) {

            //查找离线服务
            String offlineSvcNamespace = "";
            String offlineSvcName = "";
            for (NowService nowService : nowServiceList) {
                if (nowService.getOnlineType().equals("offline")) {
                    offlineSvcNamespace = nowService.getNamespace();
                    offlineSvcName = nowService.getServiceName();
                }
            }

            if (nowNode.getCpuUsage() > CpuUsageMaxThreshold) {

                for (ForecastNode forecastNode : forecastNodeList) {
                    if (forecastNode.getNodeIp().equals(nowNode.getNodeIP()) &&
                            nowNode.getCpuUsage() > forecastNode.getCpuUsage()) {
                        Result resultPod = new Result(1, offlineSvcNamespace,
                                offlineSvcName, "1");
                        results.add(resultPod);
                    }
                }


            } else if (nowNode.getCpuUsage() < CpuUsageMinThreshold) {
                Result resultPod = new Result(0, offlineSvcNamespace,
                        offlineSvcName, "1");
                results.add(resultPod);
            }


        }

    }
}
