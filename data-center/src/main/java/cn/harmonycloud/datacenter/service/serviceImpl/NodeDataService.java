package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.controller.NodeDataController;
import cn.harmonycloud.datacenter.dao.INodeDataDao;
import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.NodeData;
import cn.harmonycloud.datacenter.repository.NodeDataRepository;
import cn.harmonycloud.datacenter.service.INodeDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:22
 *@Modify By:
 */

@Service
public class NodeDataService implements INodeDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeDataService.class);
    private ReentrantLock lock = new ReentrantLock();
    @Autowired
    private NodeDataRepository nodeDataRepository;
    @Resource(name = "nodeDataDao")
    private INodeDataDao nodeDataDao;
    @Override
    public NodeData saveOneNodeData(NodeData nodeData) {
        return nodeDataRepository.save(nodeData);
    }

    @Override
    public Iterable<NodeData> saveAllNodeDatas(List<NodeData> nodeDatas){
        Iterable<NodeData> nodeData = null;
        lock.lock();
        try{
            nodeData = nodeDataRepository.saveAll(nodeDatas);
        }catch(Exception ex){

        }finally{
            lock.unlock();   //释放锁
        }
        return nodeData;

    }

    @Override
    public Optional<NodeData> findById(String id) {
        return nodeDataRepository.findById(id);
    }

    @Override
    public List<NodeData> findAllNodeDatas() {
        return nodeDataDao.findAllNodeDatas();
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
    public List<NodeData> getNowNodes() {
        List<NodeData> nodeData = null;
        while(true){
            if(!lock.isLocked()){
                nodeData = nodeDataDao.getNowNodes();
                break;
            }
        }

        return nodeData;
    }
}
