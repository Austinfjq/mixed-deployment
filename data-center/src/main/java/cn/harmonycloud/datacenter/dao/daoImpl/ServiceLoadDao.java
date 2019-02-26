package cn.harmonycloud.datacenter.dao.daoImpl;

import cn.harmonycloud.datacenter.dao.IServiceLoadDao;
import cn.harmonycloud.datacenter.entity.ServiceLoad;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.InternalMultiBucketAggregation;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.InternalDateRange;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.harmonycloud.datacenter.tools.Constant.SERVICE_INDEX;
import static cn.harmonycloud.datacenter.tools.Constant.SERVICE_TYPE;

@Repository(value = "serviceLoadDao")
public class ServiceLoadDao implements IServiceLoadDao {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<ServiceLoad> getServiceLoadForecastValues(String startTime, String endTime) {
        //{
        //	"aggs":{
        //		"group_by_time":{
        //			"range":{
        //				"field":"time",
        //				"ranges":[{
        //					"from":"2019-01-01 00:00:00",
        //					"to":"2019-02-01 00:00:00"
        //				}]
        //			},
        //			"aggs":{
        //				"group_by_name":{
        //					"terms":{
        //						"field":"namespace"
        //					},
        //					"aggs":{
        //						"group_by_service":{
        //							"terms":{
        //								"field":"serviceName"
        //							},
        //							"aggs":{
        //                    			"avg_reqConns":{
        //                        			"avg":{
        //										"field" : "requestConnections"
        //                        			}
        //                    			},
        //                    			"avg_netErrors":{
        //                        			"avg":{
        //										"field" : "netErrors"
        //                        			}
        //                    			},
        //                              "avg_timeResp":{
        //                                	"avg":{
        //        								"field" : "responseTime"
        //                                	}
        //                           	}
        //                			}
        //						}
        //					}
        //				}
        //			}
        //		}
        //	}
        //}

        List<ServiceLoad> serviceLoads = new ArrayList<>();
        DateRangeAggregationBuilder dateRangeAggregationBuilder = AggregationBuilders
                .dateRange("group_by_time")
                .field("time")
                .addRange(startTime,endTime);
        TermsAggregationBuilder namespaceTerms = AggregationBuilders.terms("group_by_name").field("namespace");
        TermsAggregationBuilder serviceNameTerms = AggregationBuilders.terms("group_by_service").field("serviceName");
        AvgAggregationBuilder avgRequestConnections = AggregationBuilders.avg("avg_reqConns").field("requestConnections");
        AvgAggregationBuilder avgNetErrors = AggregationBuilders.avg("avg_netErrors").field("netErrors");
        AvgAggregationBuilder avgTimeResp = AggregationBuilders.avg("avg_timeResp").field("responseTime");
        serviceNameTerms.subAggregation(avgNetErrors);
        serviceNameTerms.subAggregation(avgRequestConnections);
        serviceNameTerms.subAggregation(avgTimeResp);
        dateRangeAggregationBuilder.subAggregation(namespaceTerms.subAggregation(serviceNameTerms));

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(SERVICE_INDEX)
                .withTypes(SERVICE_TYPE)
                .withSearchType(SearchType.DEFAULT)
                .addAggregation(dateRangeAggregationBuilder)
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        InternalDateRange internalDateRange = aggregations.get("group_by_time");

        if(internalDateRange.getBuckets().size()>0){
            for (InternalDateRange.Bucket bk : internalDateRange.getBuckets()) {
                InternalTerms nameInternalTerms = (InternalTerms) bk.getAggregations().asMap().get("group_by_name");
                for(InternalTerms.Bucket bk1 : (List<InternalTerms.Bucket>)nameInternalTerms.getBuckets()){
                    InternalTerms serviceInternalTerms = (InternalTerms) bk1.getAggregations().asMap().get("group_by_service");
                    String namespace = bk1.getKeyAsString();
                    for(InternalTerms.Bucket bk2 : (List<InternalTerms.Bucket>)serviceInternalTerms.getBuckets()){
                        Map subaggmap = bk2.getAggregations().asMap();
                        double avg_reqConns = ((InternalAvg) subaggmap.get("avg_reqConns")).getValue();
                        double avg_netErrors = ((InternalAvg) subaggmap.get("avg_netErrors")).getValue();
                        double avg_timeResp = ((InternalAvg) subaggmap.get("avg_timeResp")).getValue();
                        String serviceName = bk2.getKeyAsString();
                        ServiceLoad serviceLoad = new ServiceLoad(namespace,serviceName,avg_timeResp,avg_netErrors,avg_reqConns,startTime,endTime);
                        serviceLoads.add(serviceLoad);
                    }
                }
            }
        }
        return serviceLoads;
    }
}
