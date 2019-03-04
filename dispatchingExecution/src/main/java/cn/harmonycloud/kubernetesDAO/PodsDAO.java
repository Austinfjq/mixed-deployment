package cn.harmonycloud.kubernetesDAO;

import cn.harmonycloud.utils.K8sClient;
import io.fabric8.kubernetes.api.model.apps.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 调用kubernetesAPI，对pod进行操作
 */
public class PodsDAO {
    private final static Logger LOGGER = LoggerFactory.getLogger(PodsDAO.class);
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
        LOGGER.info("Create Pod{Deployment.Namespace["+namespace+"],Deployment.Name["+ownName+"]} Successfully!");
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
        LOGGER.info("Create Pod{Replicaset.Namespace["+namespace+"],Replicaset.Name["+ownName+"]} Successfully!");
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
        LOGGER.info("Create Pod{StatefulSet.Namespace["+namespace+"],StatefulSet.Name["+ownName+"]} Successfully!");
        return true;
    }

    public static boolean deleteDeploymentPod(String namespace,String ownername,int replicas){
        Deployment deployment = new Deployment();
        DeploymentList deploymentList = K8sClient.getInstance().apps().deployments().list();
        for (Deployment e : deploymentList.getItems()){
            if (e.getMetadata().getName().equals(ownername)){
                deployment = e;
            }
        }
        K8sClient.getInstance().apps().deployments().inNamespace(namespace).withName(ownername).scale(deployment.getSpec().getReplicas()-replicas);
        LOGGER.info("Reduce Pod{Deployment.Namespace["+namespace+"],Deployment.Name["+ownername+"]} Successfully!");
        return true;
    }

    public static boolean deleteStatefulsetPod(String namespace,String ownername,int replicas){
        StatefulSet statefulSet = new StatefulSet();
        StatefulSetList statefulSetList = K8sClient.getInstance().apps().statefulSets().list();
        for (StatefulSet e : statefulSetList.getItems()){
            if (e.getMetadata().getName().equals(ownername)){
                statefulSet = e;
            }
        }
        K8sClient.getInstance().apps().statefulSets().inNamespace(namespace).withName(ownername).scale(statefulSet.getSpec().getReplicas()-replicas);
        LOGGER.info("Reduce Pod{StatefulSet.Namespace["+namespace+"],StatefulSet.Name["+ownername+"]} Successfully!");
        return true;
    }
    public static boolean deleteReplicasetPod(String namespace,String ownername,int replicas){
        ReplicaSet replicaSet = new ReplicaSet();
        ReplicaSetList replicaSetList = K8sClient.getInstance().apps().replicaSets().list();
        for (ReplicaSet e:replicaSetList.getItems()){
            if (e.getMetadata().getName().equals(ownername)){
                replicaSet = e;
            }
        }
        K8sClient.getInstance().apps().replicaSets().inNamespace(namespace).withName(ownername).scale(replicaSet.getSpec().getReplicas()-replicas);
        LOGGER.info("Reduce Pod{ReplicaSet.Namespace["+namespace+"],ReplicaSet.Name["+ownername+"]} Successfully!");
        return true;
    }
}
