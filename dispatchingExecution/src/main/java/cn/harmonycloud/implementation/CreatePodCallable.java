package cn.harmonycloud.implementation;

import java.util.concurrent.Callable;

public class CreatePodCallable implements Callable<Boolean> {
    private String json;
    public CreatePodCallable(String json){
        this.json = json;
    }
    @Override
    public Boolean call() {
        //创建ruler

        //创建pod
        PodsImplementation.createPod(json);
        return null;
    }
}
