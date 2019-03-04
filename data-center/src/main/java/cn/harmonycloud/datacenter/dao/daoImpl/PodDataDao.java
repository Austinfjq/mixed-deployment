package cn.harmonycloud.datacenter.dao.daoImpl;

import cn.harmonycloud.datacenter.dao.IPodDataDao;
import cn.harmonycloud.datacenter.entity.es.NodeData;
import cn.harmonycloud.datacenter.entity.es.PodData;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
}
