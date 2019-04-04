package cn.harmonycloud.controller;

import cn.harmonycloud.bean.Rule;
import cn.harmonycloud.bean.RuleSpec;
import cn.harmonycloud.kubernetesDAO.PodsDAO;
import cn.harmonycloud.kubernetesDAO.RulesDAO;
import cn.harmonycloud.reference.Reference;
import cn.harmonycloud.utils.Constants;
import cn.harmonycloud.utils.StringUtil;
import cn.harmonycloud.utils.ThreadPoolUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        final JSONObject owner;
        owner = Reference.getOwnerOfPod(masterIp,namespace ,servicename);
        if (owner == null){
            LOGGER.debug("Owner is null");
            return false;
        }
        //countDownLatch
        final CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
        //create CRD Rule
        Runnable podRunnable = new Runnable(){
            @Override
            public void run() {
                RuleSpec ruleSpec = new RuleSpec(namespace,owner.getString("resourceKind"),owner.getString("resourceName"), JSONArray.parseArray(nodeList),REPLICAS);
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
                PodsDAO.createPod(masterIp,namespace,owner.getString("resourceKind"),owner.getString("resourceName"));
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
        return true;
    }

}
