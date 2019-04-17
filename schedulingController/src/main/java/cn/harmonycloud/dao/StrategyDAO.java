package cn.harmonycloud.dao;

import cn.harmonycloud.beans.OfflineDilatationStrategy;
import cn.harmonycloud.beans.OfflineShrinkageStrategy;
import cn.harmonycloud.beans.OnlineStrategy;

/**
 * @classname：StrategyDAO
 * @author：WANGYUZHONG
 * @date：2019/4/11 11:27
 * @description:TODO
 * @version:1.0
 **/
public interface StrategyDAO {

    /**
     * @Author WANGYUZHONG
     * @Description //处理在线应用的调控策略
     * @Date 17:13 2019/4/10
     * @Param
     * @return
     **/
    boolean dealOnlineStrategy(OnlineStrategy onlineStrategy);

    /**
     * @Author WANGYUZHONG
     * @Description //处理离线应用的扩容策略
     * @Date 17:13 2019/4/10
     * @Param
     * @return
     **/
    boolean dealOfflineDilatationStrategy(OfflineDilatationStrategy offlineDilatationStrategy);

    /**
     * @Author WANGYUZHONG
     * @Description //处理离线应用的缩容策略
     * @Date 17:13 2019/4/10
     * @Param
     * @return
     **/
    boolean dealOfflineShrinkageStrategy(OfflineShrinkageStrategy offlineShrinkageStrategy);

}
