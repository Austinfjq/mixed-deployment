package cn.harmonycloud;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午2:44
 * @Despriction 线程池
 */
public class TaskThreadPoolExecutor {

    private static ExecutorService INSTANCE = null;
    private static final int POOL_SIZE = 2;


    public static ExecutorService getExecutor() {
        if (INSTANCE == null) {
            synchronized (TaskThreadPoolExecutor.class) {
                if (INSTANCE == null) {
                    INSTANCE = Executors.newFixedThreadPool(POOL_SIZE);
                }
            }
        }
        return INSTANCE;
    }
}
