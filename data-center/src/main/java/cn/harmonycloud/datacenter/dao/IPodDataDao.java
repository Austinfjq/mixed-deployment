package cn.harmonycloud.datacenter.dao;

import cn.harmonycloud.datacenter.entity.es.PodData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/3/2 20:24
*@Modify By:
*/
@Repository
public interface IPodDataDao {
    public List<PodData> findAllPodDatas();

    public List<PodData> getNowPods();

    public List<Map> getPodNamesByNodeAndService(String clusterMasterIP, String namespace, String serviceName, String nodeName);

    public List<Map> getNodeByClusterMasterIP(String clusterMasterIP);
}
