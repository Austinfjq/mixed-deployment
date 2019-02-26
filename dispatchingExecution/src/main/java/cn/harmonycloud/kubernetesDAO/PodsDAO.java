package cn.harmonycloud.kubernetesDAO;

import cn.harmonycloud.utils.K8sClient;
import io.fabric8.kubernetes.api.model.apps.*;

/**
 * 调用kubernetesAPI，对pod进行操作
 */
public class PodsDAO {
    //createPod func
    public static boolean createDeploymentPod(String namespace,String ownName,int replicas){
        Deployment deployment = new Deployment();
        DeploymentList deploymentList = K8sClient.getInstance().apps().deployments().list();
        for (Deployment e : deploymentList.getItems()){
            if (e.getMetadata().getName().equals(ownName)){
                deployment = e;
            }
        }
        K8sClient.getInstance().apps().deployments().inNamespace(namespace).withName(ownName).scale(deployment.getSpec().getReplicas()+replicas);
        return true;
    }
    public static boolean createReplicasetPod(String namespace,String ownName,int replicas){
        ReplicaSet replicaSet = new ReplicaSet();
        ReplicaSetList replicaSetList = K8sClient.getInstance().apps().replicaSets().list();
        for (ReplicaSet e:replicaSetList.getItems()){
            if (e.getMetadata().getName().equals(ownName)){
                replicaSet = e;
            }
        }
        K8sClient.getInstance().apps().replicaSets().inNamespace(namespace).withName(ownName).scale(replicaSet.getSpec().getReplicas()+replicas);
        return true;
    }
    public static boolean createStatefulsetPod(String namespace,String ownName,int replicas){
        StatefulSet statefulSet = new StatefulSet();
        StatefulSetList statefulSetList = K8sClient.getInstance().apps().statefulSets().list();
        for (StatefulSet e : statefulSetList.getItems()){
            if (e.getMetadata().getName().equals(ownName)){
                statefulSet = e;
            }
        }
        K8sClient.getInstance().apps().statefulSets().inNamespace(namespace).withName(ownName).scale(statefulSet.getSpec().getReplicas()+replicas);
        return true;
    }
//    public static boolean createDaemonsetPod(String namespace,String ownName,int replicas){
//        DaemonSet daemonSet = new DaemonSet();
//        DaemonSetList daemonSetList = K8sClient.getInstance().apps().daemonSets().list();
//        for (DaemonSet e : daemonSetList.getItems()){
//            if (e.getMetadata().getName().equals(ownName)){
//                daemonSet = e;
//            }
//        }
//        K8sClient.getInstance().apps().daemonSets().inNamespace(namespace).withName(ownName).
//        return true;
//    }
}
