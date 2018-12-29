package cn.harmonycloud.kubernetesDAO;

/**
 * 调用kubernetesAPI，对pod进行操作
 */
public class PodsDAO {
    //createPod func
    public static boolean createDeploymentPod(String namespace,String ownName,int replicas){
        return true;
    }
    public static boolean createReplicasetPod(String namespace,String ownName,int replicas){
        return true;
    }
    public static boolean createStatefulsetPod(String namespace,String ownName,int replicas){
        return true;
    }
    public static boolean createDaemonsetPod(String namespace,String ownName,int replicas){
        return true;
    }
}
