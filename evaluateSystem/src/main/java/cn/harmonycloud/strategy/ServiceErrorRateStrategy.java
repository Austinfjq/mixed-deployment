package cn.harmonycloud.strategy;

import cn.harmonycloud.DAO.ServiceDAO;
import cn.harmonycloud.DAO.imp.ServiceDAOImp;
import cn.harmonycloud.beans.Service;

/**
 * @author wangyuzhong
 * @date 19-1-21 上午9:57
 * @Despriction
 */
public class ServiceErrorRateStrategy{
    public boolean evaluate(Service service) {
        String queryStr = "sum(container_network_transmit_errors_total{method=POST, service=" + service.getServiceName()+",namespace="+ service.getNamespace()+"})by(service,namespace)";
        ServiceDAO serviceDAO = new ServiceDAOImp();

        double normalErrorRate = serviceDAO.getServiceNormalErrorRate(service.getMasterIp(),service.getNamespace(), service.getServiceName());
        double nowErrorRate = serviceDAO.getServiceIndexValue(queryStr);
        if (normalErrorRate < nowErrorRate) {
            return false;
        }

        return true;
    }
}
