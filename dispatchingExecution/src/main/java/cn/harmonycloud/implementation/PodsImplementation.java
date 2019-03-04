package cn.harmonycloud.implementation;

import cn.harmonycloud.bean.Rule;
import cn.harmonycloud.bean.RuleSpec;
import cn.harmonycloud.kubernetesDAO.PodsDAO;
import cn.harmonycloud.utils.K8sClient;
import cn.harmonycloud.utils.OwnTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PodsImplementation {
    private final static Logger LOGGER = LoggerFactory.getLogger(PodsImplementation.class);

    public static boolean createPod(Rule rule){
        RuleSpec ruleSpec = rule.getSpec();
        //创建pod
        boolean flag = false;
        switch (ruleSpec.getOwnerType()){
//            case  DAEMONSET:
//                flag = PodsDAO.createDaemonsetPod(ruleSpec.getNamespace(),ruleSpec.getOwnName(),ruleSpec.getReplicas());
//                break;
            case REPLICASET:
                flag = PodsDAO.createReplicasetPod(ruleSpec.getNamespace(),ruleSpec.getOwnerName(),1);
                break;
            case DEPLOYMENT:
                flag = PodsDAO.createDeploymentPod(ruleSpec.getNamespace(),ruleSpec.getOwnerName(),1);
                break;
            case STATEFULSET:
                flag = PodsDAO.createStatefulsetPod(ruleSpec.getNamespace(),ruleSpec.getOwnerName(),1);
                break;
                default:
                    System.out.println("OwnType is unknown!");
        }

        return flag;
    }

    public static boolean deletePod(String namespace,String podName){
        //code
        try {
            //Pod 删除
            K8sClient.getInstance().pods().inNamespace(namespace).withName(podName).delete();
            LOGGER.info("Delete Pod["+podName+"] Successfully!");
            return true;
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            return false;
        }
    }

    public static boolean subtractReplicas(String namespace,OwnTypes ownerType,String ownername){
        boolean flag = false;
        switch (ownerType){
            case REPLICASET:
                flag = PodsDAO.deleteReplicasetPod(namespace,ownername,1);
                break;
            case DEPLOYMENT:
                flag = PodsDAO.deleteDeploymentPod(namespace,ownername,1);
                break;
            case STATEFULSET:
                flag = PodsDAO.deleteStatefulsetPod(namespace,ownername,1);
                break;
            default:
                LOGGER.info("ownerType["+ownerType+"] is unknown!");
        }
        return flag;
    }

}
