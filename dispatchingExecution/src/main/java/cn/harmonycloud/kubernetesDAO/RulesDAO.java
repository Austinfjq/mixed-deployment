package cn.harmonycloud.kubernetesDAO;

import cn.harmonycloud.bean.DoneableRule;
import cn.harmonycloud.bean.Rule;
import cn.harmonycloud.bean.RuleList;
import cn.harmonycloud.bean.RuleSpec;
import cn.harmonycloud.utils.Constants;
import cn.harmonycloud.utils.K8sClient;
import cn.harmonycloud.utils.StringUtil;
import com.alibaba.fastjson.JSONArray;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 调用kubernetesAPI，对crd资源操作
 */
public class RulesDAO {
    private final static Logger LOGGER = LoggerFactory.getLogger(RulesDAO.class);
    //createRule func
    public static boolean createRule(String masterIp,Rule rule){
        CustomResourceDefinitionList ruleDefinitionList = K8sClient.getInstance(masterIp).customResourceDefinitions().list();
        CustomResourceDefinition ruleDefinition = new CustomResourceDefinition();
        for (CustomResourceDefinition e: ruleDefinitionList.getItems()){
            if (e.getApiVersion().equals(Constants.RULE_API_VERSION) && e.getKind().equals(Constants.RULE_KIND) && e.getMetadata().getName().equals(Constants.RULE_NAME)){
                ruleDefinition = e;
                break;
            }
        }
        LOGGER.info(ruleDefinition.toString());
        if (ruleDefinition != null && ruleDefinition.getMetadata() != null){
            LOGGER.info("Found Rule CRD:"+ruleDefinition.getMetadata().getSelfLink());
        }else{
            ruleDefinition = new CustomResourceDefinitionBuilder().withKind("CustomResourceDefinition").
                    withApiVersion("apiextensions.k8s.io/v1beta1").
                    withNewMetadata().withName("rules.crd.k8s.io").endMetadata().
                    withNewSpec().withGroup("crd.k8s.io").withVersion("v1").withScope("Namespaced").
                    withNewNames().withKind("Rule").withShortNames("").withPlural("rules").endNames().endSpec().
                    build();
        }
        NonNamespaceOperation<Rule, RuleList, DoneableRule, Resource<Rule, DoneableRule>> ruleClient = K8sClient.getInstance(masterIp).customResources(ruleDefinition, Rule.class, RuleList.class, DoneableRule.class);
        if (ruleClient == null){
            LOGGER.debug("RuleClient is null");
            return false;
        }
        rule.getSpec().setOwnerType("deployment");
        Rule result = ruleClient.create(rule);

        LOGGER.info("Create Rule{Rule.Name["+rule.getMetadata().getName()+"],Rule.Namespace["+rule.getMetadata().getNamespace()+"]} Successfully!");
        return true;
    }

    public static void main(String[] args){
        String nodeList = "[{\"score\":\"3\",\"hostname\":\"10.10.103.25-master\"},{\"score\":\"4\",\"hostname\":\"10.10.103.28-build\"}]";
        RuleSpec ruleSpec = new RuleSpec("wy","deployment","nginx", JSONArray.parseArray(nodeList),1);
        Rule rule = new Rule(ruleSpec);
        ObjectMeta meta = new ObjectMetaBuilder().withName(Constants.NAME_PREFIX+ StringUtil.randomStringGenerator(5)).build();
        meta.setNamespace("wy");
        rule.setMetadata(meta);
//        Rule rule = new Rule();
        createRule("10.10.102.25",rule);
    }
}
