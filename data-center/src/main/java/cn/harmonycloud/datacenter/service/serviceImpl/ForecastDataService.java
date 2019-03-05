package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.dao.INodeLoadDao;
import cn.harmonycloud.datacenter.dao.IServiceLoadDao;
import cn.harmonycloud.datacenter.entity.NodeLoad;
import cn.harmonycloud.datacenter.entity.ServiceLoad;
import cn.harmonycloud.datacenter.service.IForecastDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ForecastDataService implements IForecastDataService {
    @Autowired
    private IServiceLoadDao serviceLoadDao;
    @Autowired
    private INodeLoadDao nodeLoadDao;

    @Override
    public List<Object> getAllForecastValue(String startTime, String endTime) {
        List<Object> lists = new ArrayList<>();
        lists.addAll(serviceLoadDao.getServiceLoadForecastValues(startTime,endTime));
        lists.addAll(nodeLoadDao.getNodeLoadForecastValues(startTime,endTime));
        return lists;
    }

    @Override
    public List<ServiceLoad> getAllServiceLoads(String startTime, String endTime) {
        return serviceLoadDao.getServiceLoadForecastValues(startTime,endTime);
    }

    @Override
    public List<NodeLoad> getAllNodeLoads(String startTime, String endTime) {
        return nodeLoadDao.getNodeLoadForecastValues(startTime,endTime);
    }
}
