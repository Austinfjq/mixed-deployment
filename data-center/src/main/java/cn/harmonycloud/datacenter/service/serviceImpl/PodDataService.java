package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.entity.es.PodData;
import cn.harmonycloud.datacenter.repository.PodDataRepository;
import cn.harmonycloud.datacenter.service.IPodDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:23
 *@Modify By:
 */

@Service
public class PodDataService implements IPodDataService {
    @Autowired
    private PodDataRepository podDataRepository;

    @Override
    public PodData saveOnePodData(PodData podData) {
        return podDataRepository.save(podData);
    }

    @Override
    public Iterable<PodData> saveAllPodDatas(List<PodData> podDatas) {
        return podDataRepository.saveAll(podDatas);
    }

    @Override
    public Optional<PodData> findById(String id) {
        return podDataRepository.findById(id);
    }

    @Override
    public Iterable<PodData> findAllPodDatas() {
        return podDataRepository.findAll();
    }
}
