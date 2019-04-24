package cn.harmonycloud.datacenter.dao.daoImpl;

import cn.harmonycloud.datacenter.dao.INodeDataDao;
import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.NodeData;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

import java.util.*;

import static cn.harmonycloud.datacenter.tools.Constant.*;

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
    public List<NodeData> findAllNodeDatas() {
        //{
        //	"query":{
        //		"bool": {
        //            "must": [
        //                {
        //                    "match_all": {}
        //                }
        //            ]
        //        }
        //	}
        //}
        List<NodeData> resultList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(NODE_INDEX)
                .withTypes(NODE_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .withPageable(PageRequest.of(0,100))
                .build();

        Page<NodeData> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, NodeData.class);
        String scrollId = ((ScrolledPage) scroll).getScrollId();
        while (scroll.hasContent()) {
            resultList.addAll(scroll.getContent());
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, NodeData.class);
        }
        elasticsearchTemplate.clearScroll(scrollId);

        return resultList;
    }

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
        List<DataPoint> resultList = new ArrayList<>();
        final SearchResultMapper dataPointResultMapper = new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<DataPoint> dataPoints = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
                    }
                    Map map = searchHit.getSourceAsMap();

                    DataPoint dataPoint = new DataPoint();
                    dataPoint.setValue(Double.parseDouble(map.get(indexName).toString()));
                    dataPoint.setTime((String) map.get("time"));
                    dataPoints.add(dataPoint);
                }
                if (dataPoints.size() > 0) {
                    return new AggregatedPageImpl<T>((List<T>) dataPoints, response.getScrollId());
                }
                return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
            }
        };
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
                .withSearchType(SearchType.DEFAULT)
                .withPageable(PageRequest.of(0,10))
                .build();
        Page<DataPoint> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, DataPoint.class,dataPointResultMapper);
        String scrollId = ((ScrolledPage) scroll).getScrollId();
        while (scroll.hasContent()) {
            resultList.addAll(scroll.getContent());
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, DataPoint.class,dataPointResultMapper);
        }
        elasticsearchTemplate.clearScroll(scrollId);

        return resultList;
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

    /**
     * 获取最近的时间
     *
     * @return
     */
    public String getRecentTime(){
        //GET /node/nodeData/_search
        //{
        //	"aggs":{
        //		"recent_time":{
        //			"max":{
        //				"field":"time"
        //			}
        //		}
        //	}
        //}
        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("recent_time").field("time");

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(NODE_INDEX)
                .withTypes(NODE_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .addAggregation(maxAggregationBuilder)
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        InternalMax internalMax = aggregations.get("recent_time");
        if(internalMax != null){
            return internalMax.getValueAsString();
        } else{
            return null;
        }
    }

    @Override
    public List<NodeData> getNowNodes() {
//        GET /node/nodedata/_search
//        {
//            "query":{
//                "bool":{
//                    "must":[
//                        {"match_phrase":{"time":"2019-01-01 00:00:00"}}
//                    ]
//                }
//            },
//        	"_source":["nodeName","nodeIP","podNums","cpuUsage","memUsage"]
//        }
        List<NodeData> resultList = new ArrayList<>();
//        final SearchResultMapper nowNodeResultMapper = new SearchResultMapper() {
//            @Override
//            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
//                List<Map> result = new ArrayList<>();
//                for (SearchHit searchHit : response.getHits()) {
//                    if (response.getHits().getHits().length <= 0) {
//                        return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
//                    }
//                    Map map = searchHit.getSourceAsMap();
//                    Map<String, Object> values = new HashMap<>();
//                    values.put("hostName", map.get("nodeName"));
//                    values.put("hostIP", map.get("nodeIP"));
//                    values.put("podNums", map.get("podNums"));
//                    values.put("cpuUsage", map.get("cpuUsage"));
//                    values.put("memUsage", map.get("memUsage"));
//                    result.add(values);
//                }
//                if (result.size() > 0) {
//                    return new AggregatedPageImpl<T>((List<T>) result, response.getScrollId());
//                }
//                return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
//            }
//        };

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("time",getRecentTime()));
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(NODE_INDEX)
                .withTypes(NODE_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .build();
        Page<NodeData> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, NodeData.class);
        String scrollId = ((ScrolledPage) scroll).getScrollId();
        while (scroll.hasContent()) {
            resultList.addAll(scroll.getContent());
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, NodeData.class);
        }
        elasticsearchTemplate.clearScroll(scrollId);
        return resultList;
    }
}
