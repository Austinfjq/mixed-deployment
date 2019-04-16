package cn.harmonycloud.service.serviceImp;

import cn.harmonycloud.beans.OnlineStrategy;
import cn.harmonycloud.beans.Service;
import cn.harmonycloud.dao.ServiceDAO;
import cn.harmonycloud.dao.imp.StrategyDaoImp;
import cn.harmonycloud.service.IOnlineRegulateControl;
import cn.harmonycloud.service.IStrategyProduce;
import cn.harmonycloud.tools.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @classname：OnlineRegulateControlServiceImp
 * @author：WANGYUZHONG
 * @date：2019/4/10 20:22
 * @description:TODO
 * @version:1.0
 **/
@org.springframework.stereotype.Service
public class OnlineRegulateControlServiceImp implements IOnlineRegulateControl {

    private final static Logger LOGGER = LoggerFactory.getLogger(OnlineRegulateControlServiceImp.class);


    @Autowired
    ServiceDAO serviceDAO;

    @Autowired
    IStrategyProduce iStrategyProduce;

    @Autowired
    StrategyDaoImp iStrategyDeal;

    @Override
    public boolean dealService(Service service) {
        int regulateNums = regulate(service);

        if (regulateNums == 0) {
            return true;
        } else {
            OnlineStrategy onlineStrategy = productOnlineStrategy(service, regulateNums);

            if (onlineStrategy == null) {
                LOGGER.error("product onlineStrategy failed!");
                return false;
            }
            return iStrategyDeal.dealOnlineStrategy(onlineStrategy);
        }
    }

    @Override
    public int regulate(Service service) {
        int servicePodNums = serviceDAO.getServicePodNums(service.getMasterIp(), service.getNamespace(), service.getServiceName());

        double lastPeriodMaxRequestNums = getLastPeriodMaxRequestNums(service.getMasterIp(), service.getNamespace(), service.getServiceName());

        double nextPeriodMaxRequestNums = getNextPeriodMaxRequestNums(service.getMasterIp(), service.getNamespace(), service.getServiceName());
        double maxRequestNums = getMaxRequestNums(lastPeriodMaxRequestNums, nextPeriodMaxRequestNums);

        int serviceRequestPodNums = serviceDAO.getServiceRequestPodNums(service.getMasterIp(), service.getNamespace(), service.getServiceName(), maxRequestNums);
        return servicePodNums-serviceRequestPodNums;
    }

    @Override
    public OnlineStrategy productOnlineStrategy(Service service, int regulateNums) {
        if (regulateNums == 0) {
            LOGGER.error("this service is not need regulate!");
        }else if (regulateNums < 0) {
            return iStrategyProduce.produceOnlineDilatationStrategy(service.getMasterIp(), service.getNamespace(), service.getServiceName(), Math.abs(regulateNums));
        }else {
            return iStrategyProduce.produceOnlineShrinkageStrategy(service.getMasterIp(), service.getNamespace(), service.getServiceName(), Math.abs(regulateNums));
        }
        return null;
    }

    double getMaxRequestNums(double lastPeriodMaxRequestNums, double nextPeriodMaxRequestNums) {
        return lastPeriodMaxRequestNums>=nextPeriodMaxRequestNums?lastPeriodMaxRequestNums:nextPeriodMaxRequestNums;
    }

    double getLastPeriodMaxRequestNums(String masterIp, String namespace, String serviceName) {
        String endTime = DateUtil.getCurrentTime();
        String startTime = DateUtil.getLastPeriodTime();

        return serviceDAO.getLastPeriodMaxRequestNums(masterIp,namespace,serviceName,startTime,endTime);
    }

    double getNextPeriodMaxRequestNums(String masterIp, String namespace, String serviceName) {
        String startTime = DateUtil.getCurrentTime();
        String endTime = DateUtil.getNextPeriodTime();

        return serviceDAO.getNextPeriodMaxRequestNums(masterIp, namespace, serviceName, startTime, endTime);
    }

}
