package cn.harmonycloud.interfaces;

import cn.harmonycloud.implementation.CreatePodCallable;
import cn.harmonycloud.implementation.DeletePodCallable;
import cn.harmonycloud.utils.ThreadPoolUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

//springboot或者springmvc等框架
@RestController
@RequestMapping("/dispatching")
public class DispatchInterfaces {
    //createPod
    @RequestMapping("/createPod")
    public boolean createPod(String ruleJSON) throws ExecutionException, InterruptedException {
        CreatePodCallable createPodCallable = new CreatePodCallable(ruleJSON);
        Future<Boolean> result = ThreadPoolUtils.getInstance().submit(createPodCallable);
        return result.get().booleanValue();
    }

    //deletePod
    @RequestMapping("/deletePod")
    public boolean deletePod(String namespace,String podName) throws ExecutionException, InterruptedException {
        DeletePodCallable deletePodCallable = new DeletePodCallable(namespace,podName);
        Future<Boolean> result = ThreadPoolUtils.getInstance().submit(deletePodCallable);
        return result.get().booleanValue();
    }
}
