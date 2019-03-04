package cn.harmonycloud.datacenter.repository;

import cn.harmonycloud.datacenter.entity.es.NodeData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:22
 *@Modify By:
 */

@Repository
public interface NodeDataRepository extends ElasticsearchRepository<NodeData, String> {

}
