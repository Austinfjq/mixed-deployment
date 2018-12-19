package cn.harmonycloud.apiserver.DAO;

import cn.harmonycloud.apiserver.entry.ForecastResultCell;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author wangyuzhong
 * @date 18-12-14 下午2:04
 * @Despriction
 */

@Repository
public class ForecastResultSaveDAO {


    public static boolean insertEsByBulk(Client client, String index, String type,
                                         List<Map<String, Object>> list)
            throws IOException {
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
            bulkRequest.add(client.prepareIndex(index, type).setSource(xContentBuilder));

        }

        BulkResponse bulkResponse = bulkRequest.get();
        client.close();

        return !bulkResponse.hasFailures();
    }


    public static <T> Map<String, Object> objectToMap(T obj) throws Exception {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }

        return map;
    }

    public List<Map<String,Object>> objectsToMaps(List<ForecastResultCell> forecastResultCellList) throws Exception{
        if (null == forecastResultCellList) {
            System.out.println("list is null!");
            return null;
        }

        List<Map<String,Object>> maps = new ArrayList<>();
        for(ForecastResultCell forecastResultCell:forecastResultCellList) {
            maps.add(objectToMap(forecastResultCell));
        }

        return maps;
    }

}
