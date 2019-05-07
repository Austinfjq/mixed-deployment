package cn.harmonycloud.datacenter.service.serviceImpl;

import cn.harmonycloud.datacenter.entity.es.SearchPod;
import cn.harmonycloud.datacenter.entity.mysql.Service;
import cn.harmonycloud.datacenter.entity.mysql.services;
import cn.harmonycloud.datacenter.mapper.ServiceLoadMappingPodInstancesMapper;
import cn.harmonycloud.datacenter.mapper.ServiceMapper;
import cn.harmonycloud.datacenter.service.IServiceSqlService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class ServiceSqlService implements IServiceSqlService {
    @Autowired
    private ServiceMapper serviceMapper;
    @Autowired
    private ServiceLoadMappingPodInstancesMapper serviceLoadMappingPodInstancesMapper;
    @Override
    public Map<String, Object> getOwnerTypeAndName(String namespace, String serviceName) {
        Service service = serviceMapper.findServiceByNamespaceAndServiceName(namespace,serviceName);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("ownerType",service.getOwnerType());
        resultMap.put("ownerName",service.getOwnerName());
        return resultMap;
    }
    @Override
    public List<services> getServiceByClusterIp(String clusterIp) {
        List<Service> service= serviceMapper.findServiceByClusterIp(clusterIp);
        List<services> serviceNodes=new ArrayList<services>();
        for(Service pd:service)
        {
            if(clusterIp.equals(pd.getClusterIp()))
            {
                services sn=new services();
                sn.setClusterIp(pd.getClusterIp());
                sn.setServiceName(pd.getServiceName());
                sn.setNamespace(pd.getNamespace());
                sn.setServiceType(String.valueOf(pd.getType()));
                System.out.print(sn.toString());
                serviceNodes.add(sn);
            }
        }
        return serviceNodes;
    }
    @Override
    public int getPodNumsByService(SearchPod searchPod){
        return serviceLoadMappingPodInstancesMapper.findPodNumsByService(searchPod.getClusterIp(),
                searchPod.getNamespace(),searchPod.getServiceName());
    }
}