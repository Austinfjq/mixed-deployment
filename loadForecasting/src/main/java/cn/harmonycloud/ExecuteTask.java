package cn.harmonycloud;

import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;
import cn.harmonycloud.entry.ForecastCell;
import cn.harmonycloud.exception.ServiceGotException;
import cn.harmonycloud.tools.HttpSend;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午2:42
 * @Despriction 定时执行的任务
 */
public class ExecuteTask {

    public void process() {

        List<ForecastCell> forecastCells = getServiceList();

        if (forecastCells == null) {
            throw new ServiceGotException("Get service list failed!");
        }

        if (forecastCells.size() == 0) {
            System.out.println("There is no any service!");
        }

        for (int i = 0; i< forecastCells.size(); i++) {
            ForecastCell forecastCell = forecastCells.get(i);
            ServiceTaskThreadExecutor taskThreadExecutor = new ServiceTaskThreadExecutor(forecastCell);
            Thread thread = new Thread(taskThreadExecutor);
            TaskThreadPoolExecutor.getExecutor().execute(thread);
        }
    }

    public List<ForecastCell> getServiceList() {
        String ServiceListStr = getServiceListStr();

        Type type = new TypeReference<List<ForecastCell>>() {}.getType();
        List<ForecastCell> list = JSON.parseObject(ServiceListStr, type);

        return list;
    }

    public String getServiceListStr() {
        String params = "dsd";
        String url = "http://localhost:8080/service/testSpring";
        return HttpSend.sendPost(url,params);
    }

    public static void main(String[] args) {

        ExecuteTask executeTask = new ExecuteTask();

        List<ForecastCell> forecastCells = executeTask.getServiceList();

        DataSet observedData = new DataSet();
        DataPoint dp;

        dp = new DataPoint( 2.1 , 0);
        observedData.add( dp );

        dp = new DataPoint( 7.7 ,1);
        observedData.add( dp );

        dp = new DataPoint( 13.6 ,2);
        observedData.add( dp );

        dp = new DataPoint( 27.2 ,3);
        observedData.add( dp );

        dp = new DataPoint( 40.9 ,4);
        observedData.add( dp );

        dp = new DataPoint( 61.1 ,5);
        observedData.add( dp );

        dp = new DataPoint( 59.2 ,6);
        observedData.add( dp );

        String str = JSON.toJSONString(observedData);

        System.out.println(str);

    }

}
