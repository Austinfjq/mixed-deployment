package cn.harmonycloud.datacenter.dao.daoImpl;

import cn.harmonycloud.datacenter.dao.IServiceDataDao;
import cn.harmonycloud.datacenter.entity.DataPoint;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.InternalDateRange;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
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

@Repository(value = "serviceDataDao")
public class ServiceDataDao implements IServiceDataDao {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Double getIndexTimeSeries(String namespace, String serviceName, String indexName, String startTime, String endTime) {
        //GET /service/servicedata/_search
        //{
        //    "query" : {
        //        "bool" : {
        //            "must" : [
        //                {"match_phrase" : {"namespace" : "xxx"}},
        //                {"match_phrase" : {"serviceName" : "xxx"}}
        //            ]
        //        }
        //    },
        //    "agg" : {
        //        "group_by_time" : {
        //            "range" : {
        //                "field" : "time",
        //                "ranges" : [
        //                    {
        //                        "from": "2019-01-09 17:00:00",
        //                  		"to": "2019-01-09 19:00:00"
        //                    }
        //                ]
        //            },
        //            "aggs" : {
        //                "avg_index" : {
        //                    "avg" : {
        //                        "field" : "xxx"
        //                    }
        //                }
        //            }
        //        }
        //    }
        //}
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("namespace",namespace));

