package cn.harmonycloud.datacenter.service;

import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.NodeData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:23
 *@Modify By:
 */

@Service
public interface INodeDataService {
    public NodeData saveOneNodeData(NodeData nodeData);
    public Iterable<NodeData> saveAllNodeDatas(List<NodeData> nodeDatas);
    public Optional<NodeData> findById(String id);
    public List<NodeData> findAllNodeDatas();

    //获取node某个指标的历史数据值
    public List<DataPoint> getIndexDatas(String id, String indexName, String startTime, String endTime);
    //获得Node的Conditions
    public Map<String,Object> getNodeConditions(String nodeName, String nodeIP);
    //获得当前时间的Node
    public List<Map> getNowNodes();
}
