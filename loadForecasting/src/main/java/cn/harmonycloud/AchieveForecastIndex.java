package cn.harmonycloud;

import cn.harmonycloud.beans.ForecastIndex;
import cn.harmonycloud.tools.XMLUtil;

import java.util.List;

/**
 * @author wangyuzhong
 * @date 19-1-22 下午2:24
 * @Despriction
 */
public class AchieveForecastIndex {

    private static List<ForecastIndex> nodeForecastIndexs = null;

    private static List<ForecastIndex> serviceForecastIndexs = null;


    public static void inital() {
        nodeForecastIndexs = XMLUtil.getNodeForecastIndex();
        serviceForecastIndexs = XMLUtil.getServiceForecastIndex();
    }

    public static List<ForecastIndex> getNodeForecastIndexs(){
        if (null == nodeForecastIndexs) {
            inital();
        }
        return nodeForecastIndexs;
    }

    public static List<ForecastIndex> getServiceForecastIndexs(){
        if (null == serviceForecastIndexs) {
            inital();
        }
        return serviceForecastIndexs;
    }

}
