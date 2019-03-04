package cn.harmonycloud.datacenter.repository.test;

import cn.harmonycloud.datacenter.entity.test.ResultNode;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultNodeRepository extends ElasticsearchRepository<ResultNode,String> {
}
