package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.dao.IServiceDataDao;
import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.ServiceData;
import cn.harmonycloud.datacenter.repository.ServiceDataRepository;
import cn.harmonycloud.datacenter.service.IServiceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.harmonycloud.datacenter.tools.Constant.TIME_INTERVAL;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:23
 *@Modify By:
 */

@Service
public class ServiceDataService implements IServiceDataService {
    @Autowired
    private ServiceDataRepository serviceDataRepository;

    @Resource(name = "serviceDataDao")
    private IServiceDataDao serviceDataDao;

    @Override
    public ServiceData saveOneServiceData(ServiceData serviceData) {
        return serviceDataRepository.save(serviceData);
    }

    @Override
    public Iterable<ServiceData> saveAllServiceDatas(List<ServiceData> serviceDatas) {
        return serviceDataRepository.saveAll(serviceDatas);
    }

    @Override
    public Optional<ServiceData> findById(String id) {
        return serviceDataRepository.findById(id);
    }

    @Override
    public Iterable<ServiceData> findAllServiceDatas() {
        return serviceDataRepository.findAll();
    }

    @Override
    public Double getIndexTimeSeries(String namespace, String serviceName, String indexName, String startTime, String endTime) {
        return serviceDataDao.getIndexTimeSeries(namespace,serviceName,indexName,startTime,endTime);
    }

    @Override
    public List<DataPoint> getIndexDatas(String id, String indexName, String startTime, String endTime) {
        String[] splits = id.split("&");
        String namespace = splits[0];
        String serviceName = splits[1];
        return serviceDataDao.getIndexDatas(namespace,serviceName,indexName,startTime,endTime);
    }

    @Override
    public List<String> getAllPodNameFromOneService(String namespace, String serviceName, String clusterIP) {
        return serviceDataDao.getAllPodNameFromOneService(namespace,serviceName,clusterIP);
    }

    @Override
    public Map<String, Object> getStorageVolume(String namespace, String serviceName, String clusterIP) {
        return serviceDataDao.getStorageVolume(namespace,serviceName,clusterIP);
    }

    @Override
    public List<String> getAllServiceNames(String namespace) {
        return serviceDataDao.getAllServiceNames(namespace);
    }

    @Override
    public Map<String, Object> getPodNums(String namespace, String serviceName, String clusterIP) {
        return serviceDataDao.getPodNums(namespace,serviceName,clusterIP);
    }

    @Override
    public Map<String, Object> getNetVolume(String namespace, String serviceName, String clusterIP) {
        return serviceDataDao.getNetVolume(namespace,serviceName,clusterIP);
    }

    @Override
    public List<Map<String,Object>> getResourceConsume(String namespace, String serviceName, String clusterIP, String startTime, String endTime) {
        return serviceDataDao.getResourceConsume(namespace,serviceName,clusterIP,startTime,endTime);
    }

    @Override
    public List<Map<String, Object>> getLoadMappingInstances(String namespace, String serviceName, String clusterIP, String startTime, String endTime) {
        return serviceDataDao.getLoadMappingInstances(namespace,serviceName,clusterIP,startTime,endTime);
    }

    @Override
    public Double getAvgResponseTime(String namespace, String serviceName, String clusterIP) {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = new Date();
        calendar.setTime(endTime);
        calendar.add(Calendar.SECOND,-TIME_INTERVAL);
        Date startTime = calendar.getTime();
        return serviceDataDao.getAvgResponseTime(namespace,serviceName,clusterIP,simpleDateFormat.format(startTime),simpleDateFormat.format(endTime));
    }
}
