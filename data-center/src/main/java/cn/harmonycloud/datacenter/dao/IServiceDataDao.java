package cn.harmonycloud.datacenter.dao;

import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.ServiceData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:21
 *@Modify By:
 */

@Repository
public interface IServiceDataDao {
    public List<ServiceData> findAllServiceDatas();
    //获得某个在线服务的某个指标在某段时间段的值
    public Double getIndexTimeSeries(String namespace, String serviceName, String indexName, String startTime, String endTime);
    //获取service某个指标的历史数据值
    public List<DataPoint> getIndexDatas(String namespace, String serviceName, String indexName, String startTime, String endTime);
    //返回指定Services包括的Pod实例名
    public List<String> getAllPodNameFromOneService(String namespace,String serviceName,String clusterIP);
    //获得Service的存储卷信息接口
    public Map<String, Object> getStorageVolume(String namespace, String serviceName, String clusterIP);
    //获得应用-服务层级关系接口
    public List<String> getAllServiceNames(String namespace);

    //获得当前指定service的系统实时pod实例数
    public Map<String,Object> getPodNums(String namespace,String serviceName,String clusterIP);
    //获取指定service的实时网络IO流量
    public Map<String,Object> getNetVolume(String namespace,String serviceName, String clusterIP);
    //获得指定服务的相应时间段的CPU,mem,网络上行速率，网络下载速率值
    public List<Map> getResourceConsume(String namespace,String serviceName,String clusterIP,String startTime,String endTime);
    //获得指定服务的相应时间段的服务单位时间请求数及实例数
    public List<Map> getLoadMappingInstances(String namespace,String serviceName,String clusterIP,String startTime,String endTime);
    //实时获得指定服务的单位时间请求数响应时间平均值（衡量Qos）
    public Double getAvgResponseTime(String namespace,String serviceName,String clusterIP,String startTime,String endTime);
    //获取当前service数据
    public List<Map> getNowServices();
}
