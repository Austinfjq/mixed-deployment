package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.entity.mysql.services;
import cn.harmonycloud.datacenter.service.IServiceSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
*@Author: shaodilong
*@Description: 与Mysql数据库端service表有关的controller
*@Date: Created in 2019/1/25 22:55
*@Modify By:
*/
@RestController
public class ServiceSqlController {
    @Autowired
    private IServiceSqlService serviceSqlService;

/*    @GetMapping("/managements")
    public Map<String, Object> getOwnerTypeAndName(@RequestParam("namespace") String namespace,
                                                   @RequestParam("serviceName") String serviceName){
        return serviceSqlService.getOwnerTypeAndName(namespace,serviceName);
    }*/
    @GetMapping("/service/onlineServices")
    public List<services> getService(@RequestParam("clusterIp") String clusterIp)
    {
        return serviceSqlService.getServiceByClusterIp(clusterIp);
    }
}
