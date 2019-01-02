package cn.harmonycloud.bean;

import cn.harmonycloud.utils.Constants;
import io.fabric8.kubernetes.client.CustomResource;

public class Rule extends CustomResource {
    private RuleSpec spec;

    public Rule(RuleSpec ruleSpec){
        this.spec = ruleSpec;
        this.setApiVersion(Constants.API_VERSION);
        this.setKind(Constants.KIND);
    }

    public RuleSpec getSpec() {
        return spec;
    }

    public void setSpec(RuleSpec spec) {
        this.spec = spec;
    }
}

