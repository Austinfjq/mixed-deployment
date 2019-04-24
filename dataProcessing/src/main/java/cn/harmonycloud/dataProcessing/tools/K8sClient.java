package cn.harmonycloud.dataProcessing.tools;

import cn.harmonycloud.dataProcessing.metric.Constant;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;


public class K8sClient {

    private final static KubernetesClient K8S_CLIENT = new DefaultKubernetesClient(new ConfigBuilder()
            .withOauthToken("330957b867a3462ea457bec41410624b")
            .withTrustCerts(true).withMasterUrl(Constant.K8S_MASTER_URL).build());

    private K8sClient() {
    }

    public static KubernetesClient getClient() {
        return K8S_CLIENT;
    }

}
