package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.entity.es.SchedulePod;
import cn.harmonycloud.datacenter.service.ISchedulePodService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SchedulePodController {
    @Autowired
    private ISchedulePodService schedulePodService;

    @PutMapping("/schedulePods")
    public Map<String,Object> saveAllSchedulePods(@RequestBody List<SchedulePod> schedulePods){
        Map<String, Object> responseMap = new HashMap<>();
        if(schedulePods.size() > 0) {
            for (SchedulePod schedulePod : schedulePods) {
                schedulePod.setId(UUID.randomUUID().toString());
            }

            Iterable<SchedulePod> schedulePodIterable = schedulePodService.saveAllSchedulePods(schedulePods);
            if(Lists.newArrayList(schedulePodIterable).size() == schedulePods.size()){
                responseMap.put("isSucceed",true);
            }else{
                responseMap.put("isSucceed",false);
            }
        }else{
            responseMap.put("isSucceed",false);
        }

        return responseMap;
    }

    @GetMapping("/schedulePods")
    public List<SchedulePod> getAllSchedulePods(){
        List<SchedulePod> schedulePods = Lists.newArrayList(schedulePodService.getAllSchedulePods());
        return schedulePods;
    }
}
