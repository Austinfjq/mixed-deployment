package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.service.IServiceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
*@Author: shaodilong
*@Description: Scheduler接口
*@Date: Created in 2019/5/6 19:35
*@Modify By:
*/
@RestController
public class SchedulerController {
    @Autowired
    private IServiceDataService serviceDataService;

    /**
     * 获取Service的管理方式信息
     *
     * @param namespace
     * @param serviceName
     * @return
     */
    @GetMapping("/management")
    public Map<String,Object> getManagement(@RequestParam("namespace") String namespace,
                                            @RequestParam("serviceName") String serviceName,
                                            @RequestParam("clusterMasterIP") String clusterMasterIP){
        return serviceDataService.getManagement(namespace,serviceName,clusterMasterIP);
    }
}
