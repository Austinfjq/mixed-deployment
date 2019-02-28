package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.dao.INodeDataDao;
import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.NodeData;
import cn.harmonycloud.datacenter.repository.NodeDataRepository;
import cn.harmonycloud.datacenter.service.INodeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:22
 *@Modify By:
 */

@Service
public class NodeDataService implements INodeDataService {
    @Autowired
    private NodeDataRepository nodeDataRepository;
    @Resource(name = "nodeDataDao")
    private INodeDataDao nodeDataDao;
    @Override
    public NodeData saveOneNodeData(NodeData nodeData) {
        return nodeDataRepository.save(nodeData);
    }

    @Override
    public Iterable<NodeData> saveAllNodeDatas(List<NodeData> nodeDatas) {
        return nodeDataRepository.saveAll(nodeDatas);
    }

    @Override
    public Optional<NodeData> findById(String id) {
        return nodeDataRepository.findById(id);
    }

    @Override
    public Iterable<NodeData> findAllNodeDatas() {
        return nodeDataRepository.findAll();
    }

    @Override
    public List<DataPoint> getIndexDatas(String id, String indexName, String startTime, String endTime) {
        String[] splits = id.split("&");
        String nodeName = splits[0];
        String nodeIp = splits[1];

        return nodeDataDao.getIndexDatas(nodeName,nodeIp,indexName,startTime,endTime);
    }

    @Override
    public Map<String, Object> getNodeConditions(String nodeName, String nodeIP) {
        return nodeDataDao.getNodeConditions(nodeName,nodeIP);
    }

    @Override
    public List<Map<String, Object>> getNowNodes(String now) {
        return nodeDataDao.getNowNodes(now);
    }
}
