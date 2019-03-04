package cn.harmonycloud.datacenter.repository.test;

import cn.harmonycloud.datacenter.entity.test.ResultPod;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultPodRepository extends ElasticsearchRepository<ResultPod, String> {
}
