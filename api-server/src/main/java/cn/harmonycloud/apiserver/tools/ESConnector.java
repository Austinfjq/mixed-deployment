//package cn.harmonycloud.apiserver.tools;
//
//
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//
//import java.net.InetAddress;
//import java.util.Optional;
//
//public class ESConnector {
//
//    private static final String clusterName = "elasticsearch";
//    private static final String IP = "10.10.101.115";
//    private static final int PORT = 9300;
//    private static final boolean sniff = true;
//
//
//
//    public Optional<Client> getClient() {
//        try {
//            Settings.Builder setting =Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", sniff);
//            TransportClient client = TransportClient.builder().settings(setting).build()
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP), PORT));
//            return Optional.of(client);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Optional.empty();
//        }
//    }
//
//
//    public static Client getInstance() {
//        return SingletonInstance.INSTANCE;
//    }
//
//
//    private static class SingletonInstance {
//        private static final Client INSTANCE = new ESConnector().getClient().get();
//    }
//
//
//    public static void main(String[] args) {
//        ESConnector esManager = new ESConnector();
//        Client client = esManager.getClient().get();
//
//        if (null == client) {
//            System.out.println("connect failed!");
//        }else {
//            System.out.println("connect succeed!");
//        }
//    }
//
//}
