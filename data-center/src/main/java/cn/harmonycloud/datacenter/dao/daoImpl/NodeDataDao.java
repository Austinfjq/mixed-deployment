package cn.harmonycloud.datacenter.dao.daoImpl;

import cn.harmonycloud.datacenter.dao.INodeDataDao;
import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.NodeData;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.harmonycloud.datacenter.tools.Constant.NODE_INDEX;
import static cn.harmonycloud.datacenter.tools.Constant.NODE_TYPE;


/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:20
 *@Modify By:
 */

@Repository(value = "nodeDataDao")
public class NodeDataDao implements INodeDataDao {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<DataPoint> getIndexDatas(String nodeName, String nodeIP, String indexName, String startTime, String endTime) {
        //{
        //   "query": {
        //   		"bool" : {
        //            "must" : [
        //                {"match_phrase" : {"nodeName" : "10.10.101.65-share"}},
        //                {"match_phrase" : {"nodeIP" : "10.10.101.65"}}
        //            ],
        //            "filter":{
        //            	"range": {
        //        			"time": {
        //        				"gte": "2019-01-01 00:00:00",
        //            			"lte": "2019-02-01 00:00:00",
        //            			"format": "yyyy-MM-dd HH:mm:ss"
        //        			}
        //				}
        //            }
        //        }
        //   },
        //   "_source": [
        //      "allocatablePods",
        //      "time"
        //   ]
        //}
        List<DataPoint> dataPoints = new ArrayList<>();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("nodeName",nodeName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("nodeIP",nodeIP));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").format("yyyy-MM-dd HH:mm:ss").gte(startTime).lte(endTime));

        String[] includes = {indexName,"time"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes, null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(NODE_INDEX)
                .withTypes(NODE_TYPE)
                .withSourceFilter(fetchSourceFilter)
                .withSearchType(SearchType.DEFAULT)
                .build();
        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, response -> response);
        SearchHits searchHits = searchResponse.getHits();
        if(searchHits.getTotalHits() > 0){
            for (SearchHit searchHit : searchHits.getHits()){
                Map map = searchHit.getSourceAsMap();
                DataPoint dataPoint = new DataPoint();
                dataPoint.setValue((Double) map.get(indexName));
                dataPoint.setTime((String) map.get("time"));
                dataPoints.add(dataPoint);
            }
            return dataPoints;
        }else{
            return dataPoints;
        }
    }

    @Override
    public Map<String, Object> getNodeConditions(String nodeName, String nodeIP) {
        //GET /node/nodedata/_search
        //{
        //    "query":{
        //        "bool":{
        //            "must":[
        //                {"match_phrase":{"nodeName":"10.10.103.27-slave"}},
        //                {"match_phrase":{"nodeIP":"10.10.102.27"}}
        //            ]
        //        }
        //    },
        //	"_source":["nodeConditions"]
        //}
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("nodeName",nodeName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("nodeIP",nodeIP));
        String[] includes = {"nodeConditions"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(NODE_INDEX)
                .withTypes(NODE_TYPE)
                .withSourceFilter(fetchSourceFilter)
                .withSearchType(SearchType.DEFAULT)
                .build();
        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, response -> response);
        SearchHits searchHits = searchResponse.getHits();
        if(searchHits.getTotalHits()>0){
            for(SearchHit searchHit : searchHits){
                Map map = searchHit.getSourceAsMap();
                return map;
            }
        }
        return new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> getNowNodes(String now) {
        //GET /node/nodedata/_search
        //{
        //    "query":{
        //        "bool":{
        //            "must":[
        //                {"match_phrase":{"time":"2019-01-01 00:00:00"}}
        //            ]
        //        }
        //    },
        //	"_source":["nodeName","nodeIP","podNums","cpuUsage","memUsage"]
        //}
        List<Map<String,Object>> resultList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("time",now));
        String[] includes = {"nodeName","nodeIP","podNums","cpuUsage","memUsage"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(NODE_INDEX)
                .withTypes(NODE_TYPE)
                .withSourceFilter(fetchSourceFilter)
                .withSearchType(SearchType.DEFAULT)
                .build();
        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, response -> response);
        SearchHits searchHits = searchResponse.getHits();
        if(searchHits.getTotalHits()>0){
            for(SearchHit searchHit : searchHits){
                Map map = searchHit.getSourceAsMap();
                Map<String, Object> result = new HashMap<>();
                result.put("hostName", map.get("nodeName"));
                result.put("hostIP", map.get("nodeIP"));
                result.put("podNums", map.get("podNums"));
                result.put("cpuUsage", map.get("cpuUsage"));
                result.put("memUsage", map.get("memUsage"));
                resultList.add(result);
            }
        }
        return resultList;
    }
}
