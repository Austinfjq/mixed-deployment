package cn.harmonycloud.apiserver.tools;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author wangyuzhong
 * @date 18-12-17 上午10:55
 * @Despriction
 */
public class Util {


    public static TransportClient getConnect() {
        TransportClient client = null;
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch").build();
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.101.115"), 9300));
        } catch (UnknownHostException e) {
            System.out.println("主机不存在!");
            e.printStackTrace();
        }
        return client;
    }


    public static void main(String[] args) {


Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = null;
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.101.115"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("user", "小明");
        json.put("title", "Java Engineer");
        json.put("desc", "web 开发");
        IndexResponse response = client.prepareIndex("accounts", "person")
                .setSource(json, XContentType
                        .JSON).get();
        String _index = response.getIndex();
        System.out.println(_index);
    }
}
