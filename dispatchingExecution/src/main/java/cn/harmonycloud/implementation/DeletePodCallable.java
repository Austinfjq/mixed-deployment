package cn.harmonycloud.implementation;

import cn.harmonycloud.reference.Reference;
import cn.harmonycloud.utils.OwnTypes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class DeletePodCallable implements Callable<Boolean> {
    private final static Logger LOGGER = LoggerFactory.getLogger(DeletePodCallable.class);

    private String servicename;
    private String namepsace;
    private String podName;

    public DeletePodCallable(String namespace,String servicename,String podname){
        this.namepsace = namespace;
        this.servicename = servicename;
        this.podName = podname;
    }
    @Override
    public Boolean call() {
        //Owner
        JSONObject owner = Reference.getOwnerOfPod(namepsace,servicename);
        //delete pod
        PodsImplementation.deletePod(namepsace,podName);
        //subtract replicas
        OwnTypes ownertype = null;
        switch (owner.get("ownerType").toString()){
            case "Deployment":ownertype = OwnTypes.DEPLOYMENT;break;
            case "Replicaset":ownertype = OwnTypes.REPLICASET;break;
            case "Statefulset":ownertype = OwnTypes.STATEFULSET;break;
            case "Daemonset":ownertype = OwnTypes.DAEMONSET;break;
            default:
                LOGGER.info("ownertype["+owner.get("ownerType").toString()+"] is unknown!");
                break;
        }
        PodsImplementation.subtractReplicas(namepsace,ownertype,owner.getString("ownerName"));
        return null;
    }
}
