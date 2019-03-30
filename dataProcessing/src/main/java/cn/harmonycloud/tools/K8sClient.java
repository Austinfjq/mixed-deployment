package cn.harmonycloud.tools;

import cn.harmonycloud.metric.Constant;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;


public class K8sClient {
    private final static KubernetesClient K8S_CLIENT = new DefaultKubernetesClient(new ConfigBuilder()
            .withTrustCerts(true).withMasterUrl(Constant.K8S_MASTER).build());

    private K8sClient() {
    }

    public static KubernetesClient getClient() {
        return K8S_CLIENT;
    }

}
