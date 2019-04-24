package cn.harmonycloud.service;


import cn.harmonycloud.beans.Node;
import cn.harmonycloud.beans.OfflineDilatationStrategy;
import cn.harmonycloud.beans.OfflineShrinkageStrategy;

import java.util.List;

public interface IOfflineRegulateControl {

    /**
     * @Author WANGYUZHONG
     * @Description //调控模块处理一个节点
     * @Date 10:18 2019/4/11
     * @Param
     * @return
     **/
    boolean dealNode(Node node);

    /**
     * @Author WANGYUZHONG
     * @Description //
     * @Date 10:26 2019/4/11
     * @Param
     * @return
     **/
    int regulate(Node node);

    /**
     * @Author WANGYUZHONG
     * @Description //生成离线应用缩容策略
     * @Date 10:57 2019/4/11
     * @Param
     * @return
     **/
    List<OfflineShrinkageStrategy> productOfflineShrinkageStrategy(Node node, int regulateNums);


    /**
     * @Author WANGYUZHONG
     * @Description //生成离线应用扩容策略
     * @Date 10:57 2019/4/11
     * @Param
     * @return
     **/
    List<OfflineDilatationStrategy> productOfflineDilatationStrategy(Node node, int regulateNums);

    void process();
}
