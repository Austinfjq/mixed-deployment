package cn.harmonycloud.service.serviceImp;

import cn.harmonycloud.beans.OfflineDilatationStrategy;
import cn.harmonycloud.beans.OfflineShrinkageStrategy;
import cn.harmonycloud.beans.OnlineStrategy;
import cn.harmonycloud.beans.SchedulableNode;
import cn.harmonycloud.dao.imp.NodeDaoImp;
import cn.harmonycloud.service.IStrategyProduce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @classname：StrategyProduceServiceImp
 * @author：WANGYUZHONG
 * @date：2019/4/10 17:09
 * @description:TODO
 * @version:1.0
 **/
@Service
public class StrategyProduceServiceImp implements IStrategyProduce {

    private final static Logger LOGGER = LoggerFactory.getLogger(StrategyProduceServiceImp.class);

    @Autowired
    NodeDaoImp nodeDAO;
    @Override
    public OnlineStrategy produceOnlineDilatationStrategy(String cluserIp, String namespace, String serviceName, int numbers) {
        OnlineStrategy onlineStrategy = new OnlineStrategy();
        onlineStrategy.setClusterMasterIP(cluserIp);
        onlineStrategy.setNamespace(namespace);
        onlineStrategy.setServiceName(serviceName);
        onlineStrategy.setNumber(numbers);
        onlineStrategy.setOperation(1);

        return onlineStrategy;
    }

    @Override
    public OnlineStrategy produceOnlineShrinkageStrategy(String cluserIp, String namespace, String serviceName, int numbers) {
        OnlineStrategy onlineStrategy = new OnlineStrategy();
        onlineStrategy.setClusterMasterIP(cluserIp);
        onlineStrategy.setNamespace(namespace);
        onlineStrategy.setServiceName(serviceName);
        onlineStrategy.setNumber(numbers);
        onlineStrategy.setOperation(2);

        return onlineStrategy;
    }

    @Override
    public List<OfflineDilatationStrategy> produceOfflineDilatationStrategy(String cluserIp, String namespace, String serviceName, String hostName, int numbers) {
        List<OfflineDilatationStrategy> offlineDilatationStrategies = new ArrayList<>();

        for (int i=0; i<numbers; i++) {
            OfflineDilatationStrategy offlineDilatationStrategy = new OfflineDilatationStrategy();
            List<SchedulableNode> schedulableNodes = new ArrayList<>();
            SchedulableNode schedulableNode = new SchedulableNode();
            schedulableNode.setNodeHostName(hostName);
            schedulableNode.setScore(10);

            schedulableNodes.add(schedulableNode);

            offlineDilatationStrategy.setMasterIp(cluserIp);
            offlineDilatationStrategy.setNamespace(namespace);
            offlineDilatationStrategy.setServiceName(serviceName);
            offlineDilatationStrategy.setSchedulableNodes(schedulableNodes);

            offlineDilatationStrategies.add(offlineDilatationStrategy);
        }

        return offlineDilatationStrategies;
    }

    @Override
    public List<OfflineShrinkageStrategy> produceOfflineShrinkageStrategy(String cluserIp, String namespace, String serviceName, String hostName, int numbers) {
        List<String> podList = nodeDAO.getPodNameListOfHost(cluserIp,namespace,serviceName,hostName);

        if (podList == null) {
            LOGGER.error("get pod list failed!");
            return  null;
        }
        if (podList.size() < numbers) {
            LOGGER.error("decreace node load failed,because the offline resource too small!");
        }

        List<OfflineShrinkageStrategy> offlineShrinkageStrategies = new ArrayList<>();
        int i=0;
        while (i<podList.size() && i<numbers) {
            OfflineShrinkageStrategy offlineShrinkageStrategy = new OfflineShrinkageStrategy();
            offlineShrinkageStrategy.setMasterIP(cluserIp);
            offlineShrinkageStrategy.setNamespace(namespace);
            offlineShrinkageStrategy.setServiceName(serviceName);
            offlineShrinkageStrategy.setPodName(podList.get(i));
            offlineShrinkageStrategies.add(offlineShrinkageStrategy);
            i++;
        }
        LOGGER.debug("product offlineShrinkageStrategies:"+offlineShrinkageStrategies.toString());
        return offlineShrinkageStrategies;
    }
}
