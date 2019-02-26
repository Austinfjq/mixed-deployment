package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.mapper.ServiceMapper;
import cn.harmonycloud.datacenter.service.IServiceSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import cn.harmonycloud.datacenter.entity.mysql.Service;

import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Service
public class ServiceSqlService implements IServiceSqlService {
    @Autowired
    private ServiceMapper serviceMapper;
    @Override
    public Map<String, Object> getOwnerTypeAndName(String namespace, String serviceName) {
        Service service = serviceMapper.findServiceByNamespaceAndServiceName(namespace,serviceName);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("ownerType",service.getOwnerType());
        resultMap.put("ownerName",service.getOwnerName());
        return resultMap;
    }
}
