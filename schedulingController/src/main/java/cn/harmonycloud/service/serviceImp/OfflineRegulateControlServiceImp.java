package cn.harmonycloud.service.serviceImp;

import cn.harmonycloud.beans.Node;
import cn.harmonycloud.beans.OfflineDilatationStrategy;
import cn.harmonycloud.beans.OfflineShrinkageStrategy;
import cn.harmonycloud.dao.NodeDAO;
import cn.harmonycloud.dao.imp.StrategyDaoImp;
import cn.harmonycloud.service.IOfflineRegulateControl;
import cn.harmonycloud.tools.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname：OfflineRegulateControlServiceImp
 * @author：WANGYUZHONG
 * @date：2019/4/11 9:50
 * @description:TODO
 * @version:1.0
 **/

@Service
public class OfflineRegulateControlServiceImp implements IOfflineRegulateControl {

    private final static Logger LOGGER = LoggerFactory.getLogger(OfflineRegulateControlServiceImp.class);

    @Value("${CpuUsageMaxThreshold}")
    private double cpuUsageMaxThreshold;

    @Value("${CpuUsageMinThreshold}")
    private double cpuUsageMinThreshold;

    @Value("${CpuUsageNormalThreshold}")
    private double cpuUsageNormalThreshold;

    @Value("${MemUsageMaxThreshold}")
    private double memUsageMaxThreshold;

    @Value("${MemUsageMinThreshold}")
    private double memUsageMinThreshold;

    @Value("${MemUsageNormalThreshold}")
    private double memUsageNormalThreshold;

    @Value("${NodeManagerCpuQuota}")
    private int nodeManagerCpuQuota;

    @Value("${NodeManagerMemQuota}")
    private int nodeManagerMemQuota;

    @Value("${NodeManagerServiceName}")
    private String nodeManagerServiceName;

    @Value("${HadoopNamespace}")
    private String hadoopNamespace;

    @Value("${ClusterIp}")
    private String clusterIp;

    @Autowired
    private NodeDAO nodeDao;

    @Autowired
    StrategyProduceServiceImp strategyProduceServiceImp;

    @Autowired
    StrategyDaoImp strategyDealServiceImp;

