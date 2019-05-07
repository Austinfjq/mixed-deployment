package cn.harmonycloud.datacenter.dao.daoImpl;

import cn.harmonycloud.datacenter.dao.IPodDataDao;
import cn.harmonycloud.datacenter.entity.es.PodData;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
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
*@Date: Created in 2019/3/2 20:24
*@Modify By:
*/
@Repository(value = "podDataDao")
public class PodDataDao implements IPodDataDao {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    
    @Override
    public List<PodData> findAllPodDatas() {
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
        List<PodData> resultList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(POD_INDEX)
                .withTypes(POD_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .withPageable(PageRequest.of(0,100))
                .build();

        Page<PodData> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, PodData.class);
        String scrollId = ((ScrolledPage) scroll).getScrollId();
        while (scroll.hasContent()) {
            resultList.addAll(scroll.getContent());
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, PodData.class);
        }
        elasticsearchTemplate.clearScroll(scrollId);

        return resultList;
    }

    @Override
    public List<PodData> getNowPods() {
//        {
//        	"query":{
//        		"bool":{
//        			"must":[
//        				{"match_phrase" : {"time" : "2019-02-28 18:25:29"}}
//        			]
//        		}
//        	}
//        }
        List<PodData> resultList = new ArrayList<>();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("time",getRecentTime()));
//        String[] includes = {"namespace","serviceName","podNums","cpuUsage","memUsage","onlineType"};
//        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(POD_INDEX)
                .withTypes(POD_TYPE)
                .withSearchType(SearchType.DEFAULT)
//                .withSourceFilter(fetchSourceFilter)
                .withPageable(PageRequest.of(0,10))
                .build();

        Page<PodData> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, PodData.class);
        String scrollId = ((ScrolledPage) scroll).getScrollId();
        while (scroll.hasContent()) {
            resultList.addAll(scroll.getContent());
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, PodData.class);
        }
        elasticsearchTemplate.clearScroll(scrollId);

        return resultList;
    }

    @Override
    public List<Map> getPodNamesByNodeAndService(String clusterMasterIP, String namespace, String serviceName, String nodeName) {
        //{
        //	"query": {
        //		"bool": {
        //			"must": [{
        //				"match_phrase": {
        //					"time": "2019-04-30 14:23:41"
        //				}
        //			},
        //			{
        //				"match_phrase": {
        //					"clusterMasterIP": "10.10.102.25"
        //				}
        //			},
        //			{
        //				"match_phrase": {
        //					"namespace": "wy"
        //				}
        //			},
        //			{
        //				"match_phrase": {
        //					"serviceName": "nginx"
        //				}
        //			},
        //			{
        //				"match_phrase": {
        //					"nodeName": "10.10.103.27-slave"
        //				}
        //			}]
        //		}
        //	}
        //}
        List<Map> resultList = new ArrayList<>();

        final SearchResultMapper pairResultMapper = new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Map> result = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
                    }
                    Map map = searchHit.getSourceAsMap();
                    Map<String, String> values = new HashMap<>();
                    values.put("podName", (String) map.get("podName"));
                    result.add(values);
                }
                if (result.size() > 0) {
                    return new AggregatedPageImpl<T>((List<T>) result, response.getScrollId());
                }
                return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
            }
        };

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("time",getRecentTime()))
                        .must(QueryBuilders.matchPhraseQuery("clusterMasterIP",clusterMasterIP))
                        .must(QueryBuilders.matchPhraseQuery("namespace",namespace))
                        .must(QueryBuilders.matchPhraseQuery("serviceName",serviceName))
                        .must(QueryBuilders.matchPhraseQuery("nodeName",nodeName));

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(POD_INDEX)
                .withTypes(POD_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .withPageable(PageRequest.of(0,10))
                .build();

        Page<Map> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, Map.class, pairResultMapper);
        String scrollId = ((ScrolledPage) scroll).getScrollId();
        while (scroll.hasContent()) {
            resultList.addAll(scroll.getContent());
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, Map.class,pairResultMapper);
        }
        elasticsearchTemplate.clearScroll(scrollId);

        return resultList;
    }

    @Override
    public List<Map> getNodeByClusterMasterIP(String clusterMasterIP) {
        //{
        //	"query": {
        //		"bool": {
        //			"must": [{
        //				"match_phrase": {
        //					"time": "2019-04-30 14:23:41"
        //				}
        //			},
        //			{
        //				"match_phrase": {
        //					"clusterMasterIP": "10.10.102.25"
        //				}
        //			}]
        //		}
        //	},
        //	"_source":["nodeName"]
        //}
        List<Map> resultList = new ArrayList<>();
        final SearchResultMapper pairResultMapper = new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Map> result = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
                    }
                    Map map = searchHit.getSourceAsMap();
                    Map<String, String> values = new HashMap<>();
                    values.put("hostName", (String) map.get("nodeName"));
                    result.add(values);
                }
                if (result.size() > 0) {
                    return new AggregatedPageImpl<T>((List<T>) result, response.getScrollId());
                }
                return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
            }
        };

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("time",getRecentTime()))
                .must(QueryBuilders.matchPhraseQuery("clusterMasterIP",clusterMasterIP));
        String[] includes = {"nodeName"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(includes,null);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(POD_INDEX)
                .withTypes(POD_TYPE)
                .withSourceFilter(fetchSourceFilter)
                .withSearchType(SearchType.DEFAULT)
                .withPageable(PageRequest.of(0,10))
                .build();

        Page<Map> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, Map.class,pairResultMapper);
        String scrollId = ((ScrolledPage) scroll).getScrollId();
        Map<String,Boolean> hash = new HashMap<>();
        while (scroll.hasContent()) {
            List<Map> tmp = scroll.getContent();
            for(Map map : tmp){
                if(!hash.containsKey(map.get("hostName"))){
                    resultList.add(map);
                    hash.put((String) map.get("hostName"),true);
                }
            }
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, Map.class, pairResultMapper);
        }
        elasticsearchTemplate.clearScroll(scrollId);

        return resultList;
    }

    /**
     * 获取最近的时间
     *
     * @return
     */
    public String getRecentTime(){
        //GET /pod/podData/_search
//        {
//        	"aggs":{
//        		"recent_time":{
//        			"max":{
//        				"field":"time"
//        			}
//        		}
//        	}
//        }
        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("recent_time").field("time");

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(POD_INDEX)
                .withTypes(POD_TYPE)
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

}
