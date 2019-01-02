package cn.harmonycloud.utils;

import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * KubernetesClient 单例
 */
public class K8sClient {
    private static KubernetesClient CLIENT;
    private K8sClient(){}
    public static KubernetesClient getInstance(){
        if (CLIENT == null){
            synchronized (K8sClient.class){
                if (CLIENT == null){
                    CLIENT = new DefaultKubernetesClient(new ConfigBuilder().withTrustCerts(true).withMasterUrl(Constants.MASTER).build());
                }
            }
        }
        return CLIENT;
    }
}
