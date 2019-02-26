package cn.harmonycloud.datacenter.dao.daoImpl;

import cn.harmonycloud.datacenter.dao.INodeLoadDao;
import cn.harmonycloud.datacenter.entity.NodeLoad;
import cn.harmonycloud.datacenter.entity.ServiceLoad;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.InternalDateRange;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.harmonycloud.datacenter.tools.Constant.*;

@Repository(value = "nodeLoadDao")
public class NodeLoadDao implements INodeLoadDao {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Override
    public List<NodeLoad> getNodeLoadForecastValues(String startTime, String endTime) {
//        {
//        	"aggs":{
//        		"group_by_time":{
//        			"range":{
//        				"field":"time",
//        				"ranges":[{
//        					"from":"2019-01-01 00:00:00",
//        					"to":"2019-02-01 00:00:00"
//        				}]
//        			},
//        			"aggs":{
//        				"group_by_nodeIP":{
//        					"terms":{
//        						"field":"nodeIP"
//        					},
//        					"aggs":{
//                            	"avg_cpuUsage":{
//                                	"avg":{
//        								"field" : "cpuUsage"
//                                	}
//                            	},
//                            	"avg_memUsage":{
//                                	"avg":{
//        								"field" : "memUsage"
//                                	}
//                            	},
//                             "avg_diskUsage":{
//                                  "avg":{
//                						 "field" : "diskUsage"
//                                   }
//                             }
//                          }
//        				}
//        			}
//        		}
//        	}
//        }

        List<NodeLoad> nodeLoads = new ArrayList<>();
        DateRangeAggregationBuilder dateRangeAggregationBuilder = AggregationBuilders
                .dateRange("group_by_time")
                .field("time")
                .addRange(startTime,endTime);
        TermsAggregationBuilder nodeIPTerms = AggregationBuilders.terms("group_by_nodeIP").field("nodeIP");
        AvgAggregationBuilder avgCpuUsage = AggregationBuilders.avg("avg_cpuUsage").field("cpuUsage");
        AvgAggregationBuilder avgMemUsage = AggregationBuilders.avg("avg_memUsage").field("memUsage");
        AvgAggregationBuilder avgDiskUsage = AggregationBuilders.avg("avg_diskUsage").field("diskUsage");
        nodeIPTerms.subAggregation(avgCpuUsage);
        nodeIPTerms.subAggregation(avgMemUsage);
        nodeIPTerms.subAggregation(avgDiskUsage);
        dateRangeAggregationBuilder.subAggregation(nodeIPTerms);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(NODE_INDEX)
                .withTypes(NODE_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .addAggregation(dateRangeAggregationBuilder)
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        InternalDateRange internalDateRange = aggregations.get("group_by_time");

        if(internalDateRange.getBuckets().size()>0){
            for (InternalDateRange.Bucket bk : internalDateRange.getBuckets()) {
                InternalTerms ipInternalTerms = (InternalTerms) bk.getAggregations().asMap().get("group_by_nodeIP");
                for (InternalTerms.Bucket bk1 : (List<InternalTerms.Bucket>) ipInternalTerms.getBuckets()) {
                    String nodeIP = bk1.getKeyAsString();
                    Map subaggmap = bk1.getAggregations().asMap();
                    double avg_cpuUsage = ((InternalAvg) subaggmap.get("avg_cpuUsage")).getValue();
                    double avg_memUsage = ((InternalAvg) subaggmap.get("avg_memUsage")).getValue();
                    double avg_diskUsage = ((InternalAvg) subaggmap.get("avg_diskUsage")).getValue();
                    NodeLoad nodeLoad = new NodeLoad(nodeIP,avg_cpuUsage,avg_memUsage,avg_diskUsage,startTime,endTime);
                    nodeLoads.add(nodeLoad);
                }
            }
        }
        return nodeLoads;
    }
}
