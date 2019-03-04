package cn.harmonycloud;

import cn.harmonycloud.DAO.NodeDAO;
import cn.harmonycloud.DAO.ServiceDAO;
import cn.harmonycloud.beans.NodeLoad;
import cn.harmonycloud.beans.ServiceLoad;

import java.util.List;

/**
 * @author wangyuzhong
 * @date 19-1-8 下午5:57
 * @Despriction
 */
public class ExecuteTask {

    public static void process(){

        NodeDAO monitorDataDAO = NodeDAO.getInstance();

        List<ServiceLoad> serviceLoads = ServiceDAO.getServiceLoadList("2019-01-21 10:46:00","2019-01-21 10:47:00");

        List<NodeLoad> nodeLoads = NodeDAO.getNodeLoadList("2019-01-21 10:46:00","2019-01-21 10:47:00");


        //执行node评估的线程

        NodeEvaluateTaskThread nodeEvaluateTaskThread = new NodeEvaluateTaskThread(nodeLoads);

        Thread nodeEvaluateThread = new Thread(nodeEvaluateTaskThread);

        nodeEvaluateThread.start();


        //执行service评估的线程

        ServiceEvaluateTaskThread serviceEvaluateTaskThread = new ServiceEvaluateTaskThread(serviceLoads);

        Thread serviceEvaluateThread = new Thread(serviceEvaluateTaskThread);

        serviceEvaluateThread.start();

    }


    public static void main(String[] args) {
        process();
    }
}
