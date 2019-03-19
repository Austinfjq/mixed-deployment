package cn.harmonycloud;

import cn.harmonycloud.beans.LoadConfigFile;
import cn.harmonycloud.dao.NodeDAO;
import cn.harmonycloud.dao.ServiceDAO;
import cn.harmonycloud.entry.*;
import cn.harmonycloud.tools.Constant;
import cn.harmonycloud.tools.Write2ES;
import com.alibaba.fastjson.JSON;
import cn.harmonycloud.tools.HttpSend;

import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;

public class StrategyExecutor {
    private final static Logger LOGGER = LoggerFactory.getLogger(StrategyExecutor.class);
    public static ArrayList<Result> results = new ArrayList<>();

    public static ArrayList<Result> getResults(int onlineNum, int offlineNum) {

        List<ForecastService> forecastServiceList = ServiceDAO.getForecastServiceList();
        List<NowService> nowServiceList = ServiceDAO.getNowServiceList();

        List<ForecastNode> forecastNodeList = NodeDAO.getForecastNodeList();
        List<NowNode> nowNodeList = NodeDAO.getNowNodeList();

        Collections.sort(nowServiceList, new Comparator<NowService>() {
            @Override
            public int compare(NowService o1, NowService o2) {
//                return (String.valueOf(o1.getCpuUsage()).compareTo( String.valueOf(o2.getCpuUsage()) ));
                return (o1.getCpuUsage().compareTo(o2.getCpuUsage()));
            }
        });

        //执行service分析的线程

        StrategyTaskThread strategyTaskThread = new StrategyTaskThread(forecastNodeList, nowNodeList,
                forecastServiceList, nowServiceList, results, onlineNum, offlineNum);
        Thread strategyThread = new Thread(strategyTaskThread);
        strategyThread.start();

        return results;
    }


    public static void run(int onlineNum, int offlineNum) {

        Write2ES.run(getResults(onlineNum, offlineNum), "schedulePods");
        String returnValue = JSON.toJSONString(getResults(onlineNum, offlineNum), WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty);


        LOGGER.info("StrategyExecutor return value: " + returnValue);
        Date nowTime = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(nowTime));
        System.out.println(returnValue);

        HttpSend.sendPost("POST", "http://" + Constant.HOST + ":" + Constant.PORT2 + "/" + "schedulepod", returnValue);
    }

}
