package cn.harmonycloud.datacenter.dao.daoImpl;

import cn.harmonycloud.datacenter.dao.INodeDataDao;
import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.NodeData;
import cn.harmonycloud.datacenter.service.serviceImpl.NodeDataService;
import org.aspectj.lang.annotation.Around;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.InternalDateRange;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
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
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:20
 *@Modify By:
 */

@Repository(value = "nodeDataDao")
public class NodeDataDao implements INodeDataDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeDataDao.class);
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
    public List<DataPoint> getIndexDatas(String clusterMasterIP, String nodeName, String indexName, String startTime, String endTime) {
        //{
        //   "query": {
        //   		"bool" : {
        //            "must" : [
        //                {"match_phrase" : {"nodeName" : "10.10.101.65-share"}},
        //                {"match_phrase" : {"clusterMasterIP" : "10.10.101.65"}}
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
        //   "sort" : [
        // 	 {
        //     	"time" : "desc"
        //	 }
        //   ]
        //}
        List<DataPoint> resultList = new ArrayList<>();
        Client client = elasticsearchTemplate.getClient();
        QueryBuilder qb = termQuery("multi", "test");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("nodeName",nodeName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("clusterMasterIP",clusterMasterIP));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").format("yyyy-MM-dd HH:mm:ss").gte(startTime).lte(endTime));
        SearchResponse scrollResp = client.prepareSearch(NODE_INDEX)
                .addSort(SortBuilders.fieldSort("time").order(SortOrder.DESC))
                .setScroll(new TimeValue(60000))
                .setQuery(boolQueryBuilder)
                .setSize(100).get(); //max of 100 hits will be returned for each scroll
        //Scroll until no hits are returned
        do {
            //List<DataPoint> dataPoints = new ArrayList<>();
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                //Handle the hit...
                Map map = hit.getSourceAsMap();

                DataPoint dataPoint = new DataPoint();
                dataPoint.setValue(Double.parseDouble(map.get(indexName).toString()));
                dataPoint.setTime((String) map.get("time"));
                //dataPoints.add(dataPoint);
                resultList.add(dataPoint);
            }

            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
        } while(scrollResp.getHits().getHits().length != 0);
        return resultList;

        //TODO: 默认的spring-data-elasticsearch 3.1.6.RELEASE scroll api中没有添加addSort（见ElasticsearchTemplate.prepareScroll）的办法，无法进行排序。
        //      官方git上最新的代码已经添加了sort的功能，但还没有发布，后续发布后再更新代码。
//        List<DataPoint> resultList = new ArrayList<>();
//        final SearchResultMapper dataPointResultMapper = new SearchResultMapper() {
//            @Override
//            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
//                List<DataPoint> dataPoints = new ArrayList<>();
//                for (SearchHit searchHit : response.getHits()) {
//                    if (response.getHits().getHits().length <= 0) {
//                        return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
//                    }
//                    Map map = searchHit.getSourceAsMap();
//
//                    DataPoint dataPoint = new DataPoint();
//                    dataPoint.setValue(Double.parseDouble(map.get(indexName).toString()));
//                    dataPoint.setTime((String) map.get("time"));
//                    dataPoints.add(dataPoint);
//                }
//                if (dataPoints.size() > 0) {
//                    return new AggregatedPageImpl<T>((List<T>) dataPoints, response.getScrollId());
//                }
//                return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
//            }
//        };
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("nodeName",nodeName));
//        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("clusterMasterIP",clusterMasterIP));
//        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").format("yyyy-MM-dd HH:mm:ss").gte(startTime).lte(endTime));
//        SortBuilder sortBuilder = SortBuilders.fieldSort("time").order(SortOrder.DESC);
//
//        //String[] includes = {indexName,"time"};
//        //FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes, null);
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(boolQueryBuilder)
//                .withSort(sortBuilder)
//                .withIndices(NODE_INDEX)
//                .withTypes(NODE_TYPE)
//                .withSearchType(SearchType.DEFAULT)
//                .withPageable(PageRequest.of(0,10))
//                .build();
//        Page<DataPoint> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, DataPoint.class,dataPointResultMapper);
//        String scrollId = ((ScrolledPage) scroll).getScrollId();
//        while (scroll.hasContent()) {
//            resultList.addAll(scroll.getContent());
//            scrollId = ((ScrolledPage) scroll).getScrollId();
//            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, DataPoint.class,dataPointResultMapper);
//        }
//        elasticsearchTemplate.clearScroll(scrollId);
//        return resultList;
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
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("nodeName",nodeName))
                        .must(QueryBuilders.matchPhraseQuery("nodeIP",nodeIP))
                        .must(QueryBuilders.matchPhraseQuery("time",getRecentTime()));
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

