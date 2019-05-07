package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.NodeData;
import cn.harmonycloud.datacenter.entity.es.PodData;
import cn.harmonycloud.datacenter.entity.es.ServiceData;
import cn.harmonycloud.datacenter.entity.mysql.Service;
import cn.harmonycloud.datacenter.service.INodeDataService;
import cn.harmonycloud.datacenter.service.IPodDataService;
import cn.harmonycloud.datacenter.service.IRegulationService;
import cn.harmonycloud.datacenter.service.IServiceDataService;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*@Author: shaodilong
*@Description: 调控模块接口
*@Date: Created in 2019/4/30 9:49
*@Modify By:
*/
@RestController
public class RegulationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegulationController.class);
    @Autowired
    private IRegulationService regulationService;
    @Autowired
    private INodeDataService nodeDataService;
    @Autowired
    private IServiceDataService serviceDataService;
    @Autowired
    private IPodDataService podDataService;
    /**
     * 获取当前node数据
     *
     * @return
     */
    @GetMapping("/nowNode")
    public List<NodeData> getNowNodes(){
        return nodeDataService.getNowNodes();
    }

    /**
     * 获取当前service数据
     *
     * @return
     */
    @GetMapping("/nowService")
    public List<ServiceData> getNowServices(){
        return serviceDataService.getNowServices();
    }

    /**
     * 获取当前pod数据
     *
     * @return
     */
    @GetMapping("/nowPod")
    public List<PodData> getNowPods(){
        List<PodData> pods = podDataService.getNowPods();
        // TODO: 需修改实现
        for(PodData podData:pods){
            podData.changeLabels();
        }
        return pods;
    }

    /**
     * 获取某个服务在一定请求量下所需的Pod实例数
     * @param clusterIp
     * @param namespace
     * @param serviceName
     * @param serviceLoad
     * @return
     */
    @GetMapping("/service/requestPodNums")
    public Map<String,Object> getPodNumsUnderRequest(@RequestParam("clusterIp")String clusterIp,
                                                     @RequestParam("namespace")String namespace,
                                                     @RequestParam("serviceName")String serviceName,
                                                     @RequestParam("requestNums")String serviceLoad){
        Map<String,Object> responseMap = new HashMap<>();
        int podNums = regulationService.getPodNumsUnderRequest(clusterIp,namespace,serviceName,serviceLoad);
        responseMap.put("podNums",podNums);
        return responseMap;
    }

    /**
     * 插入Service信息
     * 将新建的Service插入到数据库中(Mysql的Service表)．
     *
     * @param clusterIp
     * @param namespace
     * @param serviceName
     * @param type
     * @return
     */
    @PutMapping("/service/service")
    public Map<String,Object> saveOneService(@RequestParam("clusterIp")String clusterIp,
                                             @RequestParam("namespace")String namespace,
                                             @RequestParam("serviceName")String serviceName,
                                             @RequestParam("serviceType")int type){
        Map<String,Object> responseMap = new HashMap<>();
        Service service = new Service();
        service.setClusterIp(clusterIp);
        service.setNamespace(namespace);
        service.setServiceName(serviceName);
        service.setType(type);
        int result = regulationService.saveOneService(service);
        if(result > 0){
            LOGGER.info("Succeed to insert service to mysql(service table)");
            responseMap.put("isSucceed",true);
        }else{
            LOGGER.error("Fail to insert service to mysql(service table)");
            responseMap.put("isSucceed",false);
        }
        return responseMap;
    }

    /**
     * 获取某个节点上某个service的所有Pod实例名
     *
     * @param clusterMasterIP
     * @param namespace
     * @param serviceName
     * @param nodeName
     * @return
     */
    @GetMapping("/node/service/pods")
    public List<Map> getPodNamesByNodeAndService(@RequestParam("clusterIp")String clusterMasterIP,
                                                  @RequestParam("namespace")String namespace,
                                                  @RequestParam("serviceName")String serviceName,
                                                  @RequestParam("hostName")String nodeName){
        return regulationService.getPodNamesByNodeAndService(clusterMasterIP,namespace,serviceName,nodeName);
    }

    /**
     * 获取某个集群中所有的工作节点
     *
     * @param clusterMasterIP
     * @return
     */
    @GetMapping("/node/nodes")
    public List<Map> getNodeByClusterMasterIP(@RequestParam("clusterIp")String clusterMasterIP){
        return regulationService.getNodeByClusterMasterIP(clusterMasterIP);
    }

    /**
     * 获取某个集群下所有的在线应用
     *
     * @param clusterMasterIP
     * @return
     */
    @GetMapping("/service/onlineServices")
    public List<Map> getOnlineServicesByClusterMasterIP(@RequestParam("clusterIp")String clusterMasterIP){
        return regulationService.getOnlineServicesByClusterMasterIP(clusterMasterIP);
    }

    /**
     * 获取某个服务在过去一段时间的最大请求量
     *
     * @param clusterMasterIP
     * @param namespace
     * @param serviceName
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/service/lastPeriodMaxRequestNums")
    public Map<String,Object> getLastPeriodMaxRequestNums(@RequestParam("clusterIp")String clusterMasterIP,
                                                          @RequestParam("namespace")String namespace,
                                                          @RequestParam("serviceName")String serviceName,
                                                          @RequestParam("startTime")String startTime,
                                                          @RequestParam("endTime")String endTime){
        Map<String,Object> resultMap = new HashMap<>();
        double lastPeriodMaxRequestNums = regulationService.getLastPeriodMaxRequestNums(clusterMasterIP,namespace,serviceName,startTime,endTime);
        resultMap.put("lastPeriodMaxRequestNums",lastPeriodMaxRequestNums);
        return resultMap;
    }

    /**
     * 获取某个服务在将来一段时间的最大请求量
     *
     * @param clusterMasterIP
     * @param namespace
     * @param serviceName
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/service/nextPeriodMaxRequestNums")
    public Map<String,Object> getNextPeriodMaxRequestNums(@RequestParam("clusterIp")String clusterMasterIP,
                                                          @RequestParam("namespace")String namespace,
                                                          @RequestParam("serviceName")String serviceName,
                                                          @RequestParam("startTime")String startTime,
                                                          @RequestParam("endTime")String endTime){
        Map<String,Object> resultMap = new HashMap<>();
        double nextPeriodMaxRequestNums = regulationService.getNextPeriodMaxRequestNums(clusterMasterIP,namespace,serviceName,startTime,endTime);
        resultMap.put("nextPeriodMaxRequestNums",nextPeriodMaxRequestNums);
        return resultMap;
    }

    /**
     * 获取某个集群中某个工作节点的CPU总量
     *
     * @param clusterMasterIP
     * @param nodeName
     * @return
     */
    @GetMapping("/node/cpuTotal")
    public Map<String,Object> getNodeCpuCores(@RequestParam("clusterIp")String clusterMasterIP,
                                              @RequestParam("hostName")String nodeName){
        Map<String,Object> resultMap = new HashMap<>();
        double cpuCores = regulationService.getNodeCpuCores(clusterMasterIP,nodeName);
        resultMap.put("cpuTotal",cpuCores);
        return resultMap;
    }

    /**
     * 获取某个集群中某个工作节点的内存总量,单位MB
     *
     * @param clusterMasterIP
     * @param nodeName
     * @return
     */
    @GetMapping("/node/memTotal")
    public Map<String,Object> getNodeMemMaxCapacity(@RequestParam("clusterIp")String clusterMasterIP,
                                              @RequestParam("hostName")String nodeName){
        Map<String,Object> resultMap = new HashMap<>();
        double memMaxCapacity = regulationService.getNodeMemMaxCapacity(clusterMasterIP,nodeName)/1024/1024;
        resultMap.put("memTotal",memMaxCapacity);
        return resultMap;
    }

    /**
     * 获取某个工作节点在过去一段时间的最大cpu利用率
     *
     * @param clusterMasterIP
     * @param nodeName
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/node/lastPeriodMaxCpuUsage")
    public Map<String,Object> getLastPeriodMaxCpuUsage(@RequestParam("clusterIp")String clusterMasterIP,
                                                       @RequestParam("hostName")String nodeName,
                                                       @RequestParam("startTime")String startTime,
                                                       @RequestParam("endTime")String endTime){
        Map<String,Object> resultMap = new HashMap<>();
        double lastPeriodMaxCpuUsage = regulationService.getLastPeriodMaxCpuUsage(clusterMasterIP,nodeName,startTime,endTime);
        resultMap.put("lastPeriodMaxCpuUsage",lastPeriodMaxCpuUsage);
        return resultMap;
    }

    /**
     * 获取某个工作节点在过去一段时间的最大mem利用率
     *
     * @param clusterMasterIP
     * @param nodeName
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/node/lastPeriodMaxMemUsage")
    public Map<String,Object> getLastPeriodMaxMemUsage(@RequestParam("clusterIp")String clusterMasterIP,
                                                       @RequestParam("hostName")String nodeName,
                                                       @RequestParam("startTime")String startTime,
                                                       @RequestParam("endTime")String endTime){
        Map<String,Object> resultMap = new HashMap<>();
        double lastPeriodMaxMemUsage = regulationService.getLastPeriodMaxMemUsage(clusterMasterIP,nodeName,startTime,endTime);
        resultMap.put("lastPeriodMaxCpuUsage",lastPeriodMaxMemUsage);
        return resultMap;
    }
}
