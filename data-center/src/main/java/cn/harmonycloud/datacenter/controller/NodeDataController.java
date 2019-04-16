package cn.harmonycloud.datacenter.controller;


import cn.harmonycloud.datacenter.entity.es.NodeData;
import cn.harmonycloud.datacenter.entity.es.SearchNode;
import cn.harmonycloud.datacenter.entity.es.ServiceRequest;
import cn.harmonycloud.datacenter.service.INodeDataService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:18
 *@Modify By:
 */

@RestController
public class NodeDataController {

    @Autowired
    private INodeDataService nodeDataService;

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

    @GetMapping("/nodes")
    public List<NodeData> findAllNodes(){
        List<NodeData> nodes = nodeDataService.findAllNodeDatas();
        return nodes;
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
     * 获取当前node数据
     *
     * @return
     */
    @GetMapping("/nowNode")
    public List<NodeData> getNowNodes(){
        return nodeDataService.getNowNodes();
    }
    @GetMapping("/node/nodes")
    public List<SearchNode> getNodes(@RequestParam("clusterIp") String clusterIp)
    {
        //String newStr = clusterIp.substring(1, clusterIp.length()-1);
        List<NodeData> pod=nodeDataService.getNowNodes();
        List<SearchNode> searchNodes=new ArrayList<SearchNode>();
        for(NodeData pd:pod)
        {
            if(clusterIp.equals(pd.getClusterMasterIP()))
            {
                SearchNode sn=new SearchNode();
                sn.setClusterIp(pd.getClusterMasterIP());
                sn.setHostName(pd.getNodeName());
                searchNodes.add(sn);
            }
        }
        return searchNodes;
    }
    @GetMapping("/node/cpuTotal")
    public Map<String, Double> getNodeCpuNums(@RequestParam("clusterIp") String clusterIp,
                                                 @RequestParam("hostName") String hostName){
        Map<String, Double> responseMap = new HashMap<>();
        List<NodeData> pod=nodeDataService.getNowNodes();
        double ins=0;
        for(NodeData pd:pod)
        {
            if(clusterIp.equals(pd.getClusterMasterIP())&& hostName.equals(pd.getNodeName()))
            {
                ins+=pd.getCpuCores();
            }
        }
        responseMap.put("cpuTotal",ins);
        return responseMap;
    }
    @GetMapping("/node/memTotal")
    public Map<String, Double> getNodeMem(@RequestParam("clusterIp") String clusterIp,
                                                 @RequestParam("hostName") String hostName){
        Map<String, Double> responseMap = new HashMap<>();
        List<NodeData> pod=nodeDataService.getNowNodes();
        double ins=0;
        for(NodeData pd:pod)
        {
            if(clusterIp.equals(pd.getClusterMasterIP())&& hostName.equals(pd.getNodeName()))
            {
                ins+=pd.getAllocatableMem();
            }
        }
        responseMap.put("memTotal",ins);
        return responseMap;
    }
    @GetMapping("/node/lastPeriodMaxCpuUsage")
    public Map<String, Double> getNodeMaxCpu(@RequestParam("clusterIp") String clusterIp, @RequestParam("namespace") String namespace
            , @RequestParam("serviceName") String serviceName, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime){
        Map<String, Double> responseMap = new HashMap<>();
        List<NodeData> pod=nodeDataService.getNowNodes();
        double ins=0;
        ServiceRequest serviceRequests=new ServiceRequest();
        serviceRequests.setClusterIp(clusterIp);
        serviceRequests.setNamespace(namespace);
        serviceRequests.setServiceName(serviceName);
        serviceRequests.setStartTime(startTime);
        serviceRequests.setEndTime(endTime);
        Date startTimes=new Date();
        Date endTimes=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startTimes = df.parse(serviceRequests.getStartTime());
            endTimes=df.parse(serviceRequests.getEndTime());
        }
        catch (ParseException e)
        {
            System.out.println("字符串转日期失败3\n");
        }
        for(NodeData pd:pod)
        {
            if(serviceRequests.getClusterIp().equals(pd.getClusterMasterIP())&&
                    serviceRequests.getServiceName().equals(pd.getNodeName()))
            {
                Date nowTime=new Date();
                try
                {
                    nowTime = df.parse(pd.getTime());
                }
                catch (ParseException e)
                {
                    System.out.println("字符串转日期失败2\n");
                }
                if(ServiceRequest.isEffectiveDate(nowTime,startTimes,endTimes))
                {
                    double te=pd.getCpuUsage();
                    if(te>ins) ins=te;
                }
            }
        }
        responseMap.put("lastPeriodMaxCpuUsage",ins);
        return responseMap;
    }
    @GetMapping("/node/lastPeriodMaxMemUsage")
    public Map<String, Double> getNodeMaxMem(@RequestParam("clusterIp") String clusterIp, @RequestParam("namespace") String namespace
            , @RequestParam("serviceName") String serviceName, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime){
        Map<String, Double> responseMap = new HashMap<>();
        List<NodeData> pod=nodeDataService.getNowNodes();
        double ins=0;
        ServiceRequest serviceRequests=new ServiceRequest();
        serviceRequests.setClusterIp(clusterIp);
        serviceRequests.setNamespace(namespace);
        serviceRequests.setServiceName(serviceName);
        serviceRequests.setStartTime(startTime);
        serviceRequests.setEndTime(endTime);
        Date startTimes=new Date();
        Date endTimes=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startTimes = df.parse(serviceRequests.getStartTime());
            endTimes=df.parse(serviceRequests.getEndTime());
        }
        catch (ParseException e)
        {
            System.out.println("字符串转日期失败3\n");
        }
        for(NodeData pd:pod)
        {
            if(serviceRequests.getClusterIp().equals(pd.getClusterMasterIP())&&
                    serviceRequests.getServiceName().equals(pd.getNodeName()))
            {
                Date nowTime=new Date();
                try
                {
                    nowTime = df.parse(pd.getTime());
                }
                catch (ParseException e)
                {
                    System.out.println("字符串转日期失败2\n");
                }
                if(ServiceRequest.isEffectiveDate(nowTime,startTimes,endTimes))
                {
                    double te=pd.getMemUsage();
                    if(te>ins) ins=te;
                }
            }
        }
        responseMap.put("lastPeriodMaxMemUsage",ins);
        return responseMap;
    }
}
