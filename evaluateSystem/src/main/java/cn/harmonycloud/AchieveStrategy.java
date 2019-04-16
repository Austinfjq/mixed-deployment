package cn.harmonycloud;

import cn.harmonycloud.beans.EvaluateStrategy;
import cn.harmonycloud.tools.XMLUtil;

import java.util.List;

/**
 * @author wangyuzhong
 * @date 19-1-22 下午2:24
 * @Despriction
 */
public class AchieveStrategy {

    private static List<EvaluateStrategy> nodeStrategies = null;

    private static List<EvaluateStrategy> serviceStrategies = null;

    private static List<EvaluateStrategy> clusterStrategies = null;


    public static void inital() {
        nodeStrategies = XMLUtil.getNodeStrategys();
        serviceStrategies = XMLUtil.getServiceStrategys();
        clusterStrategies = XMLUtil.getClusterStrategys();
    }

    public static List<EvaluateStrategy> getNodeStrategies(){
        if (null == nodeStrategies) {
            inital();
        }
        return nodeStrategies;
    }

    public static List<EvaluateStrategy> getServiceStrategies(){
        if (null == serviceStrategies) {
            inital();
        }
        return serviceStrategies;
    }

    public static List<EvaluateStrategy> getClusterStrategies(){
        if (null == clusterStrategies) {
            inital();
        }
        return clusterStrategies;
    }
}
