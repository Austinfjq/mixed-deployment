package cn.harmonycloud.datacenter.dao.daoImpl;

import cn.harmonycloud.datacenter.dao.IForecastResultCellDao;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.InternalDateRange;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static cn.harmonycloud.datacenter.tools.Constant.*;

@Repository(value = "forecastResultCellDao")
public class ForecastResultCellDao implements IForecastResultCellDao {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public double getNextPeriodMaxRequestNums(String forecastResultCellID, String startTime, String endTime) {
        //{
        //	"query": {
        //		"bool": {
        //			"must": [{
        //				"match_phrase": {
        //					"forecastResultCellID": "xxx"
        //				}
        //			},
        //			{
        //				"match_phrase": {
        //					"forecastingIndex": "requestConnections"
        //				}
        //			}]
        //		}
        //	},
        //	"aggs": {
        //		"group_by_time": {
        //			"range": {
        //				"field": "time",
        //				"ranges": [{
        //					"from": "2019-04-09 17:00:00",
        //					"to": "2019-05-09 19:00:00"
        //				}]
        //			},
        //			"aggs": {
        //				"nextPeriodMaxRequestNums": {
        //					"max": {
        //						"field": "value"
        //					}
        //				}
        //			}
        //		}
        //	}
        //}
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("forecastResultCellID",forecastResultCellID))
                        .must(QueryBuilders.matchPhraseQuery("forecastingIndex","requestConnections"));

        MaxAggregationBuilder maxRequestConnections = AggregationBuilders.max("nextPeriodMaxRequestNums").field("value");
        DateRangeAggregationBuilder dateRangeAggregationBuilder = AggregationBuilders
                .dateRange("group_by_time")
                .field("time")
                .addRange(startTime, endTime);
        dateRangeAggregationBuilder.subAggregation(maxRequestConnections);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices(FORECAST_RESULT_CELL_INDEX)
                .withTypes(FORECAST_RESULT_CELL_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .addAggregation(dateRangeAggregationBuilder)
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        InternalDateRange internalDateRange = aggregations.get("group_by_time");
        if(internalDateRange.getBuckets().size()>0){
            double nextPeriodMaxRequestNums = 0.0;
            for (InternalDateRange.Bucket bk : internalDateRange.getBuckets()) {
                long count = bk.getDocCount();
                //得到所有子聚合
                Map subaggmap = bk.getAggregations().asMap();
                //获取指标的均值，并返回
                nextPeriodMaxRequestNums = ((InternalAvg) subaggmap.get("nextPeriodMaxRequestNums")).getValue();
            }
            return nextPeriodMaxRequestNums;
        }else{
            return 0.0;
        }
    }
}
