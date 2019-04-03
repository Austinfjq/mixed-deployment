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

import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static java.lang.Math.abs;

public class StrategyExecutor {

    private List<ForecastNode> forecastNodeList;
    private List<NowNode> nowNodeList;
    private List<ForecastService> forecastServiceList;
    private List<NowService> nowServiceList;
    //    private ArrayList<Result> results;
    private int onlineNum;
    private int offlineNum;

    public StrategyExecutor(List<ForecastNode> forecastNodeList, List<NowNode> nowNodeList, List<ForecastService> forecastServiceList, List<NowService> nowServiceList, int onlineNum, int offlineNum) {
        this.forecastNodeList = forecastNodeList;
        this.nowNodeList = nowNodeList;
        this.forecastServiceList = forecastServiceList;
        this.nowServiceList = nowServiceList;
        this.onlineNum = onlineNum;
        this.offlineNum = offlineNum;

    }


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

        ArrayList<Result> results = new ArrayList<>();

        AppList appList = new AppList();
        appList.init();

        int podNum = 0;

        for (int i = 0; i < appList.getOnlineApp().size(); i++) {

            for (NowService nowService : nowServiceList) {
                if (nowService.getServiceName().equals(appList.getOnlineApp().get(i).get("name"))
                        && nowService.getNamespace().equals(appList.getOnlineApp().get(i).get("namespace"))) {
                    podNum = nowService.getPodNums() - onlineNum;
                    break;
                }
            }

            Result resultPod = new Result(podNum < 0 ? 1 : 2, appList.getOnlineApp().get(i).get("namespace"),
                    appList.getOnlineApp().get(i).get("name"), String.valueOf(abs(podNum)));//1增加，2减少
            results.add(resultPod);

        }

        for (int i = 0; i < appList.getOfflineApp().size(); i++) {

            for (NowService nowService : nowServiceList) {
                if (nowService.getServiceName().equals(appList.getOfflineApp().get(i).get("name"))
                        && nowService.getNamespace().equals(appList.getOfflineApp().get(i).get("namespace"))) {
                    podNum = nowService.getPodNums() - offlineNum;
                    break;
                }
            }

            Result resultPod = new Result(podNum < 0 ? 1 : 2, appList.getOfflineApp().get(i).get("namespace"),
                    appList.getOfflineApp().get(i).get("name"), String.valueOf(abs(podNum)));//1增加，2减少
            results.add(resultPod);

        }


        return results;
    }


    public static void run(int onlineNum, int offlineNum) {

        Write2ES.run(getResults(onlineNum, offlineNum), "schedulePods");
        String returnValue = JSON.toJSONString(getResults(onlineNum, offlineNum), WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty);

        Date nowTime = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(nowTime));
        System.out.println(returnValue);

        HttpSend.sendPost("POST", "http://" + Constant.URL_HOST + ":" + Constant.URL_PORT2 + "/" + "schedulepod", returnValue);
    }

}
