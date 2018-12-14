package cn.harmonycloud.interfaces;

import cn.harmonycloud.implementation.CreatePodCallable;
import cn.harmonycloud.implementation.DeletePodCallable;
import cn.harmonycloud.implementation.PodsImplementation;
import cn.harmonycloud.utils.ThreadPoolUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

//springboot或者springmvc等框架
@RestController
@RequestMapping("/dispatching")
public class DispatchInterfaces {


    private ExecutorService THREAD_POOL = ThreadPoolUtils.getInstance();
    //createPod
    @RequestMapping("/createPod")
    public boolean createPod(String ruleJSON) throws ExecutionException, InterruptedException {
        CreatePodCallable createPodCallable = new CreatePodCallable(ruleJSON);
        Future<Boolean> result = THREAD_POOL.submit(createPodCallable);
        return result.get().booleanValue();
    }

    //deletePod
    @RequestMapping("/deletePod")
    public boolean deletePod(String json) throws ExecutionException, InterruptedException {
        DeletePodCallable deletePodCallable = new DeletePodCallable(json);
        Future<Boolean> result = THREAD_POOL.submit(deletePodCallable);
        return result.get().booleanValue();
    }
}
