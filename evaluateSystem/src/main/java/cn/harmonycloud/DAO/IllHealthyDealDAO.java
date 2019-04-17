package cn.harmonycloud.DAO;

public interface IllHealthyDealDAO {

    /**
     * @Author WANGYUZHONG
     * @Description //处理不健康的service
     * @Date 9:02 2019/4/15
     * @Param
     * @return
     **/
    boolean regulateIllHealthService(String masterIP, String namespace, String serviceName);


    /**
     * @Author WANGYUZHONG
     * @Description //处理不健康的Node
     * @Date 9:02 2019/4/15
     * @Param
     * @return
     **/
    boolean regulateIllHealthNode(String masterIP, String hostName);
}
