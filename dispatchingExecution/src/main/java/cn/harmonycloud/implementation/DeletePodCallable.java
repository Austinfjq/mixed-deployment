package cn.harmonycloud.implementation;

import java.util.concurrent.Callable;

public class DeletePodCallable implements Callable<Boolean> {
    private String json;

    public DeletePodCallable(String json){
        this.json = json;
    }
    @Override
    public Boolean call() {
        PodsImplementation.deletePod(json);
        return null;
    }
}
