package cn.harmonycloud.interfaces;

import cn.harmonycloud.controller.PodController;
import cn.harmonycloud.utils.ThreadPoolUtils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

//springboot或者springmvc等框架
@RestController
@RequestMapping("/dispatching")
public class DispatchInterfaces {
    private final static Logger LOGGER = LoggerFactory.getLogger(DispatchInterfaces.class);
    //createPod
    @RequestMapping("/createPod")
    public boolean createPod(@RequestParam(value = "masterIp") String masterIp,
                             @RequestParam(value = "namespace") String namespace,
                             @RequestParam(value = "servicename")String servicename,
                             @RequestParam(value = "nodeList") String nodeList) {
        LOGGER.info("#######Create a Pod#######");
        boolean result = PodController.createPodController(masterIp,namespace,servicename,nodeList);
        return result;
    }

    //deletePod
    @RequestMapping("/deletePod")
    public boolean deletePod(@RequestParam String masterIp,@RequestParam String namespace,@RequestParam String servicename,@RequestParam String podname) throws ExecutionException, InterruptedException {
        LOGGER.info("#######Delete a Pod#######");
        boolean result = PodController.deletePodController(masterIp,namespace,servicename,podname);
        return result;
    }
}
