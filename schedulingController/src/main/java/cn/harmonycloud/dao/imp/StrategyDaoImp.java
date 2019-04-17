package cn.harmonycloud.dao.imp;

import cn.harmonycloud.beans.OfflineDilatationStrategy;
import cn.harmonycloud.beans.OfflineShrinkageStrategy;
import cn.harmonycloud.beans.OnlineStrategy;
import cn.harmonycloud.dao.StrategyDAO;
import cn.harmonycloud.tools.DataUtil;
import cn.harmonycloud.tools.HttpClientResult;
import cn.harmonycloud.tools.HttpClientUtils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @classname：StrategyDaoImp
 * @author：WANGYUZHONG
 * @date：2019/4/11 11:28
 * @description:TODO
 * @version:1.0
 **/

@Service
public class StrategyDaoImp implements StrategyDAO {

    private final static Logger LOGGER = LoggerFactory.getLogger(StrategyDaoImp.class);

    @Value("${ScheduleAlgorithmHostIP}")
    private String scheduleAlgorithmHostIP;

    @Value("${ScheduleAlgorithmPort}")
    private String scheduleAlgorithmPort;

    @Value("${ScheduleExecutorHostIP}")
    private String scheduleExecutorHostIP;

    @Value("${ScheduleExecutorPort}")
    private String scheduleExecutorPort;

    @Override
    public boolean dealOnlineStrategy(OnlineStrategy onlineStrategy) {
        LOGGER.debug("onlineStrategy is:"+ DataUtil.objectToJson(onlineStrategy));
        String url = "http://"+ scheduleAlgorithmHostIP + ":" + scheduleAlgorithmPort + "/schedulepod";
        String onlineStrategyStr = "["+DataUtil.objectToJson(onlineStrategy)+"]";
        String result = null;
        try {
            result =  HttpClientUtils.sendPost(url,onlineStrategyStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == result) {
            LOGGER.error("onlineStrategy deal failed!");
            return false;
        }
        LOGGER.debug(result);

        JSONObject jsonObject = DataUtil.jsonStringtoObject(result);
        Boolean isSucceed = jsonObject.getBoolean("isSucceed");
        return isSucceed;
    }

    @Override
    public boolean dealOfflineDilatationStrategy(OfflineDilatationStrategy offlineDilatationStrategy) {
        Map<String,String> params = new HashMap<>();
        params.put("offlineDilatationStrategy", DataUtil.objectToJson(offlineDilatationStrategy));
        String url = "http://"+ scheduleExecutorHostIP + ":" + scheduleExecutorPort + "/dispatching/createPod";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doPost(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("offlineDilatationStrategy deal failed!");
            return false;
        }
        return Boolean.valueOf(httpClientResult.getContent());
    }

    @Override
    public boolean dealOfflineShrinkageStrategy(OfflineShrinkageStrategy offlineShrinkageStrategy) {
        Map<String,String> params = new HashMap<>();
        params.put("offlineShrinkageStrategy", DataUtil.objectToJson(offlineShrinkageStrategy));
        String url = "http://"+ scheduleExecutorHostIP + ":" + scheduleExecutorPort + "/dispatching/deletePod";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doPost(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("offlineShrinkageStrategy deal failed!");
            return false;
        }
        return Boolean.valueOf(httpClientResult.getContent());
    }

    public static void main(String[] args) {
        OnlineStrategy onlineStrategy = new OnlineStrategy();
        onlineStrategy.setClusterMasterIP("https://10.10.102.25:6443/");
        onlineStrategy.setNamespace("wordpress");
        onlineStrategy.setServiceName("wordpress-wp");
        onlineStrategy.setOperation(1);
        onlineStrategy.setNumber(3);

        StrategyDaoImp strategyDaoImp = new StrategyDaoImp();
        System.out.println(String.valueOf(strategyDaoImp.dealOnlineStrategy(onlineStrategy)));

    }
}
