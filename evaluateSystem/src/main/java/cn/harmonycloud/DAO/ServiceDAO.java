package cn.harmonycloud.DAO;

import cn.harmonycloud.beans.Service;

import java.util.List;

public interface ServiceDAO {

    List<Service> getAllOnlineService(String masterIp);

    /**
     * @Author WANGYUZHONG
     * @Description //获取节点的某个指标最新值
     * @Date 8:08 2019/4/15
     * @Param 查询语句
     * @return
     **/
    double getServiceIndexValue(String queryStr);

    double getServiceNormalResponseTime(String masterIp, String namespace,String serviceName);

    double getServiceNormalErrorRate(String masterIp, String namespace,String serviceName);
}