//    @Override
//    public double getNodeCpuCores(String clusterMasterIP, String nodeName) {
//        //{
//        //	"query": {
//        //		"bool": {
//        //			"must": [{
//        //				"match_phrase": {
//        //					"time": "2019-05-07 10:44:05"
//        //				}
//        //			},
//        //			{
//        //				"match_phrase": {
//        //					"clusterMasterIP": "10.10.102.25"
//        //				}
//        //			},
//        //			{
//        //				"match_phrase": {
//        //					"nodeName": "10.10.103.31-share"
//        //				}
//        //			}]
//        //		}
//        //	},
//        //	"_source": ["cpuCores"]
//        //}
//
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("time",getRecentTime()))
//                        .must(QueryBuilders.matchPhraseQuery("clusterMasterIP",clusterMasterIP))
//                        .must(QueryBuilders.matchPhraseQuery("nodeName",nodeName));
//        String[] includes = {"cpuCores"};
//        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(boolQueryBuilder)
//                .withIndices(NODE_INDEX)
//                .withTypes(NODE_TYPE)
//                .withSourceFilter(fetchSourceFilter)
//                .withSearchType(SearchType.DEFAULT)
//                .build();
//        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, response -> response);
//        SearchHits searchHits = searchResponse.getHits();
//        if(searchHits.getTotalHits()>0){
//            for(SearchHit searchHit : searchHits){
//                Map map = searchHit.getSourceAsMap();
//                return (double) map.get("cpuCores");
//            }
//        }
//        return 0;
//    }

    public Object getFieldValueByClusterMasterIPAndNodeName(String clusterMasterIP, String nodeName, String fieldName){
        //{
        //	"query": {
        //		"bool": {
        //			"must": [{
        //				"match_phrase": {
        //					"time": "2019-05-07 10:44:05"
        //				}
        //			},
        //			{
        //				"match_phrase": {
        //					"clusterMasterIP": "10.10.102.25"
        //				}
        //			},
        //			{
        //				"match_phrase": {
        //					"nodeName": "10.10.103.31-share"
        //				}
        //			}]
        //		}
        //	},
        //	"_source": [fieldName]
        //}
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("time",getRecentTime()))
                .must(QueryBuilders.matchPhraseQuery("clusterMasterIP",clusterMasterIP))
                .must(QueryBuilders.matchPhraseQuery("nodeName",nodeName));
        String[] includes = {fieldName};
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
                return map.get(fieldName);
            }
        }
        return null;
    }

    @Override
    public Object getLastPeriodMaxFiledValue(String clusterMasterIP, String nodeName, String startTime, String endTime, String fieldName) {
        //{
        //	"query": {
        //		"bool": {
        //			"must": [{
        //				"match_phrase": {
        //					"clusterMasterIP": "10.10.102.25"
        //				}
        //			},
        //			{
        //				"match_phrase": {
        //					"nodeName": "10.10.103.31-share"
        //				}
        //			}]
        //		}
        //	},
        //	"aggs": {
        //		"group_by_time": {
        //			"range": {
        //				"field": "time",
        //				"ranges": [{
        //					"from": "2019-04-19 17:00:00",
        //					"to": "2019-05-29 19:00:00"
        //				}]
        //			},
        //			"aggs": {
        //				"max_value": {
        //					"max": {
        //						"field": fieldName
        //					}
        //				}
        //			}
        //		}
        //	}
        //}

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("nodeName",nodeName))
                        .must(QueryBuilders.matchPhraseQuery("clusterMasterIP",clusterMasterIP));

        MaxAggregationBuilder maxRequestConnections = AggregationBuilders.max("max_value").field(fieldName);
        DateRangeAggregationBuilder dateRangeAggregationBuilder = AggregationBuilders
                .dateRange("group_by_time")
                .field("time")
                .addRange(startTime, endTime);
        dateRangeAggregationBuilder.subAggregation(maxRequestConnections);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(NODE_INDEX)
                .withTypes(NODE_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .withPageable(PageRequest.of(0,1))//不返回Hits
                .addAggregation(dateRangeAggregationBuilder)
                .build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        InternalDateRange internalDateRange = aggregations.get("group_by_time");
        if(internalDateRange.getBuckets().size()>0){
            double max_value = 0.0;
            for (InternalDateRange.Bucket bk : internalDateRange.getBuckets()) {
                long count = bk.getDocCount();
                //得到所有子聚合
                Map subaggmap = bk.getAggregations().asMap();
                //获取指标的值，并返回
                max_value = ((InternalMax) subaggmap.get("max_value")).getValue();
            }
            return max_value;
        }else{
            return null;
        }
    }
}
