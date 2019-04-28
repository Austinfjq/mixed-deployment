package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.dao.INodeDataDao;
import cn.harmonycloud.datacenter.dao.IPodDataDao;
import cn.harmonycloud.datacenter.entity.es.NodeData;
import cn.harmonycloud.datacenter.entity.es.PodData;
import cn.harmonycloud.datacenter.repository.PodDataRepository;
import cn.harmonycloud.datacenter.service.IPodDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

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
    private ReentrantLock lock = new ReentrantLock();
    @Resource(name = "podDataDao")
    private IPodDataDao podDataDao;
    @Override
    public PodData saveOnePodData(PodData podData) {
        return podDataRepository.save(podData);
    }

    @Override
    public Iterable<PodData> saveAllPodDatas(List<PodData> podDatas) {
        Iterable<PodData> podData = null;
        lock.lock();
        try{
            podData = podDataRepository.saveAll(podDatas);
        }catch(Exception ex){

        }finally{
            lock.unlock();   //释放锁
        }
        return podData;
    }

    @Override
    public Optional<PodData> findById(String id) {
        return podDataRepository.findById(id);
    }

    @Override
    public List<PodData> findAllPodDatas() {
        return podDataDao.findAllPodDatas();
    }

    @Override
    public List<PodData> getNowPods() {
        List<PodData> podData = null;
        while(true){
            if(!lock.isLocked()){
                podData = podDataDao.getNowPods();
                break;
            }
        }

        return podData;
    }
}
