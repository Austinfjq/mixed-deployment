package cn.harmonycloud.service;

import cn.harmonycloud.beans.OnlineStrategy;
import cn.harmonycloud.beans.Service;

public interface IOnlineRegulateControl {


    /**
     * @Author WANGYUZHONG
     * @Description //调控分析某一个service
     * @Date 21:10 2019/4/10
     * @Param
     * @return
     **/
    boolean dealService(Service service);


    /**
     * @Author WANGYUZHONG
     * @Description //分析某个服务的调整实例数
     * @Date 20:13 2019/4/10
     * @Param
     * @return
     **/
    int regulate(Service service);


    /**
     * @Author WANGYUZHONG
     * @Description //生成调控策略
     * @Date 20:13 2019/4/10
     * @Param
     * @return
     **/
    OnlineStrategy productOnlineStrategy(Service service, int regulateNums);


    void process();

}
