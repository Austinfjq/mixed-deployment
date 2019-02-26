package cn.harmonycloud.bean;

import io.fabric8.kubernetes.api.builder.Function;
import io.fabric8.kubernetes.client.CustomResourceDoneable;

public class DoneableRule extends CustomResourceDoneable<Rule> {
    public DoneableRule(Rule resource, Function function){super(resource,function);}
}
