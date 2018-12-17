package cn.harmonycloud.apiserver.tools;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ElasticSearchUtil {


    public static boolean insertEsByBulk(String clusterName, String ip, int port, String inedx, String type,
                                         ArrayList<Map<String, Object>> list)
            throws IOException {
        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", true)
                .build();
        TransportClient client;
        PreBuiltTransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
        preBuiltTransportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
        client = preBuiltTransportClient;
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        int rowNum = list.size();

        for (int i = 0; i <= rowNum; i++) {
            Map<String, Object> map = list.get(i);

            XContentBuilder xContentBuilder = jsonBuilder();
            xContentBuilder.startObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                xContentBuilder.field(entry.getKey(), entry.getValue());
            }
            xContentBuilder.endObject();
            bulkRequest.add(client.prepareIndex(inedx, type).setSource(xContentBuilder));

        }

        BulkResponse bulkResponse = bulkRequest.get();
        client.close();

        return !bulkResponse.hasFailures();
    }
}
