package cn.harmonycloud.service;


import cn.harmonycloud.beans.OfflineDilatationStrategy;
import cn.harmonycloud.beans.OfflineShrinkageStrategy;
import cn.harmonycloud.beans.OnlineStrategy;

import java.util.List;

public interface IStrategyProduce {

    /**
     * @Author WANGYUZHONG
     * @Description //生成在线应用的扩容策略
     * @Date 17:01 2019/4/10
     * @Param
     * @return
     **/
    OnlineStrategy produceOnlineDilatationStrategy(String cluserIp, String namespace, String serviceName, int numbers);

    /**
     * @Author WANGYUZHONG
     * @Description //生成在线应用的缩容策略
     * @Date 17:01 2019/4/10
     * @Param
     * @return
     **/
    OnlineStrategy produceOnlineShrinkageStrategy(String cluserIp, String namespace, String serviceName, int numbers);


    /**
     * @Author WANGYUZHONG
     * @Description //生成在线应用的扩容策略
     * @Date 17:01 2019/4/10
     * @Param
     * @return
     **/
    List<OfflineDilatationStrategy> produceOfflineDilatationStrategy(String cluserIp, String namespace, String serviceName, String hostName, int numbers);


    /**
     * @Author WANGYUZHONG
     * @Description //生成在线应用的扩容策略
     * @Date 17:01 2019/4/10
     * @Param
     * @return
     **/
    List<OfflineShrinkageStrategy> produceOfflineShrinkageStrategy(String cluserIp, String namespace, String serviceName, String hostName, int numbers);

}
