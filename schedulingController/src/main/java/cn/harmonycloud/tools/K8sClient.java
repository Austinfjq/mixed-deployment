package cn.harmonycloud.tools;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @classname：K8sClient
 * @author：WANGYUZHONG
 * @date：2019/4/9 15:41
 * @description:TODO
 * @version:1.0
 **/
public class K8sClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(K8sClient.class);

    private static Map<String,KubernetesClient> CLIENTS = new HashMap<>();

    public static KubernetesClient getInstance(String masterIp){
        if (!CLIENTS.containsKey(masterIp)){
            synchronized (K8sClient.class){
                if (!CLIENTS.containsKey(masterIp)){
                    KubernetesClient CLIENT = createClient(masterIp);
                    if (CLIENT != null) {
                        CLIENTS.put(masterIp,CLIENT);
                    }
                    return CLIENT;
                }
            }
        }
        return CLIENTS.get(masterIp);
    }

    public static KubernetesClient createClient(String clusterIP) {
        Config config = new ConfigBuilder()
                .withOauthToken("330957b867a3462ea457bec41410624b")
                .withTrustCerts(true)
                .withMasterUrl("https://" + clusterIP +":6443/")
                .build();
        KubernetesClient client = new DefaultKubernetesClient(config);
        if (client == null) {
            LOGGER.error("Create k8s client failed！");
        }
        return client;
    }
}
