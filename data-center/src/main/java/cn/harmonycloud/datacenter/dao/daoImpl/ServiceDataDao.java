package cn.harmonycloud.datacenter.dao.daoImpl;

import cn.harmonycloud.datacenter.dao.IServiceDataDao;
import cn.harmonycloud.datacenter.entity.DataPoint;
import cn.harmonycloud.datacenter.entity.es.ServiceData;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.InternalDateRange;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
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

@Repository(value = "serviceDataDao")
public class ServiceDataDao implements IServiceDataDao {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<ServiceData> findAllServiceDatas() {
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
        List<ServiceData> resultList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .withPageable(PageRequest.of(0,100))
                .build();

        Page<ServiceData> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, ServiceData.class);
        String scrollId = ((ScrolledPage) scroll).getScrollId();
        while (scroll.hasContent()) {
            resultList.addAll(scroll.getContent());
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, ServiceData.class);
        }
        elasticsearchTemplate.clearScroll(scrollId);

        return resultList;
    }

    @Override
    public Double getIndexTimeSeries(String namespace, String serviceName, String indexName, String startTime, String endTime) {
        //GET /service/serviceData/_search
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
    public List<DataPoint> getIndexDatas(String clusterMasterIP,String namespace, String serviceName, String indexName, String startTime, String endTime) {
        //GET /service/serviceData/_search
        //{
        //   "query": {
        //   		"bool" : {
        //            "must" : [
        //                { "h"match_phrase" : {"clusterMasterIP" :"xxx"}},
        //                { "h"match_phrase" : {"namespace" :"adoop"}},
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
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("clusterMasterIP",clusterMasterIP));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").format("yyyy-MM-dd HH:mm:ss").gte(startTime).lte(endTime));

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
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
    public List<String> getAllPodNameFromOneService(String namespace, String serviceName, String clusterIP) {
        //GET /service/serviceData/_search
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
        //GET /service/serviceData/_search
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
        //	"size":0,
        //	"query":{
        //		"bool":{
        //			"must":[
        //				{
        //					"match_phrase":{
        //						"namespace":"istio-system"
        //					}
        //				}
        //			]
        //		}
        //	},
        //	"aggs":{
        //		"service_name":{
        //			"terms":{
        //				"field":"serviceName",
        //				"size":100
        //			}
        //		}
        //	}
        //}

        List<String> resultList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("namespace",namespace));
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("service_name").field("serviceName").size(Integer.MAX_VALUE);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .addAggregation(termsAggregationBuilder)
                .build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        StringTerms stringTerms = aggregations.get("service_name");

        if(stringTerms.getBuckets().size()>0){
            //double avg_index = 0.0;
            for (Terms.Bucket bk : stringTerms.getBuckets()) {
                long count = bk.getDocCount();
                //得到所有子聚合
                resultList.add(bk.getKeyAsString());

            }
        }
        return resultList;
    }

    @Override
    public Map<String, Object> getPodNums(String namespace, String serviceName, String clusterIP) {
        //GET /service/serviceData/_search
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
        //GET /service/serviceData/_search
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
    public List<Map> getResourceConsume(String namespace, String serviceName, String clusterIP, String startTime, String endTime) {
        //GET /service/serviceData/_search
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
        List<Map> resultList = new ArrayList<>();

        final SearchResultMapper resourceConsumeResultMapper = new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Map> result = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
                    }
                    Map map = searchHit.getSourceAsMap();
                    Map<String, Object> values = new HashMap<>();
                    values.put("requestBytes", map.get("requestBytes"));
                    values.put("responseBytes", map.get("responseBytes"));
                    values.put("time", map.get("time"));
                    values.put("cpuUsage", map.get("cpuUsage"));
                    values.put("memUsage", map.get("memUsage"));

                    result.add(values);
                }
                if (result.size() > 0) {
                    return new AggregatedPageImpl<T>((List<T>) result, response.getScrollId());
                }
                return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
            }
        };

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("clusterIP",clusterIP));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").format("yyyy-MM-dd HH:mm:ss").gte(startTime).lte(endTime));
//        String[] includes = {"requestBytes","responseBytes","time","cpuUsage","memUsage"};
//        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
//                .withSourceFilter(fetchSourceFilter)
                .withPageable(PageRequest.of(0,10))
                .withSearchType(SearchType.DEFAULT)
                .build();
        Page<Map> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, Map.class,resourceConsumeResultMapper);
        String scrollId = ((ScrolledPage) scroll).getScrollId();
        while (scroll.hasContent()) {
            resultList.addAll(scroll.getContent());
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, Map.class,resourceConsumeResultMapper);
        }
        elasticsearchTemplate.clearScroll(scrollId);

        return resultList;
    }

    @Override
    public List<Map> getLoadMappingInstances(String namespace, String serviceName, String clusterIP, String startTime, String endTime) {
        //GET /service/serviceData/_search
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
        List<Map> resultList = new ArrayList<>();
        final SearchResultMapper loadMappingInstancesResultMapper = new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Map> result = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
                    }
                    Map map = searchHit.getSourceAsMap();
                    Map<String, Object> values = new HashMap<>();
                    values.put("requestConnections", map.get("requestConnections"));
                    values.put("podNums", map.get("podNums"));
                    values.put("time", map.get("time"));

                    result.add(values);
                }
                if (result.size() > 0) {
                    return new AggregatedPageImpl<T>((List<T>) result, response.getScrollId());
                }
                return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
            }
        };

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("clusterIP",clusterIP));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").format("yyyy-MM-dd HH:mm:ss").gte(startTime).lte(endTime));
//        String[] includes = {"time","requestConnections","podNums"};
//        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
//                .withSourceFilter(fetchSourceFilter)
                .withPageable(PageRequest.of(0,10))
                .withSearchType(SearchType.DEFAULT)
                .build();
        Page<Map> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, Map.class,loadMappingInstancesResultMapper);
        String scrollId = ((ScrolledPage) scroll).getScrollId();
        while (scroll.hasContent()) {
            resultList.addAll(scroll.getContent());
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, Map.class,loadMappingInstancesResultMapper);
        }
        elasticsearchTemplate.clearScroll(scrollId);

        return resultList;
    }

    @Override
    public Double getAvgResponseTime(String namespace, String serviceName, String clusterIP, String startTime, String endTime) {
    //        GET /service/serviceData/_search
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

    /**
     * 获取最近的时间
     *
     * @return
     */
    public String getRecentTime(){
        //GET /service/serviceData/_search
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
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
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
    public List<ServiceData> getNowServices() {
        //{
        //	"query":{
        //		"bool":{
        //			"must":[
        //				{"match_phrase" : {"time" : "2019-02-28 18:25:29"}}
        //			]
        //		}
        //	},
        //	"_source":["namespace","serviceName","podNums","cpuUsage","memUsage","onlineType"]
        //}
        List<ServiceData> resultList = new ArrayList<>();
//        final SearchResultMapper nowServiceResultMapper = new SearchResultMapper() {
//            @Override
//            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
//                List<Map> result = new ArrayList<>();
//                for (SearchHit searchHit : response.getHits()) {
//                    if (response.getHits().getHits().length <= 0) {
//                        return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
//                    }
//                    Map map = searchHit.getSourceAsMap();
//                    Map<String, Object> values = new HashMap<>();
//                    values.put("namespace", map.get("namespace"));
//                    values.put("serviceName", map.get("serviceName"));
//                    values.put("podNums", map.get("podNums"));
//                    values.put("cpuUsage", map.get("cpuUsage"));
//                    values.put("memUsage", map.get("memUsage"));
//                    String onlineType = (String) map.get("onlineType");
//                    if (onlineType.equals("1")) {
//                        values.put("isOffline", true);
//                    } else {
//                        values.put("isOffline", false);
//                    }
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
//        String[] includes = {"namespace","serviceName","podNums","cpuUsage","memUsage","onlineType"};
//        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSearchType(SearchType.DEFAULT)
//                .withSourceFilter(fetchSourceFilter)
                .withPageable(PageRequest.of(0,10))
                .build();

        Page<ServiceData> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, ServiceData.class);
        String scrollId = ((ScrolledPage) scroll).getScrollId();
        while (scroll.hasContent()) {
            resultList.addAll(scroll.getContent());
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, ServiceData.class);
        }
        elasticsearchTemplate.clearScroll(scrollId);

        return resultList;
    }

    @Override
    public Map<String, Object> getManagement(String namespace, String serviceName, String clusterMasterIP) {
//        {
//        	"query":{
//        		"bool":{
//        			"must":[
//        				{"match_phrase" : {"namespace" : "xxx"}},
//        				{"match_phrase" : {"serviceName" : "xxx"}}
//        			]
//        		}
//        	},
//        	"sort" : [
//        	{
//            	"time" : "desc"
//        	}
//    		],
//        	"_source":["resourceKind","resourceName","clusterMasterIP"]
//        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must().add(QueryBuilders.matchPhraseQuery("namespace",namespace));
        boolQueryBuilder.must().add(QueryBuilders.matchPhraseQuery("serviceName",serviceName));
        String[] includes = {"resourceKind","resourceName","clusterMasterIP"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SortBuilder sortBuilder = SortBuilders.fieldSort("time").order(SortOrder.DESC);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withSort(sortBuilder)
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
}
