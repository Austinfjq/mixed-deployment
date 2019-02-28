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
    //获取node某个指标的历史数据值
    public List<DataPoint> getIndexDatas(String nodeName, String nodeIP, String indexName, String startTime, String endTime);
    //获得Node的Conditions
    public Map<String,Object> getNodeConditions(String nodeName, String nodeIP);
    //获得当前时间的Node
    public List<Map<String,Object>> getNowNodes(String now);
}
