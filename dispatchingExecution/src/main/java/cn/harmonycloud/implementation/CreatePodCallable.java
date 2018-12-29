package cn.harmonycloud.implementation;

import cn.harmonycloud.bean.Rule;

import java.util.concurrent.Callable;

public class CreatePodCallable implements Callable<Boolean> {
    private String json;
    public CreatePodCallable(String json){
        this.json = json;
    }
    @Override
    public Boolean call() {
        //创建ruler
        Rule rule = new Rule(json);
        //创建pod
        PodsImplementation.createPod(rule);
        return null;
    }
}
