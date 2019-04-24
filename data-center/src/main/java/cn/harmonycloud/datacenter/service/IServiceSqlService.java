package cn.harmonycloud.datacenter.service;

import cn.harmonycloud.datacenter.entity.mysql.services;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/1/25 23:04
*@Modify By:
*/
@Service
public interface IServiceSqlService {
    //获取Service的管理方式信息
    public Map<String,Object> getOwnerTypeAndName(String namespace, String serviceName);
    public List<services> getServiceByClusterIp(String clusterIp);

}
