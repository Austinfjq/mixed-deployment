package cn.harmonycloud.interfaces;

import cn.harmonycloud.implementation.CreatePodCallable;
import cn.harmonycloud.implementation.DeletePodCallable;
import cn.harmonycloud.utils.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public boolean createPod(String namespace,String servicename,String nodeList) throws ExecutionException, InterruptedException {
        LOGGER.info("#######Create a Pod#######");
        CreatePodCallable createPodCallable = new CreatePodCallable(namespace,servicename,nodeList);
        Future<Boolean> result = ThreadPoolUtils.getInstance().submit(createPodCallable);
        return result.get().booleanValue();
    }

    //deletePod
    @RequestMapping("/deletePod")
    public boolean deletePod(String namespace,String servicename,String podname) throws ExecutionException, InterruptedException {
        LOGGER.info("#######Delete a Pod#######");
        DeletePodCallable deletePodCallable = new DeletePodCallable(namespace,servicename,podname);
        Future<Boolean> result = ThreadPoolUtils.getInstance().submit(deletePodCallable);
        return result.get().booleanValue();
    }
}