    @Override
    public boolean dealNode(Node node) {
        int regulateNums = regulate(node);

        if (regulateNums == 0) {
            LOGGER.info("this node load is narmal!");
            return true;
        } else if (regulateNums > 0) {
            LOGGER.info("this node load is need increace!");
            List<OfflineDilatationStrategy> offlineDilatationStrategies = productOfflineDilatationStrategy(node, regulateNums);

            if (offlineDilatationStrategies == null) {
                LOGGER.error("offlineDilatationStrategies product failed!");
                return false;
            }

            if (offlineDilatationStrategies.size() == 0) {
                LOGGER.info("there is no any offlineDilatationStrategies!");
                return true;
            }

            for (OfflineDilatationStrategy offlineDilatationStrategy : offlineDilatationStrategies) {
                boolean isDealSucceed = strategyDealServiceImp.dealOfflineDilatationStrategy(offlineDilatationStrategy);
                if (!isDealSucceed) {
                    return false;
                }
            }

            return true;
        } else {
            LOGGER.info("this node load is need decreace!");
            List<OfflineShrinkageStrategy> offlineShrinkageStrategies = productOfflineShrinkageStrategy(node, Math.abs(regulateNums));

            if (offlineShrinkageStrategies == null) {
                LOGGER.error("offlineShrinkageStrategies product failed!");
                return false;
            }

            if (offlineShrinkageStrategies.size() == 0) {
                LOGGER.info("there is no any offlineShrinkageStrategies!");
                return true;
            }

            for (OfflineShrinkageStrategy offlineShrinkageStrategy : offlineShrinkageStrategies) {
                boolean isDealSucceed = strategyDealServiceImp.dealOfflineShrinkageStrategy(offlineShrinkageStrategy);
                if (!isDealSucceed) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int regulate(Node node) {
        double maxCpuUsage = getLastPeriodMaxCpuUsage(node.getMasterIp(), node.getHostName());
        double maxMemUsage = getLastPeriodMaxMemUsage(node.getMasterIp(), node.getHostName());

        double cpuTotal = nodeDao.getNodeCpuTotal(node.getMasterIp(), node.getHostName());
        double memTotal = nodeDao.getNodeMemTotal(node.getMasterIp(), node.getHostName());

        if (maxCpuUsage < cpuUsageMinThreshold && maxMemUsage < memUsageMinThreshold) {
            return calculateDilatationNums(cpuTotal, memTotal, maxCpuUsage, maxMemUsage);
        } else if (maxCpuUsage > cpuUsageMaxThreshold || maxMemUsage > memUsageMaxThreshold) {
            return -calculateShrinkageNums(cpuTotal, memTotal, maxCpuUsage, maxMemUsage);
        } else {
            return 0;
        }
    }

    @Override
    public List<OfflineShrinkageStrategy> productOfflineShrinkageStrategy(Node node, int regulateNums) {
        if (regulateNums == 0) {
            LOGGER.error("this service is not need regulate!");
        }

        return strategyProduceServiceImp.produceOfflineShrinkageStrategy(node.getMasterIp(), hadoopNamespace, nodeManagerServiceName, node.getHostName(), regulateNums);
    }

    @Override
    public List<OfflineDilatationStrategy> productOfflineDilatationStrategy(Node node, int regulateNums) {
        if (regulateNums == 0) {
            LOGGER.error("this service is not need regulate!");
        }

        return strategyProduceServiceImp.produceOfflineDilatationStrategy(node.getMasterIp(), hadoopNamespace, nodeManagerServiceName, node.getHostName(), regulateNums);
    }

    @Override
    public void process() {
        //NodeDAO nodeDAO = new NodeDaoImp();
        List<Node> nodes = nodeDao.getNodeList(clusterIp);

        if (nodes == null) {
            LOGGER.error("get all node failed!");
            return;
        }

        if (nodes.size() == 0) {
            LOGGER.error("there is no any node!");
            return;
        }

        for (Node node : nodes) {
            dealNode(node);
        }
    }

    double getLastPeriodMaxCpuUsage(String masterIp, String hostName) {
        String endTime = DateUtil.getCurrentTime();
        String startTime = DateUtil.getLastPeriodTime();

        return nodeDao.getLastPeriodMaxCpuUsage(masterIp, hostName, startTime, endTime);
    }

    double getLastPeriodMaxMemUsage(String masterIp, String hostName) {
        String endTime = DateUtil.getCurrentTime();
        String startTime = DateUtil.getLastPeriodTime();

        return nodeDao.getLastPeriodMaxMemUsage(masterIp, hostName, startTime, endTime);
    }

    public int calculateDilatationNums(double cpuTotal, double memTotal, double maxCpuUsage, double maxMemUsage) {
        int nums = 1;
        while (true) {
            double nowCpuUsage = (cpuTotal * maxCpuUsage + nums * nodeManagerCpuQuota) / cpuTotal;
            double nowMemUsage = (memTotal * maxMemUsage + nums * nodeManagerMemQuota) / memTotal;

            if (nowCpuUsage > cpuUsageNormalThreshold || nowMemUsage > memUsageNormalThreshold) {
                break;
            }

            nums++;
        }
        return nums;
    }

    public int calculateShrinkageNums(double cpuTotal, double memTotal, double maxCpuUsage, double maxMemUsage) {
        int nums = 1;
        while (true) {
            double nowCpuUsage = (cpuTotal * maxCpuUsage - nums * nodeManagerCpuQuota) / cpuTotal;
            double nowMemUsage = (memTotal * maxMemUsage - nums * nodeManagerMemQuota) / memTotal;

            if (nowCpuUsage < cpuUsageMaxThreshold && nowMemUsage < memUsageMaxThreshold) {
                break;
            }

            nums++;
        }
        return nums;
    }
}
