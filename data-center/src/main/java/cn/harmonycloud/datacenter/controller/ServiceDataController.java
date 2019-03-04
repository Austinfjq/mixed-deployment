package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.ServiceData;
import cn.harmonycloud.datacenter.service.INodeDataService;
import cn.harmonycloud.datacenter.service.IServiceDataService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:20
 *@Modify By:
 */

@RestController
public class ServiceDataController {

    @Autowired
    private IServiceDataService serviceDataService;

    @Autowired
    private INodeDataService nodeDataService;

    @PutMapping("/service")
    public Map<String, Object> saveOneServiceData(@RequestBody ServiceData serviceData){
        Map<String, Object> responseMap = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uuid = UUID.randomUUID().toString();
        serviceData.setId(uuid);
        serviceData.setTime(df.format(new Date()));
        serviceDataService.saveOneServiceData(serviceData);

        Optional<ServiceData> serviceDataOptional = serviceDataService.findById(uuid);
        if(serviceDataOptional.isPresent()){
            responseMap.put("isSucceed",true);
        }else responseMap.put("isSucceed",false);

        return responseMap;
    }

    @PutMapping("/services")
    public Map<String, Object> saveAllServiceDatas(@RequestBody List<ServiceData> serviceDatas){
        Map<String, Object> responseMap = new HashMap<>();
        if(serviceDatas.size() > 0) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            for (ServiceData serviceData : serviceDatas) {
                serviceData.setId(UUID.randomUUID().toString());
                serviceData.setTime(time);
            }

            Iterable<ServiceData> serviceDataIterable = serviceDataService.saveAllServiceDatas(serviceDatas);
            if(Lists.newArrayList(serviceDataIterable).size() == serviceDatas.size()){
                responseMap.put("isSucceed",true);
            }else{
                responseMap.put("isSucceed",false);
            }
        }else{
            responseMap.put("isSucceed",false);
        }

        return responseMap;
    }

    @GetMapping("/services")
    public List<ServiceData> findAllServices(){
        List<ServiceData> services = serviceDataService.findAllServiceDatas();
        return services;
    }

    /**
     * 获得某个在线服务的某个指标在某段时间段的均值
     *
     * @param requestMap
     * @return
     */
    @PostMapping("/service/getServiceIndex")
    public Map<String, Object> getIndexTimeSeries(@RequestBody Map<String, Object> requestMap){
        Map<String, Object> responseMap = new HashMap<>();
        String namespace = (String) requestMap.get("namespace");
        String serviceName = (String) requestMap.get("serviceName");
        String indexName = (String) requestMap.get("indexName");
        String startTime = (String) requestMap.get("startTime");
        String endTime = (String) requestMap.get("endTime");

        Double avg_index = serviceDataService.getIndexTimeSeries(namespace,serviceName,indexName,startTime,endTime);
        if(avg_index != null){
            responseMap.put("indexTimeSeries",avg_index);
        }else{ }
        return responseMap;
    }

    /**
     * 取某个指标的历史数据值
     *
     * @param requestMap
     * @return
     */
    @PostMapping("/indexData")
    public List<DataPoint> getIndexDatas(@RequestBody Map<String, Object> requestMap){

        int type = (int) requestMap.get("type");
        String id = (String) requestMap.get("id");
        String indexName = (String) requestMap.get("index");//需要获取的指标名
        String startTime = (String) requestMap.get("startTime");
        String endTime = (String) requestMap.get("endTime");

        if(type == 0){
            return serviceDataService.getIndexDatas(id,indexName,startTime,endTime);
        }else if(type == 1){
            return nodeDataService.getIndexDatas(id,indexName,startTime,endTime);
        }else{
            return new ArrayList<>();
        }
    }

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

    /**
     * 获取当前service数据
     *
     * @return
     */
    @GetMapping("/nowService")
    public List<Map> getNowServices(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return serviceDataService.getNowServices();
    }
}