        AvgAggregationBuilder responseTimeAvg = AggregationBuilders.avg("avg_index").field(indexName);
        DateRangeAggregationBuilder dateRangeAggregationBuilder = AggregationBuilders
                .dateRange("group_by_time")
                .field("time")
                .addRange(startTime, endTime);
        dateRangeAggregationBuilder.subAggregation(responseTimeAvg);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .addAggregation(dateRangeAggregationBuilder)
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        InternalDateRange internalDateRange = aggregations.get("group_by_time");
        if(internalDateRange.getBuckets().size()>0){
            double avg_index = 0.0;
            for (InternalDateRange.Bucket bk : internalDateRange.getBuckets()) {
                long count = bk.getDocCount();
                //得到所有子聚合
                Map subaggmap = bk.getAggregations().asMap();
                //获取指标的均值，并返回
                avg_index = ((InternalAvg) subaggmap.get("avg_index")).getValue();
            }
            return avg_index;
        }else{
            return null;
        }
    }

    @Override
    public List<DataPoint> getIndexDatas(String namespace, String serviceName, String indexName, String startTime, String endTime) {
        //GET /service/servicedata/_search
        //{
        //   "query": {
        //   		"bool" : {
        //            "must" : [
        //                {"match_phrase" : {"namespace" : "hadoop"}},
        //                {"match_phrase" : {"serviceName" : "hadoop-datanode-web"}}
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
        //      "cpuUsage",
        //      "time"
        //   ]
        //}
        List<DataPoint> dataPoints = new ArrayList<>();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").format("yyyy-MM-dd HH:mm:ss").gte(startTime).lte(endTime));

        String[] includes = {indexName,"time"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes, null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
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
    public List<String> getAllPodNameFromOneService(String namespace, String serviceName, String clusterIP) {
        //GET /service/servicedata/_search
        //{
        //    "query" : {
        //        "bool" : {
        //            "must" : [
        //                {"match_phrase" : {"namespace" : xxx}},
        //                {"match_phrase" : {"serviceName" : xxx}},
        //                {"match_phrase" : {"clusterIP" : xxx}}
        //            ]
        //        }
        //    },
        //    "_source" : ["podList"]
        //}
        List<String> podNames = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("clusterIP",clusterIP));
        String[] includes = {"podList"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSourceFilter(fetchSourceFilter)
                .withSearchType(SearchType.DEFAULT)
                .build();
        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, response -> response);
        SearchHits searchHits = searchResponse.getHits();
        if(searchHits.getTotalHits() > 0){
            for (SearchHit searchHit : searchHits.getHits()){
                Map map = searchHit.getSourceAsMap();
                podNames.addAll((Collection<? extends String>) map.get("podList"));
            }
            return podNames;
        }else{
            return podNames;
        }
    }

    @Override
    public Map<String, Object> getStorageVolume(String namespace, String serviceName, String clusterIP) {
        //GET /service/servicedata/_search
        //{
        //    "query" : {
        //        "bool" : {
        //            "must" : [
        //                {"match_phrase" : {"field" : "namespace"}},
        //                {"match_phrase" : {"field" : "serviceName"}},
        //                 {"match_phrase" : {"field" : "clusterIP"}}
        //            ]
        //        }
        //    },
        //    "_source" : ["volumeType","volumeUsage"]
        //}
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("clusterIP",clusterIP));
        String[] includes = {"volumeType","volumeUsage"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSourceFilter(fetchSourceFilter)
                .withSearchType(SearchType.DEFAULT)
                .build();
        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, response -> response);
        SearchHits searchHits = searchResponse.getHits();
        if(searchHits.getTotalHits() > 0){
            for (SearchHit searchHit : searchHits.getHits()){
                Map map = searchHit.getSourceAsMap();
                return map;
            }
        }
        return new HashMap<>();
    }

    @Override
    public List<String> getAllServiceNames(String namespace) {
        //{
        //    "query":{
        //        "bool":{
        //            "must":[
        //                {"match_phrase":{"namespace":"hadoop"}}
        //            ]
        //        }
        //    },
        //	"_source":["serviceName"]
        //}
        List<String> resultList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must().add(QueryBuilders.matchPhraseQuery("namespace",namespace));
        String[] includes = {"serviceName"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSourceFilter(fetchSourceFilter)
                .withSearchType(SearchType.DEFAULT)
                .build();
        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, response -> response);
        SearchHits searchHits = searchResponse.getHits();
        if(searchHits.getTotalHits() > 0){
            for (SearchHit searchHit : searchHits.getHits()){
                Map map = searchHit.getSourceAsMap();
                resultList.add((String) map.get("serviceName"));
            }
        }
        return resultList;
    }

    @Override
    public Map<String, Object> getPodNums(String namespace, String serviceName, String clusterIP) {
        //GET /service/servicedata/_search
        //{
        //    "query":{
        //        "bool":{
        //            "must":[
        //                {"match_phrase":{"namespace":"hadoop"}},
        //                {"match_phrase":{"serviceName":"hadoop-datanode-web"}},
        //                {"match_phrase":{"clusterIP":"10.97.210.52"}}
        //            ]
        //        }
        //    },
        //	"_source":["podNums"]
        //}
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must().add(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.must().add(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must().add(QueryBuilders.matchPhraseQuery("clusterIP",clusterIP));
        String[] includes = {"podNums"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSourceFilter(fetchSourceFilter)
                .withSearchType(SearchType.DEFAULT)
                .build();
        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, response -> response);
        SearchHits searchHits = searchResponse.getHits();
        if(searchHits.getTotalHits() > 0){
            for (SearchHit searchHit : searchHits.getHits()){
                Map map = searchHit.getSourceAsMap();
                return map;
            }
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getNetVolume(String namespace, String serviceName, String clusterIP) {
        //GET /service/servicedata/_search
        //{
        //    "query":{
        //        "bool":{
        //            "must":[
        //                {"match_phrase":{"namespace":"hadoop"}},
        //                {"match_phrase":{"serviceName":"hadoop-datanode-web"}},
        //                {"match_phrase":{"clusterIP":"10.97.210.52"}}
        //            ]
        //        }
        //    },
        //	"_source":["requestBytes","responseBytes"]
        //}
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must().add(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.must().add(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must().add(QueryBuilders.matchPhraseQuery("clusterIP",clusterIP));
        String[] includes = {"requestBytes","responseBytes"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSourceFilter(fetchSourceFilter)
                .withSearchType(SearchType.DEFAULT)
                .build();
        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, response -> response);
        SearchHits searchHits = searchResponse.getHits();
        if(searchHits.getTotalHits() > 0){
            for (SearchHit searchHit : searchHits.getHits()){
                Map map = searchHit.getSourceAsMap();
                return map;
            }
        }
        return new HashMap<>();
    }

    @Override
    public List<Map<String,Object>> getResourceConsume(String namespace, String serviceName, String clusterIP, String startTime, String endTime) {
        //GET /service/servicedata/_search
        //{
        //    "query":{
        //        "bool":{
        //            "must":[
        //                {"match_phrase":{"namespace":"hadoop"}},
        //                {"match_phrase":{"serviceName":"hadoop-datanode-web"}},
        //                {"match_phrase":{"clusterIP":"10.97.210.52"}}
        //            ],
        //            "filter":{
        //                "range":{
        //                    "time":{
        //                        "gte": "2019-01-01 00:00:00",
        //           				"lte": "2019-02-01 00:00:00",
        //      					"format": "yyyy-MM-dd HH:mm:ss"
        //                    }
        //                }
        //            }
        //        }
        //    },
        //	"_source":["requestBytes","responseBytes","time","cpuUsage","memUsage"]
        //}
        List<Map<String,Object>> resultList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("clusterIP",clusterIP));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").format("yyyy-MM-dd HH:mm:ss").gte(startTime).lte(endTime));
        String[] includes = {"requestBytes","responseBytes","time","cpuUsage","memUsage"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSourceFilter(fetchSourceFilter)
                .withSearchType(SearchType.DEFAULT)
                .build();
        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, response -> response);
        SearchHits searchHits = searchResponse.getHits();
        if(searchHits.getTotalHits() > 0){
            for (SearchHit searchHit : searchHits.getHits()){
                Map map = searchHit.getSourceAsMap();
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> getLoadMappingInstances(String namespace, String serviceName, String clusterIP, String startTime, String endTime) {
        //GET /service/servicedata/_search
        //{
        //    "query":{
        //        "bool":{
        //            "must":[
        //                {"match_phrase":{"namespace":"hadoop"}},
        //                {"match_phrase":{"serviceName":"hadoop-datanode-web"}},
        //                {"match_phrase":{"clusterIP":"10.97.210.52"}}
        //            ],
        //            "filter":{
        //                "range":{
        //                    "time":{
        //                        "gte": "2019-01-01 00:00:00",
        //           				"lte": "2019-02-01 00:00:00",
        //      					"format": "yyyy-MM-dd HH:mm:ss"
        //                    }
        //                }
        //            }
        //        }
        //    },
        //	"_source":[time","requestConnections","podNums"]
        //}
        List<Map<String,Object>> resultList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("clusterIP",clusterIP));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").format("yyyy-MM-dd HH:mm:ss").gte(startTime).lte(endTime));
        String[] includes = {"time","requestConnections","podNums"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSourceFilter(fetchSourceFilter)
                .withSearchType(SearchType.DEFAULT)
                .build();
        SearchResponse searchResponse = elasticsearchTemplate.query(searchQuery, response -> response);
        SearchHits searchHits = searchResponse.getHits();
        if(searchHits.getTotalHits() > 0){
            for (SearchHit searchHit : searchHits.getHits()){
                Map map = searchHit.getSourceAsMap();
                resultList.add(map);
            }
        }
        return resultList;
    }

    @Override
    public Double getAvgResponseTime(String namespace, String serviceName, String clusterIP, String startTime, String endTime) {
    //        GET /service/servicedata/_search
    //        {
    //            "query" : {
    //                "bool" : {
    //                    "must" : [
    //                        {"match_phrase" : {"namespace" : "xxx"}},
    //                        {"match_phrase" : {"serviceName" : "xxx"}}
    //                        {"match_phrase" : {"clusterIP" : "xxx"}}
    //                    ]
    //                }
    //            },
    //            "aggs" : {
    //                "group_by_time" : {
    //                    "range" : {
    //                        "field" : "time",
    //                        "ranges" : [
    //                            {
    //                                "from": "2019-01-09 17:00:00",
    //                          		"to": "2019-01-09 19:00:00"
    //                            }
    //                        ]
    //                    },
    //                    "aggs" : {
    //                        "avg_responseTime" : {
    //                            "avg" : {
    //                                "field" : "responseTime"
    //                            }
    //                        }
    //                    }
    //                }
    //            }
    //        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("clusterIP",clusterIP));
        AvgAggregationBuilder responseTimeAvg = AggregationBuilders.avg("avg_responseTime").field("responseTime");
        DateRangeAggregationBuilder dateRangeAggregationBuilder = AggregationBuilders
                .dateRange("group_by_time")
                .field("time")
                .addRange(startTime, endTime);
        dateRangeAggregationBuilder.subAggregation(responseTimeAvg);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .addAggregation(dateRangeAggregationBuilder)
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        InternalDateRange internalDateRange = aggregations.get("group_by_time");
        if(internalDateRange.getBuckets().size()>0){
            double avg_responseTime = 0.0;
            for (InternalDateRange.Bucket bk : internalDateRange.getBuckets()) {
                long count = bk.getDocCount();
                //得到所有子聚合
                Map subaggmap = bk.getAggregations().asMap();
                //获取指标的均值，并返回
                avg_responseTime = ((InternalAvg) subaggmap.get("avg_responseTime")).getValue();
            }
            return avg_responseTime;
        }else{
            return null;
        }
    }
}
