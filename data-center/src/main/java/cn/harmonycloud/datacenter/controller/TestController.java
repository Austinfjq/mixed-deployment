package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.entity.test.ResultNode;
import cn.harmonycloud.datacenter.entity.test.ResultPod;
import cn.harmonycloud.datacenter.service.test.IResultNodeService;
import cn.harmonycloud.datacenter.service.test.IResultPodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/1/26 0:44
*@Modify By:
*/

@RestController
public class TestController {
    @Autowired
    private IResultPodService resultPodService;
    @Autowired
    private IResultNodeService resultNodeService;

    @PutMapping("/resultPod")
    public Map<String, Object> saveOneResultPod(@RequestBody ResultPod resultPod){
        Map<String, Object> responseMap = new HashMap<>();
        resultPod.setId(UUID.randomUUID().toString());
        ResultPod resultPodTmp = resultPodService.saveOneResultPod(resultPod);
        if(resultPodTmp.equals(resultPod)){
            responseMap.put("isSucceed",true);
        }else{
            responseMap.put("isSucceed",false);
        }
        return responseMap;
    }

    @PutMapping("/resultPods")
    public Map<String, Object> saveAllResultPods(@RequestBody List<ResultPod> resultPods){
        Map<String, Object> responseMap = new HashMap<>();
        for(ResultPod resultPod : resultPods){
            resultPod.setId(UUID.randomUUID().toString());
        }
        if(resultPods.size() > 0) {
            List<ResultPod> resultPodList = resultPodService.saveAllResultPods(resultPods);
            if (resultPodList.size() == resultPods.size()) {
                responseMap.put("isSucceed", true);
            } else {
                responseMap.put("isSucceed", false);
            }
        }else{
            responseMap.put("isSucceed", false);
        }
        return responseMap;
    }

    @GetMapping("resultPods")
    public List<ResultPod> getAllResultPods(){
        return resultPodService.getAllResultPods();
    }

    @PutMapping("/resultNode")
    public Map<String, Object> saveOneResultNode(@RequestBody ResultNode resultNode){
        Map<String, Object> responseMap = new HashMap<>();
        resultNode.setId(UUID.randomUUID().toString());
        ResultNode resultPodTmp = resultNodeService.saveOneResultNode(resultNode);
        if(resultPodTmp.equals(resultNode)){
            responseMap.put("isSucceed",true);
        }else{
            responseMap.put("isSucceed",false);
        }
        return responseMap;
    }

    @PutMapping("/resultNodes")
    public Map<String, Object> saveAllResultNodes(@RequestBody List<ResultNode> resultNodes){
        Map<String, Object> responseMap = new HashMap<>();
        for(ResultNode resultNode : resultNodes){
            resultNode.setId(UUID.randomUUID().toString());
        }
        if(resultNodes.size() > 0) {
            List<ResultNode> resultNodeList = resultNodeService.saveAllResultNodes(resultNodes);
            if (resultNodeList.size() == resultNodes.size()) {
                responseMap.put("isSucceed", true);
            } else {
                responseMap.put("isSucceed", false);
            }
        }else{
            responseMap.put("isSucceed", false);
        }
        return responseMap;
    }

    @GetMapping("resultNodes")
    public List<ResultNode> getAllResultNodes(){
        return resultNodeService.getAllResultNodes();
    }

}
