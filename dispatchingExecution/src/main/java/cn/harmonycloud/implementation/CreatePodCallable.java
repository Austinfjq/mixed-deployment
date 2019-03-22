package cn.harmonycloud.implementation;

import cn.harmonycloud.bean.Rule;
import cn.harmonycloud.bean.RuleSpec;
import cn.harmonycloud.kubernetesDAO.RulesDAO;
import cn.harmonycloud.reference.Reference;
import cn.harmonycloud.utils.Constants;
import cn.harmonycloud.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

public class CreatePodCallable implements Callable<Boolean> {
    private final static Logger LOGGER = LoggerFactory.getLogger(CreatePodCallable.class);
    private String namespace;
    private String servicename;
    private String nodeList;
    public CreatePodCallable(String namespace,String servicename,String nodeList){
        this.namespace = namespace;
        this.servicename = servicename;
        this.nodeList = nodeList;
    }
    @Override
    public Boolean call() throws KeyManagementException, NoSuchAlgorithmException {
        //getOwner
        JSONObject owner = Reference.getOwnerOfPod(namespace ,servicename);
        if (owner == null){
            LOGGER.debug("Owner is null");
            return false;
        }
        //创建ruler
        RuleSpec ruleSpec = new RuleSpec(namespace,owner, JSONArray.parseArray(nodeList));
//        LOGGER.info("RuleSpec:"+ruleSpec.getNodeList().size());
        Rule rule = new Rule(ruleSpec);
        ObjectMeta meta = new ObjectMetaBuilder().withName(Constants.NAME_PREFIX+ StringUtil.randomStringGenerator(5)).build();
        meta.setNamespace(namespace);
        rule.setMetadata(meta);
        LOGGER.info("mata:"+meta.toString());
        LOGGER.info("Rule:"+rule.toString());
        RulesDAO.createRule(rule);
        //创建pod
        PodsImplementation.createPod(rule);
        return true;
    }
}
