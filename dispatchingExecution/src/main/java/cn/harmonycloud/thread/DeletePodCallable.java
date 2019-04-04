package cn.harmonycloud.thread;

import cn.harmonycloud.kubernetesDAO.PodsDAO;
import cn.harmonycloud.reference.Reference;
import cn.harmonycloud.utils.OwnTypes;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

public class DeletePodCallable implements Callable<Boolean> {
    private final static Logger LOGGER = LoggerFactory.getLogger(DeletePodCallable.class);
    private String masterIp;
    private String servicename;
    private String namepsace;
    private String podName;

    public DeletePodCallable(String masterIp,String namespace,String servicename,String podname){
        this.masterIp = masterIp;
        this.namepsace = namespace;
        this.servicename = servicename;
        this.podName = podname;
    }
    @Override
    public Boolean call() throws KeyManagementException, NoSuchAlgorithmException {
        //Owner
        JSONObject owner = Reference.getOwnerOfPod(masterIp,namepsace,servicename);
        //delete pod
        PodsDAO.deletePod(masterIp,namepsace,podName);
        //subtract replicas
        OwnTypes ownertype = null;
        switch (owner.get("resourceKind").toString()){
            case "Deployment":ownertype = OwnTypes.DEPLOYMENT;break;
            case "Replicaset":ownertype = OwnTypes.REPLICASET;break;
            case "Statefulset":ownertype = OwnTypes.STATEFULSET;break;
            case "Daemonset":ownertype = OwnTypes.DAEMONSET;break;
            default:
                LOGGER.info("ownertype["+owner.get("ownerType").toString()+"] is unknown!");
                break;
        }
        PodsDAO.subtractReplicas(masterIp,namepsace,ownertype,owner.getString("resourceName"));
        return true;
    }
}
