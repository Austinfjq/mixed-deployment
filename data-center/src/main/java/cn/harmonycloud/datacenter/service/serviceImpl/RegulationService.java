package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.dao.IForecastResultCellDao;
import cn.harmonycloud.datacenter.dao.INodeDataDao;
import cn.harmonycloud.datacenter.dao.IPodDataDao;
import cn.harmonycloud.datacenter.dao.IServiceDataDao;
import cn.harmonycloud.datacenter.mapper.ServiceLoadMappingPodInstancesMapper;
import cn.harmonycloud.datacenter.mapper.ServiceMapper;
import cn.harmonycloud.datacenter.service.IRegulationService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import cn.harmonycloud.datacenter.entity.mysql.Service;

import java.util.List;
import java.util.Map;

/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/4/30 9:58
*@Modify By:
*/
@org.springframework.stereotype.Service
public class RegulationService implements IRegulationService {
    @Autowired
    private ServiceLoadMappingPodInstancesMapper serviceLoadMappingPodInstancesMapper;
    @Autowired
    private ServiceMapper serviceMapper;
    @Autowired
    private IPodDataDao podDataDao;
    @Autowired
    private INodeDataDao nodeDataDao;
    @Autowired
    private IServiceDataDao serviceDataDao;
    @Autowired
    private IForecastResultCellDao forecastResultCellDao;
    @Override
    public int getPodNumsUnderRequest(String clusterIp, String namespace, String serviceName, String serviceLoad) {
        return serviceLoadMappingPodInstancesMapper.getPodNumsUnderRequest(clusterIp,namespace,serviceName,serviceLoad);
    }

    @Override
    public int saveOneService(Service service) {
        return serviceMapper.saveOneService(service);
    }

    @Override
    public List<Map> getPodNamesByNodeAndService(String clusterMasterIP, String namespace, String serviceName, String nodeName) {
        return podDataDao.getPodNamesByNodeAndService(clusterMasterIP,namespace,serviceName,nodeName);
    }

    @Override
    public List<Map> getNodeByClusterMasterIP(String clusterMasterIP) {
        return podDataDao.getNodeByClusterMasterIP(clusterMasterIP);
    }

    @Override
    public List<Map> getOnlineServicesByClusterMasterIP(String clusterMasterIP) {
        return serviceDataDao.getOnlineServicesByClusterMasterIP(clusterMasterIP);
    }

    @Override
    public int getLastPeriodMaxRequestNums(String clusterMasterIP, String namespace, String serviceName, String startTime, String endTime) {
        return serviceDataDao.getLastPeriodMaxRequestNums(clusterMasterIP,namespace,serviceName,startTime,endTime);
    }

    @Override
    public double getNextPeriodMaxRequestNums(String clusterMasterIP, String namespace, String serviceName, String startTime, String endTime) {
        return forecastResultCellDao.getNextPeriodMaxRequestNums(clusterMasterIP+"&"+namespace+"&"+serviceName,startTime,endTime);
    }

    @Override
    public double getNodeCpuCores(String clusterMasterIP, String nodeName) {
        return (double) nodeDataDao.getFieldValueByClusterMasterIPAndNodeName(clusterMasterIP,nodeName,"cpuCores");
    }

    @Override
    public double getNodeMemMaxCapacity(String clusterMasterIP, String nodeName) {
        return (double) nodeDataDao.getFieldValueByClusterMasterIPAndNodeName(clusterMasterIP,nodeName,"memMaxCapacity");
    }

    @Override
    public double getLastPeriodMaxCpuUsage(String clusterMasterIP, String nodeName, String startTime, String endTime) {
        return (double) nodeDataDao.getLastPeriodMaxFiledValue(clusterMasterIP,nodeName,startTime,endTime,"cpuUsage");
    }

    @Override
    public double getLastPeriodMaxMemUsage(String clusterMasterIP, String nodeName, String startTime, String endTime) {
        return (double) nodeDataDao.getLastPeriodMaxFiledValue(clusterMasterIP,nodeName,startTime,endTime,"memUsage");
    }
}
