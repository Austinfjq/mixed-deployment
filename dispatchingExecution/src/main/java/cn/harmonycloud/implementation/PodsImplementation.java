package cn.harmonycloud.implementation;

import cn.harmonycloud.bean.Rule;
import cn.harmonycloud.bean.RuleSpec;
import cn.harmonycloud.kubernetesDAO.PodsDAO;
import cn.harmonycloud.utils.K8sClient;

public class PodsImplementation {

    public static boolean createPod(Rule rule){
        RuleSpec ruleSpec = rule.getSpec();
        //创建pod
        boolean flag = false;
        switch (ruleSpec.getOwnType()){
//            case  DAEMONSET:
//                flag = PodsDAO.createDaemonsetPod(ruleSpec.getNamespace(),ruleSpec.getOwnName(),ruleSpec.getReplicas());
//                break;
            case REPLICASET:
                flag = PodsDAO.createReplicasetPod(ruleSpec.getNamespace(),ruleSpec.getOwnName(),ruleSpec.getReplicas());
                break;
            case DEPLOYMENT:
                flag = PodsDAO.createDeploymentPod(ruleSpec.getNamespace(),ruleSpec.getOwnName(),ruleSpec.getReplicas());
                break;
            case STATEFULSET:
                flag = PodsDAO.createStatefulsetPod(ruleSpec.getNamespace(),ruleSpec.getOwnName(),ruleSpec.getReplicas());
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
