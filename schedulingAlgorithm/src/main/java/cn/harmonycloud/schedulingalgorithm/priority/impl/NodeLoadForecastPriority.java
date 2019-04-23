package cn.harmonycloud.schedulingalgorithm.priority.impl;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.NodeForecastData;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.priority.DefaultPriorityRule;

import java.util.Map;

public class NodeLoadForecastPriority implements DefaultPriorityRule {

    private int operation;

    public NodeLoadForecastPriority(int op) {
        operation = op;
    }

    @Override
    public Integer priority(Pod pod, Node node, Cache cache) {
        // 已弃用
        if (true) {
            return 0;
        }
        // now Request resource: nodeInfo.RequestedResource().MilliCPU, and mem
        // next period resource: from node load forecast, format: cpu(0~1.0), mem(xMB)
        // score = (cpu now - cpu next) * weightA + (mem now - mem next) * weightB.
        // when deleting, score *= -1
        Map<String, NodeForecastData> forecastMap = cache.getNodeForecastMap();
        if (forecastMap == null || !forecastMap.containsKey(node.getNodeIP())) {
            return GlobalSetting.PRIORITY_MAX_SCORE / 2;
        }
        NodeForecastData forecastResource = forecastMap.get(node.getNodeIP());
        double cpuScore = ((node.getCpuUsage() * node.getCpuCores() - (double) forecastResource.getCpuUsage()) / pod.getCpuRequest());
        double memScore = ((node.getMemUsage() * node.getMemMaxCapacity() - (double) forecastResource.getMemUsage()) / pod.getMemRequest());

        cpuScore = normalize(cpuScore);
        memScore = normalize(memScore);

        int score = (int) ((1 + (cpuScore + memScore) / 2) / 2 * GlobalSetting.PRIORITY_MAX_SCORE);

        if (operation == Constants.OPERATION_DELETE) {
            return GlobalSetting.PRIORITY_MAX_SCORE - score;
        } else {
            return score;
        }
    }

    private double normalize(double a) {
        if (a > 1D) {
            a = 1D;
        } else if (a < -1D) {
            a = -1D;
        }
        return a;
    }
}
