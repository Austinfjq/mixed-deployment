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
    private final static int REPLICAS = 1;
    public static void createPod(String masterIp,String namespace,String ownerType,String ownerName){
        //创建pod
        switch (ownerType.toLowerCase()) {
            case "replicaset":
                PodsDAO.createReplicasetPod(masterIp, namespace, ownerName, REPLICAS);
                break;
            case "deployment":
                PodsDAO.createDeploymentPod(masterIp, namespace, ownerName, REPLICAS);
                break;
            case "statefulset":
                PodsDAO.createStatefulsetPod(masterIp, namespace, ownerName, REPLICAS);
                break;
            default:
                LOGGER.info("OwnType is unknown!");
        }
    }

    public static void deletePod(String masterIp,String namespace,String podName){
        //code
        try {
            //Pod 删除
            K8sClient.getInstance(masterIp).pods().inNamespace(namespace).withName(podName).delete();
            LOGGER.info("Delete Pod["+podName+"] Successfully!");
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
        }
    }

    public static void subtractReplicas(String masterIp, String namespace, String ownerType, String ownername){
        switch (ownerType.toLowerCase()){
            case "replicaset":
                PodsDAO.deleteReplicasetPod(masterIp,namespace,ownername,REPLICAS);
                break;
            case "deployment":
                PodsDAO.deleteDeploymentPod(masterIp,namespace,ownername,REPLICAS);
                break;
            case "statefulset":
                PodsDAO.deleteStatefulsetPod(masterIp,namespace,ownername,REPLICAS);
                break;
            default:
                LOGGER.info("ownerType["+ownerType+"] is unknown!");
        }
    }

    //createPod func
    public static void createDeploymentPod(String masterIp,String namespace,String ownName,int replicas){
        Deployment deployment = new Deployment();
        DeploymentList deploymentList = K8sClient.getInstance(masterIp).apps().deployments().list();
        for (Deployment e : deploymentList.getItems()){
            if (e.getMetadata().getName().equals(ownName) && e.getMetadata().getNamespace().equals(namespace)){
                deployment = e;
                break;
            }
        }
        if(deployment == null){
            LOGGER.debug("Cannot Found Deployment{masterIp["+masterIp+"],namespace["+namespace+"],name["+ownName+"]}");
            return;
        }
        Deployment result = K8sClient.getInstance(masterIp).apps().deployments().inNamespace(namespace).withName(ownName).scale(deployment.getSpec().getReplicas()+replicas);
        LOGGER.info("Create Pod/Deployment{masterIp["+masterIp+"],namespace["+result.getMetadata().getNamespace()+"],name["+result.getMetadata().getName()+"]} Successfully!");
    }

    public static void main(String[] args){
//        K8sClient.getInstance("10.10.102.25").apps().deployments().inNamespace("wy").withName("nginx").scale(2);
        createDeploymentPod("10.10.102.25","wy","nginx",1);

    }
    public static void createReplicasetPod(String masterIp,String namespace,String ownName,int replicas){
        ReplicaSet replicaSet = new ReplicaSet();
        ReplicaSetList replicaSetList = K8sClient.getInstance(masterIp).apps().replicaSets().list();
        for (ReplicaSet e:replicaSetList.getItems()){
            if (e.getMetadata().getName().equals(ownName)){
                replicaSet = e;
            }
        }
        if(replicaSet == null){
            LOGGER.debug("Cannot Find Replicaset{masterIp["+masterIp+"],namespace["+namespace+"],name["+ownName+"]}");
            return;
        }
        ReplicaSet result = K8sClient.getInstance(masterIp).apps().replicaSets().inNamespace(namespace).withName(ownName).scale(replicaSet.getSpec().getReplicas()+replicas);
        LOGGER.info("Create Pod/Replicaset{masterIp["+masterIp+"],namespace["+result.getMetadata().getNamespace()+"],name["+result.getMetadata().getName()+"]} Successfully!");
    }
    public static void createStatefulsetPod(String masterIp,String namespace,String ownName,int replicas){
        StatefulSet statefulSet = new StatefulSet();
        StatefulSetList statefulSetList = K8sClient.getInstance(masterIp).apps().statefulSets().list();
        for (StatefulSet e : statefulSetList.getItems()){
            if (e.getMetadata().getName().equals(ownName)){
                statefulSet = e;
            }
        }
        if(statefulSet == null){
            LOGGER.debug("Cannot Find StatefulSet{masterI["+masterIp+"],namespace["+namespace+"],name["+ownName+"]}");
            return;
        }
        StatefulSet result = K8sClient.getInstance(masterIp).apps().statefulSets().inNamespace(namespace).withName(ownName).scale(statefulSet.getSpec().getReplicas()+replicas);
        LOGGER.info("Create Pod/StatefulSet{masterIp["+masterIp+"],namespace["+result.getMetadata().getNamespace()+"],name["+result.getMetadata().getName()+"]} Successfully!");
    }

    public static void deleteDeploymentPod(String masterIp,String namespace,String ownername,int replicas){
        Deployment deployment = new Deployment();
        DeploymentList deploymentList = K8sClient.getInstance(masterIp).apps().deployments().list();
        for (Deployment e : deploymentList.getItems()){
            if (e.getMetadata().getName().equals(ownername)){
                deployment = e;
            }
        }
        if(deployment == null){
            LOGGER.debug("Cannot Found Deployment{masterIp["+masterIp+"],namespace["+namespace+"],name["+ownername+"]}");
            return;
        }
        Deployment result = K8sClient.getInstance(masterIp).apps().deployments().inNamespace(namespace).withName(ownername).scale(deployment.getSpec().getReplicas()-replicas);
        LOGGER.info("Reduce Pod/Deployment{masterIp["+masterIp+"],namespace["+result.getMetadata().getNamespace()+"],name["+result.getMetadata().getName()+"]} Successfully!");
    }

    public static void deleteStatefulsetPod(String masterIp,String namespace,String ownername,int replicas){
        StatefulSet statefulSet = new StatefulSet();
        StatefulSetList statefulSetList = K8sClient.getInstance(masterIp).apps().statefulSets().list();
        for (StatefulSet e : statefulSetList.getItems()){
            if (e.getMetadata().getName().equals(ownername)){
                statefulSet = e;
            }
        }
        if(statefulSet == null){
            LOGGER.debug("Cannot Found StatefulSet{masterIp["+masterIp+"],namespace["+namespace+"],name["+ownername+"]}");
            return;
        }
        StatefulSet result = K8sClient.getInstance(masterIp).apps().statefulSets().inNamespace(namespace).withName(ownername).scale(statefulSet.getSpec().getReplicas()-replicas);
        LOGGER.info("Reduce Pod/StatefulSet{masterIp["+masterIp+"],namespace["+result.getMetadata().getNamespace()+"],name["+result.getMetadata().getName()+"]} Successfully!");
    }
    public static void deleteReplicasetPod(String masterIp,String namespace,String ownername,int replicas){
        ReplicaSet replicaSet = new ReplicaSet();
        ReplicaSetList replicaSetList = K8sClient.getInstance(masterIp).apps().replicaSets().list();
        for (ReplicaSet e:replicaSetList.getItems()){
            if (e.getMetadata().getName().equals(ownername)){
                replicaSet = e;
            }
        }
        if(replicaSet == null){
            LOGGER.debug("Cannot Found ReplicaSet{masterIp["+masterIp+"],namespace["+namespace+"],name["+ownername+"]}");
            return;
        }
        ReplicaSet result = K8sClient.getInstance(masterIp).apps().replicaSets().inNamespace(namespace).withName(ownername).scale(replicaSet.getSpec().getReplicas()-replicas);
        LOGGER.info("Reduce Pod/ReplicaSet{masterIp["+masterIp+"],namespace["+result.getMetadata().getNamespace()+"],name["+result.getMetadata().getName()+"]} Successfully!");
    }
}
