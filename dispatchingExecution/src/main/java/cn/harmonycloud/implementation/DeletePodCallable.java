package cn.harmonycloud.implementation;

import java.util.concurrent.Callable;

public class DeletePodCallable implements Callable<Boolean> {
    private String namepsace;
    private String podName;

    public DeletePodCallable(String namespace,String podName){
        this.namepsace = namespace;
        this.podName = podName;
    }
    @Override
    public Boolean call() {
        PodsImplementation.deletePod(namepsace,podName);
        return null;
    }
}
