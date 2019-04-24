package cn.harmonycloud;

/**
 * @author wangyuzhong
 * @date 19-1-8 下午5:57
 * @Despriction
 */
public class ExecuteTask {

    public static void process(){

        //执行node评估的线程

        NodeEvaluateTaskThread nodeEvaluateTaskThread = new NodeEvaluateTaskThread();

        Thread nodeEvaluateThread = new Thread(nodeEvaluateTaskThread);

        nodeEvaluateThread.start();


        //执行service评估的线程

        ServiceEvaluateTaskThread serviceEvaluateTaskThread = new ServiceEvaluateTaskThread();

        Thread serviceEvaluateThread = new Thread(serviceEvaluateTaskThread);

        serviceEvaluateThread.start();

        //执行cluster评估的线程
        //TODO

    }
}
