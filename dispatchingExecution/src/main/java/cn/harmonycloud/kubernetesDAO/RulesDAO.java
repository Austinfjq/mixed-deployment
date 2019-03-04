package cn.harmonycloud.kubernetesDAO;

import cn.harmonycloud.bean.DoneableRule;
import cn.harmonycloud.bean.Rule;
import cn.harmonycloud.bean.RuleList;
import cn.harmonycloud.utils.Constants;
import cn.harmonycloud.utils.K8sClient;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionList;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 调用kubernetesAPI，对crd资源操作
 */
public class RulesDAO {
    private final static Logger LOGGER = LoggerFactory.getLogger(RulesDAO.class);
    //createRule func
    public static boolean createRule(Rule rule){
        CustomResourceDefinitionList ruleDefinitionList = K8sClient.getInstance().customResourceDefinitions().list();
        CustomResourceDefinition ruleDefinition = new CustomResourceDefinition();
        for (CustomResourceDefinition e: ruleDefinitionList.getItems()){
            if (e.getApiVersion().equals(Constants.RULE_API_VERSION) && e.getKind().equals(Constants.RULE_KIND) && e.getMetadata().getName().equals(Constants.RULE_NAME)){
                ruleDefinition = e;
                break;
            }
        }
        NonNamespaceOperation<Rule, RuleList, DoneableRule, Resource<Rule, DoneableRule>> ruleClient = K8sClient.getInstance().customResources(ruleDefinition, Rule.class, RuleList.class, DoneableRule.class);
        ruleClient.create(rule);
        LOGGER.info("Create Rule{Rule.Name["+rule.getMetadata().getName()+"],Rule.Namespace["+rule.getMetadata().getNamespace()+"]} Successfully!");
        return true;
    }
}
