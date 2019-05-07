package cn.harmonycloud.datacenter.service;


import cn.harmonycloud.datacenter.entity.mysql.Service;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/4/30 9:55
*@Modify By:
*/
@org.springframework.stereotype.Service
public interface IRegulationService {
    //获取某个服务在一定请求量下所需的Pod实例数
    public int getPodNumsUnderRequest(String clusterIp, String namespace, String serviceName, String serviceLoad);
    //插入Service信息
    public int saveOneService(Service service);
    //获取某个节点上某个service的所有Pod实例数
    public List<Map> getPodNamesByNodeAndService(String clusterMasterIP, String namespace, String serviceName, String nodeName);
    //获取某个集群中所有的工作节点
    public List<Map> getNodeByClusterMasterIP(String clusterMasterIP);
    //获取某个集群下所有的在线应用
    public List<Map> getOnlineServicesByClusterMasterIP(String clusterMasterIP);
    //获取某个服务在过去一段时间的最大请求量
    public int getLastPeriodMaxRequestNums(String clusterMasterIP,String namespace,String serviceName,String startTime,String endTime);
    //获取某个服务在将来一段时间的最大请求量
    public double getNextPeriodMaxRequestNums(String clusterMasterIP,String namespace,String serviceName,String startTime,String endTime);
    //获取某个集群中某个工作节点的CPU总量
    public double getNodeCpuCores(String clusterMasterIP,String nodeName);
    //获取某个集群中某个工作节点的mem总量
    public double getNodeMemMaxCapacity(String clusterMasterIP,String nodeName);
    //获取某个工作节点在过去一段时间的最大cpu利用率
    public double getLastPeriodMaxCpuUsage(String clusterMasterIP,String nodeName,String startTime,String endTime);
    //获取某个工作节点在过去一段时间的最大内存利用率
    public double getLastPeriodMaxMemUsage(String clusterMasterIP,String nodeName,String startTime,String endTime);
}
