package cn.harmonycloud.dao;


import cn.harmonycloud.beans.Node;

import java.util.List;

public interface NodeDAO {

    /**
     * @Author WANGYUZHONG
     * @Description //获取某个节点上某个服务的所有Pod实例
     * @Date 17:59 2019/4/10
     * @Param
     * @return
     **/
    List<String> getPodNameListOfHost(String clusterIp, String namespace, String serviceName, String hostName);

    /**
     * @Author WANGYUZHONG
     * @Description //获取某个集群中所有的工作节点
     * @Date 10:08 2019/4/11
     * @Param
     * @return
     **/
    List<Node> getNodeList(String clusterIp);

    /**
     * @Author WANGYUZHONG
     * @Description //获取节点CPU总量
     * @Date 10:08 2019/4/11
     * @Param
     * @return 核为单位
     **/
    double getNodeCpuTotal(String clusterIp, String hostName);

    /**
     * @Author WANGYUZHONG
     * @Description //获取节点内存总量
     * @Date 10:08 2019/4/11
     * @Param
     * @return M为单位
     **/
    double getNodeMemTotal(String clusterIp, String hostName);

    /**
     * @Author WANGYUZHONG
     * @Description //获取某个节点过去一个调控周期的cpu利用率最大值
     * @Date 15:08 2019/4/10
     * @Param
     * @return
     **/
    double getLastPeriodMaxCpuUsage(String masterIp, String hostName, String startTime, String endTime);

    /**
     * @Author WANGYUZHONG
     * @Description //获取某个节点过去一个调控周期的内存利用率最大值
     * @Date 15:08 2019/4/10
     * @Param
     * @return
     **/
    double getLastPeriodMaxMemUsage(String masterIp, String hostName, String startTime, String endTime);

}
