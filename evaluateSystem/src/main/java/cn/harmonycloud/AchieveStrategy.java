package cn.harmonycloud;

import cn.harmonycloud.strategy.AbstractClusterStrategy;
import cn.harmonycloud.strategy.AbstractNodeStrategy;
import cn.harmonycloud.strategy.AbstractServiceStrategy;
import cn.harmonycloud.tools.XMLUtil;

import java.util.List;

/**
 * @author wangyuzhong
 * @date 19-1-22 下午2:24
 * @Despriction
 */
public class AchieveStrategy {

    private static List<AbstractNodeStrategy> abstractNodeStrategies = null;

    private static List<AbstractServiceStrategy> abstractServiceStrategies = null;

    private static List<AbstractClusterStrategy> abstractClusterStrategies = null;


    public static void inital() {
        abstractNodeStrategies = XMLUtil.getNodeStrategys();
        abstractServiceStrategies = XMLUtil.getServiceStrategys();
        abstractClusterStrategies = XMLUtil.getClusterStrategys();
    }

    public static List<AbstractNodeStrategy> getAbstractNodeStrategies(){
        if (null == abstractNodeStrategies) {
            inital();
        }
        return abstractNodeStrategies;
    }

    public static List<AbstractServiceStrategy> getAbstractServiceStrategies(){
        if (null == abstractServiceStrategies) {
            inital();
        }
        return abstractServiceStrategies;
    }

    public static List<AbstractClusterStrategy> getAbstractClusterStrategies(){
        if (null == abstractClusterStrategies) {
            inital();
        }
        return abstractClusterStrategies;
    }


}
