package cn.harmonycloud.datacenter.dao;

import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.NodeData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:20
 *@Modify By:
 */

@Repository
public interface INodeDataDao {
    public List<NodeData> findAllNodeDatas();
    //获取node某个指标的历史数据值
    public List<DataPoint> getIndexDatas(String clusterMasterIP, String nodeName, String indexName, String startTime, String endTime);
    //获得Node的Conditions
    public Map<String,Object> getNodeConditions(String nodeName, String nodeIP);
    //获得当前时间的Node
    public List<NodeData> getNowNodes();
    //获取某个集群中某个工作节点的某个属性值（cpu,mem...）
    public Object getFieldValueByClusterMasterIPAndNodeName(String clusterMasterIP, String nodeName, String fieldName);
    //取某个工作节点在过去一段时间的最大属性值
    public Object getLastPeriodMaxFiledValue(String clusterMasterIP,String nodeName,String startTime,String endTime,String fieldName);
}
