package cn.harmonycloud.service.serviceImp;

import cn.harmonycloud.beans.OnlineStrategy;
import cn.harmonycloud.beans.Service;
import cn.harmonycloud.dao.ServiceDAO;
import cn.harmonycloud.dao.imp.StrategyDaoImp;
import cn.harmonycloud.service.IOnlineRegulateControl;
import cn.harmonycloud.service.IStrategyProduce;
import cn.harmonycloud.tools.DateUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

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

    @Value("${SchedulePeriod}")
    private int schedulePeriod;

    @Autowired
    ServiceDAO serviceDAO;

    @Autowired
    IStrategyProduce iStrategyProduce;

    @Autowired
    StrategyDaoImp iStrategyDeal;

    @Value("${ClusterIp}")
    private String clusterIp;

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
        int servicePodNums = getPodNumbers(service.getMasterIp(), service.getNamespace(), service.getServiceName());

        if (servicePodNums < 0) {
            LOGGER.error("the " + service.toString() + " pod number is invaild!");
            return 0;
        }

        double lastPeriodMaxRequestNums = getLastPeriodMaxRequestNums(service.getMasterIp(), service.getNamespace(), service.getServiceName());

        double nextPeriodMaxRequestNums = getNextPeriodMaxRequestNums(service.getMasterIp(), service.getNamespace(), service.getServiceName());
        double maxRequestNums = getMaxRequestNums(lastPeriodMaxRequestNums, nextPeriodMaxRequestNums);

        int serviceRequestPodNums = serviceDAO.getServiceRequestPodNums(service.getMasterIp(), service.getNamespace(), service.getServiceName(), maxRequestNums);
        return servicePodNums - serviceRequestPodNums;
    }

    @Override
    public OnlineStrategy productOnlineStrategy(Service service, int regulateNums) {
        if (regulateNums == 0) {
            LOGGER.error("this service is not need regulate!");
        } else if (regulateNums < 0) {
            return iStrategyProduce.produceOnlineDilatationStrategy(service.getMasterIp(), service.getNamespace(), service.getServiceName(), Math.abs(regulateNums));
        } else {
            return iStrategyProduce.produceOnlineShrinkageStrategy(service.getMasterIp(), service.getNamespace(), service.getServiceName(), Math.abs(regulateNums));
        }
        return null;
    }

    double getMaxRequestNums(double lastPeriodMaxRequestNums, double nextPeriodMaxRequestNums) {
        return lastPeriodMaxRequestNums >= nextPeriodMaxRequestNums ? lastPeriodMaxRequestNums : nextPeriodMaxRequestNums;
    }

    double getLastPeriodMaxRequestNums(String masterIp, String namespace, String serviceName) {
        DateUtil dateUtil = new DateUtil();
        String endTime = dateUtil.getCurrentTime();
        String startTime = dateUtil.getLastPeriodTime(schedulePeriod);

        return serviceDAO.getLastPeriodMaxRequestNums(masterIp, namespace, serviceName, startTime, endTime);
    }

    double getNextPeriodMaxRequestNums(String masterIp, String namespace, String serviceName) {
        DateUtil dateUtil = new DateUtil();
        String startTime = dateUtil.getCurrentTime();
        String endTime = dateUtil.getNextPeriodTime(schedulePeriod);

        return serviceDAO.getNextPeriodMaxRequestNums(masterIp, namespace, serviceName, startTime, endTime);
    }


    public int getPodNumbers(String masterIp, String namespace, String serviceName) {
        JSONObject jsonObject = serviceDAO.getOwner(masterIp, namespace, serviceName);

        if (jsonObject == null || jsonObject.size() ==0) {
            LOGGER.error("the " + masterIp + " :" + namespace + " :" + serviceName + " not have owner!");
        }

        String ownerType = jsonObject.getString("resourceKind");

        String ownerName = jsonObject.getString("resourceName");

        int result = -1;
        switch (ownerType.toLowerCase()) {
            case "replicaset":
                result = serviceDAO.getReplicasOfReplicaset(masterIp, namespace, ownerName);
                break;
            case "deployment":
                result = serviceDAO.getReplicasOfDeployment(masterIp, namespace, ownerName);
                break;
            case "statefulset":
                result = serviceDAO.getReplicasOfStatefulSet(masterIp, namespace, ownerName);
                break;
            default:
                LOGGER.info("OwnType is unknown!");
        }

        if (result < 0) {
            LOGGER.error("the " + clusterIp + "：" + namespace + ": " + ownerName + " replicas is invaild!");
            return  -1;
        }

        return result;
    }

    @Override
    public void process() {
        List<Service> services = serviceDAO.getAllOnlineService(clusterIp);
        if (services == null) {
            LOGGER.error("get all online service failed!");
            return;
        }
        if (services.size() == 0) {
            LOGGER.error("there is no any online service!");
            return;
        }

        for (Service service : services) {
            dealService(service);
        }
    }
}
