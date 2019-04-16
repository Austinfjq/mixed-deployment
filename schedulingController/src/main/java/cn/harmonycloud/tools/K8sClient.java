package cn.harmonycloud.tools;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @classname：K8sClient
 * @author：WANGYUZHONG
 * @date：2019/4/9 15:41
 * @description:TODO
 * @version:1.0
 **/
public class K8sClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(K8sClient.class);

    @Value("${CERT_DATA}")
    private static String CERT_DATA;

    @Value("${KEY_DATA}")
    private static String KEY_DATA;

    @Value("${CA_DATA}")
    private static String CA_DATA;

    public static KubernetesClient createClient(String clusterIP, String port, String namespace) {
        Config config = new ConfigBuilder().withClientCertData(CERT_DATA).withClientKeyData(KEY_DATA).withCaCertData(CA_DATA).withMasterUrl("https://" + clusterIP +":" + port + "/").withUsername("kubernetes-admin").withNamespace(namespace).build();
        KubernetesClient client = new DefaultKubernetesClient(config);
        if (client == null) {
            LOGGER.error("Create k8s client failed！");
        }
        return client;
    }
}
