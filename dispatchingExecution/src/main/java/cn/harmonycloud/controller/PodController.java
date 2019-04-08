package cn.harmonycloud.controller;

import cn.harmonycloud.bean.Rule;
import cn.harmonycloud.bean.RuleSpec;
import cn.harmonycloud.kubernetesDAO.PodsDAO;
import cn.harmonycloud.kubernetesDAO.RulesDAO;
import cn.harmonycloud.reference.Reference;
import cn.harmonycloud.utils.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by hc on 19-4-4.
 */
public class PodController {
    private final static Logger LOGGER = LoggerFactory.getLogger(PodController.class);
    private final static int THREAD_NUM = 2;
    private final static int REPLICAS = 1;
    public static boolean createPodController(final String masterIp, final String namespace, String servicename, final String nodeList){
        //get Owner
        JSONObject owner = Reference.getOwnerOfPod(masterIp,namespace ,servicename);
        if (owner == null){
            LOGGER.debug("Owner is null");
            return false;
        }
        final String ownerName = owner.getString("resourceName");
        final String ownerType = owner.getString("resourceKind");
        //countDownLatch
        final CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
        //create CRD Rule
        Runnable podRunnable = new Runnable(){
            @Override
            public void run() {
                RuleSpec ruleSpec = new RuleSpec(namespace,ownerType,ownerName, JSONArray.parseArray(nodeList),REPLICAS);
                Rule rule = new Rule(ruleSpec);
                ObjectMeta meta = new ObjectMetaBuilder().withName(Constants.NAME_PREFIX+ StringUtil.randomStringGenerator(5)).build();
                meta.setNamespace(namespace);
                rule.setMetadata(meta);
                RulesDAO.createRule(masterIp,rule);
                countDownLatch.countDown();
            }
        };
        ThreadPoolUtils.getInstance().submit(podRunnable);
        //create Pod rule
        Runnable ruleRunnable = new Runnable(){
            @Override
            public void run() {
                PodsDAO.createPod(masterIp,namespace,ownerType,ownerName);
                countDownLatch.countDown();
            }
        };
        ThreadPoolUtils.getInstance().submit(ruleRunnable);
        try {
            countDownLatch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.debug("Cread Pod or Cread Rule timeout!");
            e.printStackTrace();
            return false;
        }
        //check whether rule and pod created!
        //暂时没想到如何在代码层面进行验证
        return true;
    }

    public static boolean deletePodController(final String masterIp,final String namespace,String servicename,final String podname){
        //Owner
        JSONObject owner = Reference.getOwnerOfPod(masterIp,namespace,servicename);
        if (owner == null){
            LOGGER.debug("Owner is null");
            return false;
        }
        final String ownerType = owner.getString("resourceKind");
        final String ownerName = owner.getString("resourceName");
        //countDownLatch
        final CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
        //delete pod
        Runnable deletePodRunnable = new Runnable() {
            @Override
            public void run() {
                PodsDAO.deletePod(masterIp,namespace,podname);
                countDownLatch.countDown();
            }
        };
        ThreadPoolUtils.getInstance().submit(deletePodRunnable);

        //subtract replicas
        Runnable subtractPodRunnable = new Runnable() {
            @Override
            public void run() {
                PodsDAO.subtractReplicas(masterIp,namespace,ownerType,ownerName);
                countDownLatch.countDown();
            }
        };
        ThreadPoolUtils.getInstance().submit(subtractPodRunnable);
        try {
            countDownLatch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.debug("Delete Pod or subtract replicas timeout!");
            e.printStackTrace();
            return false;
        }
        //check whether pod is deleted
        PodList podList = K8sClient.getInstance(masterIp).pods().list();
        for(Pod pod : podList.getItems()){
            if(pod.getMetadata().getName().equals(podname) && pod.getMetadata().getNamespace().equals(namespace)){
                return false;
            }
        }
        return true;
    }
}
