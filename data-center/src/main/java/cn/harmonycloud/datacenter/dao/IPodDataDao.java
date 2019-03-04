package cn.harmonycloud.datacenter.dao;

import cn.harmonycloud.datacenter.entity.es.NodeData;
import cn.harmonycloud.datacenter.entity.es.PodData;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/3/2 20:24
*@Modify By:
*/
@Repository
public interface IPodDataDao {
    public List<PodData> findAllPodDatas();
}
