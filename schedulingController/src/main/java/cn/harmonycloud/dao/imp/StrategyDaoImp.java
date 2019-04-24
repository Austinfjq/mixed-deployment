package cn.harmonycloud.dao.imp;

import cn.harmonycloud.beans.OfflineDilatationStrategy;
import cn.harmonycloud.beans.OfflineShrinkageStrategy;
import cn.harmonycloud.beans.OnlineStrategy;
import cn.harmonycloud.beans.SchedulableNode;
import cn.harmonycloud.dao.StrategyDAO;
import cn.harmonycloud.tools.DataUtil;
import cn.harmonycloud.tools.HttpClientUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        LOGGER.info("start scheduleAlgoruthm:  onlineStrategy=" + DataUtil.objectToJson(onlineStrategy));
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
        LOGGER.info("start offlineDilatationStrategy scheduleExecute:  offlineDilatationStrategy=" + DataUtil.objectToJson(offlineDilatationStrategy));

        List<NameValuePair> paramList = new ArrayList<>();
        String url = "http://"+ scheduleExecutorHostIP + ":" + scheduleExecutorPort + "/dispatching/createPod";

        paramList.add(new BasicNameValuePair("namespace", offlineDilatationStrategy.getNamespace()));
        paramList.add(new BasicNameValuePair("servicename", offlineDilatationStrategy.getServiceName()));
        paramList.add(new BasicNameValuePair("masterIp", offlineDilatationStrategy.getMasterIp()));

        JSONArray jsonArray = new JSONArray();
        for (SchedulableNode schedulableNode:offlineDilatationStrategy.getSchedulableNodes()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("hostname", schedulableNode.getNodeHostName());
            jsonObject.put("score", schedulableNode.getScore());
            jsonArray.add(jsonObject);
        }
        paramList.add(new BasicNameValuePair("nodeList", jsonArray.toString()));

        String result = HttpClientUtils.get(url, paramList);
        LOGGER.info("scheduleExecute result: " + result);
        if (result == null || !result.equals("true")) {
            LOGGER.error("offlineDilatationStrategy deal failed!");
            return false;
        }
        return Boolean.valueOf(result);
    }

    @Override
    public boolean dealOfflineShrinkageStrategy(OfflineShrinkageStrategy offlineShrinkageStrategy) {
        LOGGER.info("start offlineShrinkageStrategy scheduleExecute:  offlineShrinkageStrategy=" + DataUtil.objectToJson(offlineShrinkageStrategy));

        List<NameValuePair> paramList = new ArrayList<>();
        String url = "http://"+ scheduleExecutorHostIP + ":" + scheduleExecutorPort + "/dispatching/deletePod";

        paramList.add(new BasicNameValuePair("namespace", offlineShrinkageStrategy.getNamespace()));
        paramList.add(new BasicNameValuePair("servicename", offlineShrinkageStrategy.getServiceName()));
        paramList.add(new BasicNameValuePair("masterIp", offlineShrinkageStrategy.getMasterIP()));
        paramList.add(new BasicNameValuePair("podName", offlineShrinkageStrategy.getPodName()));

        String result = HttpClientUtils.get(url, paramList);
        LOGGER.info("scheduleExecute result: " + result);
        if (result == null || !result.equals("true")) {
            LOGGER.error("offlineShrinkageStrategy deal failed!");
            return false;
        }
        return Boolean.valueOf(result);
    }
}
