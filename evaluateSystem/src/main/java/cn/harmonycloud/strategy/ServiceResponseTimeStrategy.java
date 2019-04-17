package cn.harmonycloud.strategy;

import cn.harmonycloud.DAO.ServiceDAO;
import cn.harmonycloud.DAO.imp.ServiceDAOImp;
import cn.harmonycloud.beans.Service;

/**
 * @author wangyuzhong
 * @date 19-1-21 上午9:56
 * @Despriction
 */
public class ServiceResponseTimeStrategy {
    public boolean evaluate(Service service) {
        String queryStr = "sum(nginx_ingress_controller_response_duration_seconds_sum{method=POST, service=" + service.getServiceName()+",namespace="+ service.getNamespace()+"})by(service,namespace)";
        ServiceDAO serviceDAO = new ServiceDAOImp();

        double normalResponseTime = serviceDAO.getServiceNormalResponseTime(service.getMasterIp(), service.getNamespace(), service.getServiceName());
        double nowResponseTime = serviceDAO.getServiceIndexValue(queryStr);
        if (nowResponseTime > normalResponseTime) {
            return false;
        }
        return true;
    }
}
