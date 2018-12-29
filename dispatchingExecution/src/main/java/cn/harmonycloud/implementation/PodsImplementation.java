package cn.harmonycloud.implementation;

import cn.harmonycloud.bean.Rule;
import cn.harmonycloud.kubernetesDAO.PodsDAO;
import cn.harmonycloud.utils.K8sClient;

public class PodsImplementation {

    public static boolean createPod(Rule rule){
        //创建rule

        //创建pod
        boolean flag = false;
        switch (rule.getOwnType()){
            case  DAEMONSET:
                flag = PodsDAO.createDaemonsetPod(rule.getNamespace(),rule.getOwnName(),rule.getReplicas());
                break;
            case REPLICASET:
                flag = PodsDAO.createReplicasetPod(rule.getNamespace(),rule.getOwnName(),rule.getReplicas());
                break;
            case DEPLOYMENT:
                flag = PodsDAO.createDeploymentPod(rule.getNamespace(),rule.getOwnName(),rule.getReplicas());
                break;
            case STATEFULSET:
                flag = PodsDAO.createStatefulsetPod(rule.getNamespace(),rule.getOwnName(),rule.getReplicas());
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
            System.out.println("pod delete success");
            return true;
        }catch (Exception e){
            System.out.println("pod create failed");
            return false;
        }
    }

}
