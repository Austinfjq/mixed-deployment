package cn.harmonycloud.datacenter.service.test.serviceImpl;

import cn.harmonycloud.datacenter.entity.test.ResultNode;
import cn.harmonycloud.datacenter.repository.test.ResultNodeRepository;
import cn.harmonycloud.datacenter.service.test.IResultNodeService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultNodeService implements IResultNodeService {
    @Autowired
    private ResultNodeRepository resultNodeRepository;

    @Override
    public ResultNode saveOneResultNode(ResultNode resultNode) {
        return resultNodeRepository.save(resultNode);
    }

    @Override
    public List<ResultNode> saveAllResultNodes(List<ResultNode> resultNodes) {
        return Lists.newArrayList(resultNodeRepository.saveAll(resultNodes));
    }

    @Override
    public List<ResultNode> getAllResultNodes() {
        return Lists.newArrayList(resultNodeRepository.findAll());
    }
}
