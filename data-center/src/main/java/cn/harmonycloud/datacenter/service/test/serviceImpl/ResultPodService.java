package cn.harmonycloud.datacenter.service.test.serviceImpl;

import cn.harmonycloud.datacenter.entity.test.ResultPod;
import cn.harmonycloud.datacenter.repository.test.ResultPodRepository;
import cn.harmonycloud.datacenter.service.test.IResultPodService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultPodService implements IResultPodService {

    @Autowired
    private ResultPodRepository resultPodRepository;

    @Override
    public ResultPod saveOneResultPod(ResultPod resultPod) {
        return resultPodRepository.save(resultPod);
    }

    @Override
    public List<ResultPod> saveAllResultPods(List<ResultPod> resultPods) {
        return Lists.newArrayList(resultPodRepository.saveAll(resultPods));
    }

    @Override
    public List<ResultPod> getAllResultPods() {
        return Lists.newArrayList(resultPodRepository.findAll(PageRequest.of(0,Integer.MAX_VALUE)));
    }
}
