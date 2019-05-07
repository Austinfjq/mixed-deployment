package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.service.INodeDataService;
import cn.harmonycloud.datacenter.service.IServiceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*@Author: shaodilong
*@Description: 应用画像模块接口以及应用画像获取数据模块
*@Date: Created in 2019/5/6 19:18
*@Modify By:
*/
@RestController
public class ApplicationPortraitController {
    @Autowired
    private IServiceDataService serviceDataService;
    @Autowired
    private INodeDataService nodeDataService;
    /**
     * 获得服务-Pod层级关系接口
     *
     * @param namespace
     * @param serviceName
     * @param clusterIP
     * @return
     */
    @GetMapping("/service/allPodName")
    public List<String> getAllPodNameFromOneService(@RequestParam("namespace") String namespace,
                                                    @RequestParam("serviceName") String serviceName,
                                                    @RequestParam("clusterIP") String clusterIP){

        return serviceDataService.getAllPodNameFromOneService(namespace,serviceName,clusterIP);
    }

    /**
     * 获得Service的存储卷信息
     *
     * @param namespace
     * @param serviceName
     * @param clusterIP
     * @return
     */
    @GetMapping("/service/storageVolume")
    public Map<String,Object> getStorageVolume(@RequestParam("namespace") String namespace,
                                               @RequestParam("serviceName") String serviceName,
                                               @RequestParam("clusterIP") String clusterIP){

        return serviceDataService.getStorageVolume(namespace,serviceName,clusterIP);
    }

    /**
     * 获得应用-服务层级关系接口
     *
     * @param namespace
     * @return
     */
    @GetMapping("/application/allServiceNames")
    public List<String> getAllServiceNames(@RequestParam("namespace") String namespace){
        return serviceDataService.getAllServiceNames(namespace);
    }

    /**
     * 获得Node的Conditions
     *
     * @param nodeName
     * @param nodeIP
     * @return
     */
    @GetMapping("/node/conditions")
    public Map<String,Object> getNodeConditions(@RequestParam("nodeName") String nodeName,
                                                @RequestParam("nodeIP") String nodeIP){
        return nodeDataService.getNodeConditions(nodeName,nodeIP);
    }

    /**
     * 以下为应用画像获取数据接口
     */

    /**
     * 获得当前指定service的系统实时pod实例数
     *
     * @param namespace
     * @param serviceName
     * @param clusterIP
     * @return
     */
    @GetMapping("/service/podNums")
    public Map<String,Object> getPodNums(@RequestParam("namespace") String namespace,
                                         @RequestParam("serviceName") String serviceName,
                                         @RequestParam("clusterIP") String clusterIP){
        return serviceDataService.getPodNums(namespace,serviceName,clusterIP);
    }

    /**
     * 获取指定service的实时网络IO流量
     *
     * @param namespace
     * @param serviceName
     * @param clusterIP
     * @return
     */
    @GetMapping("/service/netVolume")
    public Map<String,Object> getNetVolume(@RequestParam("namespace") String namespace,
                                           @RequestParam("serviceName") String serviceName,
                                           @RequestParam("clusterIP") String clusterIP){
        return serviceDataService.getNetVolume(namespace,serviceName,clusterIP);
    }

    /**
     * 获得指定服务的相应时间段的CPU,mem,网络上行速率，网络下载速率值
     *
     * @param namespace
     * @param serviceName
     * @param clusterIP
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/service/resourceConsume")
    public List<Map> getResourceConsume(@RequestParam("namespace") String namespace,
                                        @RequestParam("serviceName") String serviceName,
                                        @RequestParam("clusterIP") String clusterIP,
                                        @RequestParam("startTime") String startTime,
                                        @RequestParam("endTime") String endTime){
        return serviceDataService.getResourceConsume(namespace,serviceName,clusterIP,startTime,endTime);
    }

    /**
     * 获得指定服务的相应时间段的服务单位时间请求数及实例数
     *
     * @param namespace
     * @param serviceName
     * @param clusterIP
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/service/loadMappingInstances")
    public List<Map> getLoadMappingInstances(@RequestParam("namespace") String namespace,
                                             @RequestParam("serviceName") String serviceName,
                                             @RequestParam("clusterIP") String clusterIP,
                                             @RequestParam("startTime") String startTime,
                                             @RequestParam("endTime") String endTime){
        return serviceDataService.getLoadMappingInstances(namespace,serviceName,clusterIP,startTime,endTime);
    }

    /**
     * 实时获得指定服务的单位时间请求数响应时间平均值（衡量Qos）
     *
     * @param namespace
     * @param serviceName
     * @param clusterIP
     * @return
     */
    @GetMapping("/service/responseTime")
    public Map<String,Object> getAvgResponseTime(@RequestParam("namespace") String namespace,
                                                 @RequestParam("serviceName") String serviceName,
                                                 @RequestParam("clusterIP") String clusterIP){
        double avgResponseTime = serviceDataService.getAvgResponseTime(namespace,serviceName,clusterIP);
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("responseTime",avgResponseTime);
        return responseMap;
    }
}
