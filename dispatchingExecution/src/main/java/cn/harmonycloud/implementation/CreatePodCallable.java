package cn.harmonycloud.implementation;

import cn.harmonycloud.bean.Rule;
import cn.harmonycloud.bean.RuleSpec;
import cn.harmonycloud.kubernetesDAO.RulesDAO;
import cn.harmonycloud.utils.Constants;
import cn.harmonycloud.utils.StringUtil;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;

import java.util.concurrent.Callable;

public class CreatePodCallable implements Callable<Boolean> {
    private String json;
    public CreatePodCallable(String json){
        this.json = json;
    }
    @Override
    public Boolean call() {
        //创建ruler
        RuleSpec ruleSpec = new RuleSpec(json);
        Rule rule = new Rule(ruleSpec);
        ObjectMeta meta = new ObjectMetaBuilder().withName(Constants.NAME_PREFIX+ StringUtil.randomStringGenerator(5)).build();
        rule.setMetadata(meta);
        RulesDAO.createRule(rule);
        //创建pod
        PodsImplementation.createPod(rule);
        return null;
    }
}
