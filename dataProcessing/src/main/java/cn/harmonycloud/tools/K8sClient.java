package cn.harmonycloud.tools;

import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;


public class K8sClient {
//    public static void main(String[] args){
//        String master = "https://10.10.102.25:6443/";
//        if (args.length == 1) {
//            master = args[0];
//        }
//        Config config = new ConfigBuilder().withTrustCerts(true).withMasterUrl(master).build();
//        final KubernetesClient client = new DefaultKubernetesClient(config);
//        System.out.println(
//                client.pods().withLabel("run", "nginx-app").list()
//        );
//
//        Deployment deployment = ((DefaultKubernetesClient) client).apps().deployments().inNamespace("wy").list().getItems().get(0);
//        int rep = deployment.getSpec().getReplicas();
//        client.apps().deployments().inNamespace("wy").withName("nginx-app").scale(2);
//    }

    private final static KubernetesClient K8S_CLIENT = new DefaultKubernetesClient(new ConfigBuilder().withTrustCerts(true).withMasterUrl(Constant.MASTER).build());

    private K8sClient(){
    }

    public static KubernetesClient getClient(){
        return K8S_CLIENT;
    }

}
