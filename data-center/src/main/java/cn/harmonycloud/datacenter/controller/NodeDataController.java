package cn.harmonycloud.datacenter.controller;


import cn.harmonycloud.datacenter.entity.es.NodeData;
import cn.harmonycloud.datacenter.service.INodeDataService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        List<NodeData> nodes = Lists.newArrayList(nodeDataService.findAllNodeDatas());
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
    public List<Map<String,Object>> getNowNodes(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return nodeDataService.getNowNodes(df.format(new Date()));
    }
}
