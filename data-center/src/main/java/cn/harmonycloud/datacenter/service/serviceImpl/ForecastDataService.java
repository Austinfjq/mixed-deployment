package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.dao.INodeLoadDao;
import cn.harmonycloud.datacenter.dao.IServiceLoadDao;
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
}
