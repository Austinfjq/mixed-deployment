package cn.harmonycloud.datacenter.service.test;

import cn.harmonycloud.datacenter.entity.test.ResultNode;
import cn.harmonycloud.datacenter.entity.test.ResultPod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/1/26 0:43
*@Modify By:
*/
@Service
public interface IResultPodService {
    public ResultPod saveOneResultPod(ResultPod resultPod);
    public List<ResultPod> saveAllResultPods(List<ResultPod> resultPods);

    public List<ResultPod> getAllResultPods();
}
