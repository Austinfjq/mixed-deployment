package cn.harmonycloud.service;

import cn.harmonycloud.beans.ForecastCell;
import cn.harmonycloud.beans.Node;
import cn.harmonycloud.beans.Service;

import java.util.List;

public interface IData {

    /**
     * @Author WANGYUZHONG
     * @Description //获取某个集群中所有的工作节点
     * @Date 10:08 2019/4/11
     * @Param
     * @return
     **/
    List<Node> getNodeList(String clusterIp);

    /**
     * @Author WANGYUZHONG
     * @Description //获取某个集群中所有正在运行的service
     * @Date 15:34 2019/4/16
     * @Param
     * @return
     **/
    List<Service> getAllOnlineService(String masterIp);


    /**
     * @Author WANGYUZHONG
     * @Description //将预测结果持久化到es中
     * @Date 15:48 2019/4/16
     * @Param
     * @return
     **/
    boolean saveForecastData(String forecastResult);


    /**
     * @Author WANGYUZHONG
     * @Description //获取某个指标的历史数据
     * @Date 15:55 2019/4/16
     * @Param
     * @return
     **/
    String getIndexHistoryData(String ID, int type, String index, String startTime, String endTime);


    /**
     * @Author WANGYUZHONG
     * @Description //获取某个预测单元
     * @Date 15:57 2019/4/16
     * @Param
     * @return
     **/
    ForecastCell getForecastCell(String ID, int type, String index);


    /**
     * @Author WANGYUZHONG
     * @Description //持久化某个指标的预测模型以及参数
     * @Date 15:58 2019/4/16
     * @Param
     * @return
     **/
    boolean savaForecastModel(String ID, int type, String index, String forecastingModel, String modelParams);



    /**
     * @Author WANGYUZHONG
     * @Description //更新预测到那个时间点
     * @Date 16:03 2019/4/16
     * @Param
     * @return
     **/
    boolean updateIndexLastForecastTime(String ID, int type, String index, String lastForecastTime);
}
