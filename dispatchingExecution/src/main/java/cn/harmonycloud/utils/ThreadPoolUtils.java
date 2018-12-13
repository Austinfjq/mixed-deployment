package cn.harmonycloud.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtils {
    private static ExecutorService THREAD_POOL;
    private ThreadPoolUtils(){}

    public static ExecutorService getInstance(){
        if (THREAD_POOL == null){
            synchronized (ThreadPoolUtils.class){
                if (THREAD_POOL == null){
                    THREAD_POOL = Executors.newFixedThreadPool(Constants.THREAD_POOL_NUMBER);
                }
            }
        }
        return THREAD_POOL;
    }
}
