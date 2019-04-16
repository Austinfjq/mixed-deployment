package cn.harmonycloud.dao;


import cn.harmonycloud.beans.Service;

import java.util.List;

public interface ServiceDAO {

    boolean addService(Service service);

    List<Service> getAllOnlineService(String masterIp);

    /**
     * @Author WANGYUZHONG
     * @Description //获取某服务当前Pod实例数
     * @Date 15:03 2019/4/10
     * @Param
     * @return
     **/
    int getServicePodNums(String masterIp, String namespace, String serviceName);


    /**
     * @Author WANGYUZHONG
     * @Description //根据请求量，获取服务对应所需的Pod实例数
     * @Date 15:04 2019/4/10
     * @Param
     * @return
     **/
    int getServiceRequestPodNums(String masterIp, String namespace, String serviceName, double requestNums);


    /**
     * @Author WANGYUZHONG
     * @Description //获取某个服务过去一个调控周期的请求量最大值
     * @Date 15:08 2019/4/10
     * @Param
     * @return
     **/
    double getLastPeriodMaxRequestNums(String masterIp, String namespace, String serviceName, String startTime, String endTime);


    /**
     * @Author WANGYUZHONG
     * @Description //获取某个服务将来一个调控周期的请求量最大值
     * @Date 15:09 2019/4/10
     * @Param
     * @return
     **/
    double getNextPeriodMaxRequestNums(String masterIp, String namespace, String serviceName, String startTime, String endTime);

}
