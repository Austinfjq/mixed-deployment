package cn.harmonycloud.datacenter.repository;

import cn.harmonycloud.datacenter.entity.es.SchedulePod;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulePodRepository extends ElasticsearchRepository<SchedulePod, String> {
}
