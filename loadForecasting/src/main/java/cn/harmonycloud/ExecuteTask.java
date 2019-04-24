package cn.harmonycloud;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午2:42
 * @Despriction 定时执行的任务
 */
public class ExecuteTask {

    public void process() {


        NodeTaskThreadExecutor nodeTaskThreadExecutor = new NodeTaskThreadExecutor();

        Thread nodeThread = new Thread(nodeTaskThreadExecutor);

        nodeThread.start();



        ServiceTaskThreadExecutor serviceTaskThreadExecutor = new ServiceTaskThreadExecutor();

        Thread serviceThread = new Thread(serviceTaskThreadExecutor);

        serviceThread.start();

    }

}
