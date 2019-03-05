package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.entity.es.PodData;
import cn.harmonycloud.datacenter.entity.es.ServiceData;
import cn.harmonycloud.datacenter.service.IPodDataService;
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
 *@Description:
 *@Date: Created in 2019/1/24 16:20
 *@Modify By:
 */

@RestController
public class PodDataController {

    @Autowired
    private IPodDataService podDataService;

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

    @GetMapping("/pods")
    public List<PodData> findAllPods(){
        List<PodData> pods = podDataService.findAllPodDatas();
        return pods;
    }

    /**
     * 获取当前pod数据
     *
     * @return
     */
    @GetMapping("/nowPod")
    public List<PodData> getNowServices(){

        return podDataService.getNowServices();
    }
}
