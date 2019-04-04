package cn.harmonycloud.kubernetesDAO;

import cn.harmonycloud.bean.Rule;
import cn.harmonycloud.bean.RuleSpec;
import cn.harmonycloud.utils.Constants;
import cn.harmonycloud.utils.K8sClient;
import cn.harmonycloud.utils.OwnTypes;
import io.fabric8.kubernetes.api.model.apps.*;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 调用kubernetesAPI，对pod进行操作
 */
public class PodsDAO {
    private final static Logger LOGGER = LoggerFactory.getLogger(PodsDAO.class);

    public static boolean createPod(String masterIp,String namespace,String ownerType,String ownerName){
        //创建pod
        boolean flag = false;
        switch (ownerType){
            case "replicaSet":
                flag = PodsDAO.createReplicasetPod(masterIp,namespace,ownerName,1);
                break;
            case "deployment":
                flag = PodsDAO.createDeploymentPod(masterIp,namespace,ownerName,1);
                break;
            case "statefulSet":
                flag = PodsDAO.createStatefulsetPod(masterIp,namespace,ownerName,1);
                break;
            default:
                LOGGER.info("OwnType is unknown!");
        }

        return flag;
    }

    public static boolean deletePod(String masterIp,String namespace,String podName){
        //code
        try {
            //Pod 删除
            K8sClient.getInstance(masterIp).pods().inNamespace(namespace).withName(podName).delete();
            LOGGER.info("Delete Pod["+podName+"] Successfully!");
            return true;
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            return false;
        }
    }

    public static boolean subtractReplicas(String masterIp, String namespace, OwnTypes ownerType, String ownername){
        boolean flag = false;
        switch (ownerType){
            case REPLICASET:
                flag = PodsDAO.deleteReplicasetPod(masterIp,namespace,ownername,1);
                break;
            case DEPLOYMENT:
                flag = PodsDAO.deleteDeploymentPod(masterIp,namespace,ownername,1);
                break;
            case STATEFULSET:
                flag = PodsDAO.deleteStatefulsetPod(masterIp,namespace,ownername,1);
                break;
            default:
                LOGGER.info("ownerType["+ownerType+"] is unknown!");
        }
        return flag;
    }

    //createPod func
    public static boolean createDeploymentPod(String masterIp,String namespace,String ownName,int replicas){
        KubernetesClient client = new DefaultKubernetesClient(new ConfigBuilder().withTrustCerts(true).withMasterUrl(Constants.MASTER).build());
        Deployment deployment = new Deployment();
        DeploymentList deploymentList = K8sClient.getInstance(masterIp).apps().deployments().list();
        for (Deployment e : deploymentList.getItems()){
            if (e.getMetadata().getName().equals(ownName)){
                deployment = e;
            }
        }
        LOGGER.info("namespace["+namespace+"]ownName["+ownName+"]replicas["+replicas+"]");
//        LOGGER.info("Deployment.class:"+deployment.getClass().toString());
//        LOGGER.info("deployment.Spec.Replicas:"+deployment.getSpec().getReplicas());
        NonNamespaceOperation<Deployment,DeploymentList,DoneableDeployment,RollableScalableResource<Deployment,DoneableDeployment>> operation = client.apps().deployments().inNamespace(namespace);
        RollableScalableResource rollableScalableResource = null;
        try{
            rollableScalableResource = operation.withName(ownName);//.scale(deployment.getSpec().getReplicas()+replicas,true);
        }catch (ClassCastException e){
            e.getStackTrace();
            e.getCause();
            e.printStackTrace();
            return false;
        }
        rollableScalableResource.scale(deployment.getSpec().getReplicas()+replicas,true);
        LOGGER.info("Create Pod{Deployment.Namespace["+namespace+"],Deployment.Name["+ownName+"]} Successfully!");
        return true;
    }

    public static void main(String[] args){
        createDeploymentPod("","wordpress","wordpress",2);

    }
    public static boolean createReplicasetPod(String masterIp,String namespace,String ownName,int replicas){
        ReplicaSet replicaSet = new ReplicaSet();
        ReplicaSetList replicaSetList = K8sClient.getInstance(masterIp).apps().replicaSets().list();
        for (ReplicaSet e:replicaSetList.getItems()){
            if (e.getMetadata().getName().equals(ownName)){
                replicaSet = e;
            }
        }
        K8sClient.getInstance(masterIp).apps().replicaSets().inNamespace(namespace).withName(ownName).scale(replicaSet.getSpec().getReplicas()+replicas);
        LOGGER.info("Create Pod{Replicaset.Namespace["+namespace+"],Replicaset.Name["+ownName+"]} Successfully!");
        return true;
    }
    public static boolean createStatefulsetPod(String masterIp,String namespace,String ownName,int replicas){
        StatefulSet statefulSet = new StatefulSet();
        StatefulSetList statefulSetList = K8sClient.getInstance(masterIp).apps().statefulSets().list();
        for (StatefulSet e : statefulSetList.getItems()){
            if (e.getMetadata().getName().equals(ownName)){
                statefulSet = e;
            }
        }
        K8sClient.getInstance(masterIp).apps().statefulSets().inNamespace(namespace).withName(ownName).scale(statefulSet.getSpec().getReplicas()+replicas);
        LOGGER.info("Create Pod{StatefulSet.Namespace["+namespace+"],StatefulSet.Name["+ownName+"]} Successfully!");
        return true;
    }

    public static boolean deleteDeploymentPod(String masterIp,String namespace,String ownername,int replicas){
        Deployment deployment = new Deployment();
        DeploymentList deploymentList = K8sClient.getInstance(masterIp).apps().deployments().list();
        for (Deployment e : deploymentList.getItems()){
            if (e.getMetadata().getName().equals(ownername)){
                deployment = e;
            }
        }
        K8sClient.getInstance(masterIp).apps().deployments().inNamespace(namespace).withName(ownername).scale(deployment.getSpec().getReplicas()-replicas);
        LOGGER.info("Reduce Pod{Deployment.Namespace["+namespace+"],Deployment.Name["+ownername+"]} Successfully!");
        return true;
    }

    public static boolean deleteStatefulsetPod(String masterIp,String namespace,String ownername,int replicas){
        StatefulSet statefulSet = new StatefulSet();
        StatefulSetList statefulSetList = K8sClient.getInstance(masterIp).apps().statefulSets().list();
        for (StatefulSet e : statefulSetList.getItems()){
            if (e.getMetadata().getName().equals(ownername)){
                statefulSet = e;
            }
        }
        K8sClient.getInstance(masterIp).apps().statefulSets().inNamespace(namespace).withName(ownername).scale(statefulSet.getSpec().getReplicas()-replicas);
        LOGGER.info("Reduce Pod{StatefulSet.Namespace["+namespace+"],StatefulSet.Name["+ownername+"]} Successfully!");
        return true;
    }
    public static boolean deleteReplicasetPod(String masterIp,String namespace,String ownername,int replicas){
        ReplicaSet replicaSet = new ReplicaSet();
        ReplicaSetList replicaSetList = K8sClient.getInstance(masterIp).apps().replicaSets().list();
        for (ReplicaSet e:replicaSetList.getItems()){
            if (e.getMetadata().getName().equals(ownername)){
                replicaSet = e;
            }
        }
        K8sClient.getInstance(masterIp).apps().replicaSets().inNamespace(namespace).withName(ownername).scale(replicaSet.getSpec().getReplicas()-replicas);
        LOGGER.info("Reduce Pod{ReplicaSet.Namespace["+namespace+"],ReplicaSet.Name["+ownername+"]} Successfully!");
        return true;
    }
}
