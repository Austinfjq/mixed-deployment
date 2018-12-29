package cn.harmonycloud.kubernetesDAO;

import cn.harmonycloud.bean.Rule;
import cn.harmonycloud.utils.K8sClient;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionList;

import java.util.List;

/**
 * 调用kubernetesAPI，对crd资源操作
 */
public class RulesDAO {
    //createRule func
    public static boolean createRule(Rule rule){
            CustomResourceDefinition ruleTemp = new CustomResourceDefinitionBuilder()
                    .withApiVersion("apiextensions.k8s.io/v1beta1")
                    .withNewMetadata().withName("rule1").endMetadata()
                    .withNewSpec().withGroup("crd.k8s.io").withVersion("v1").withScope("Namespaced")
                    .withNewNames().withKind("Rule").withPlural("rules").endNames()
                    .endSpec()
                    .build();
            K8sClient.getInstance().customResourceDefinitions().create(ruleTemp);
            return false;
    }
}
