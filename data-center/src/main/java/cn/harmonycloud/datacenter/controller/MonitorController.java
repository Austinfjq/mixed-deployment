package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.entity.es.NodeData;
import cn.harmonycloud.datacenter.entity.es.PodData;
import cn.harmonycloud.datacenter.entity.es.ServiceData;
import cn.harmonycloud.datacenter.service.INodeDataService;
import cn.harmonycloud.datacenter.service.IPodDataService;
import cn.harmonycloud.datacenter.service.IServiceDataService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
*@Author: shaodilong
*@Description: 监控模块数据接口
*@Date: Created in 2019/5/6 18:02
*@Modify By:
*/
@RestController
public class MonitorController {
    @Autowired
    private IServiceDataService serviceDataService;
    @Autowired
    private INodeDataService nodeDataService;
    @Autowired
    private IPodDataService podDataService;
    /**
     * 一条service数据写入
     *
     * @param serviceData
     * @return
     */
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

    /**
     * service数组写入
     *
     * @param serviceDatas
     * @return
     */
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

    /**
     * 获取所有service信息
     *
     * @return
     */
    @GetMapping("/services")
    public List<ServiceData> findAllServices(){
        List<ServiceData> services = serviceDataService.findAllServiceDatas();
        return services;
    }

    /**
     * 一条node数据写入
     *
     * @param nodeData
     * @return
     */
    @PutMapping("/node")
    public Map<String, Object> saveOneNodeData(@RequestBody NodeData nodeData){
        Map<String, Object> responseMap = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uuid = UUID.randomUUID().toString();
        nodeData.setId(uuid);
        nodeData.setTime(df.format(new Date()));
        nodeDataService.saveOneNodeData(nodeData);

        Optional<NodeData> nodeDataOptional = nodeDataService.findById(uuid);
        if(nodeDataOptional.isPresent()){
            responseMap.put("isSucceed",true);
        }else responseMap.put("isSucceed",false);

        return responseMap;
    }

    /**
     * node数组写入
     *
     * @param nodeDatas
     * @return
     */
    @PutMapping("/nodes")
    public Map<String, Object> saveAllNodeDatas(@RequestBody List<NodeData> nodeDatas){
        Map<String, Object> responseMap = new HashMap<>();
        if(nodeDatas.size() > 0) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            for (NodeData nodeData : nodeDatas) {
                nodeData.setId(UUID.randomUUID().toString());
                nodeData.setTime(time);
            }
            Iterable<NodeData> nodeDataIterable = nodeDataService.saveAllNodeDatas(nodeDatas);
            if(Lists.newArrayList(nodeDataIterable).size() == nodeDatas.size()){
                responseMap.put("isSucceed",true);
            }else{
                responseMap.put("isSucceed",false);
            }
        }else{
            responseMap.put("isSucceed",false);
        }

        return responseMap;
    }

    /**
     * 获取所有node信息
     *
     * @return
     */
    @GetMapping("/nodes")
    public List<NodeData> findAllNodes(){
        List<NodeData> nodes = nodeDataService.findAllNodeDatas();
        return nodes;
    }

    /**
     * 一条pod数据写入
     *
     * @param podData
     * @return
     */
    @PutMapping("/pod")
    public Map<String, Object> saveOnePodData(@RequestBody PodData podData){
        Map<String, Object> responseMap = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uuid = UUID.randomUUID().toString();
        podData.setId(uuid);
        podData.setTime(df.format(new Date()));
        podDataService.saveOnePodData(podData);

        Optional<PodData> podDataOptional = podDataService.findById(uuid);
        if(podDataOptional.isPresent()){
            responseMap.put("isSucceed", true);
        }else responseMap.put("isSucceed", false);

        return responseMap;
    }

    /**
     * pod数组写入
     *
     * @param podDatas
     * @return
     */
    @PutMapping("/pods")
    public Map<String, Object> saveAllPodDatas(@RequestBody List<PodData> podDatas){
        Map<String, Object> responseMap = new HashMap<>();
        if(podDatas.size() > 0) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            for (PodData podData : podDatas) {
                podData.setId(UUID.randomUUID().toString());
                podData.setTime(time);
            }

            Iterable<PodData> podDataIterable = podDataService.saveAllPodDatas(podDatas);
            if(Lists.newArrayList(podDataIterable).size() == podDatas.size()){
                responseMap.put("isSucceed",true);
            }else{
                responseMap.put("isSucceed",false);
            }
        }else{
            responseMap.put("isSucceed",false);
        }

        return responseMap;
    }

    /**
     * 获取所有pod信息
     *
     * @return
     */
    @GetMapping("/pods")
    public List<PodData> findAllPods(){
        List<PodData> pods = podDataService.findAllPodDatas();
        // TODO: 需修改实现
        for(PodData podData:pods){
            podData.changeLabels();
        }
        return pods;
    }
}
