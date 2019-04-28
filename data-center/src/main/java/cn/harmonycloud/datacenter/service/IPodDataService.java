package cn.harmonycloud.datacenter.service;

import cn.harmonycloud.datacenter.entity.es.PodData;
import cn.harmonycloud.datacenter.entity.es.ServiceData;
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
public interface IPodDataService {
    public PodData saveOnePodData(PodData podData);
    public Iterable<PodData> saveAllPodDatas(List<PodData> podDatas);
    public Optional<PodData> findById(String id);
    public List<PodData> findAllPodDatas();

    //获取当前service数据
    public List<PodData> getNowPods();
}
