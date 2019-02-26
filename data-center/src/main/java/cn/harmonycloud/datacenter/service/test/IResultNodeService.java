package cn.harmonycloud.datacenter.service.test;

import cn.harmonycloud.datacenter.entity.test.ResultNode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/1/26 0:44
*@Modify By:
*/

@Service
public interface IResultNodeService {
    public ResultNode saveOneResultNode(ResultNode resultNode);
    public List<ResultNode> saveAllResultNodes(List<ResultNode> resultNodes);

    public List<ResultNode> getAllResultNodes();
}
