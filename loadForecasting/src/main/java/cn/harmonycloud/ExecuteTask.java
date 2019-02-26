package cn.harmonycloud;

import cn.harmonycloud.entry.ForecastCell;
import cn.harmonycloud.entry.HttpClientResult;
import cn.harmonycloud.exception.ServiceGotException;
import cn.harmonycloud.tools.HttpClientUtils;
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

        List<ForecastCell> forecastCells = getForecastCellList();

        if (forecastCells == null) {
            throw new ServiceGotException("Get service list failed!");
        }

        if (forecastCells.size() == 0) {
            System.out.println("There is no any service!");
        }

        for (int i = 0; i< forecastCells.size(); i++) {
            ForecastCell forecastCell = forecastCells.get(i);
            TaskThreadExecutor taskThreadExecutor = new TaskThreadExecutor(forecastCell);
            Thread thread = new Thread(taskThreadExecutor);
            TaskThreadPoolExecutor.getExecutor().execute(thread);
        }
    }

    public List<ForecastCell> getForecastCellList() {
        HttpClientResult httpClientResult = getForecastCellListStr();

        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            System.out.println("get service list failed!");
            return null;
        }
        String ServiceListStr = httpClientResult.getContent();

        Type type = new TypeReference<List<ForecastCell>>() {}.getType();
        List<ForecastCell> list = JSON.parseObject(ServiceListStr, type);

        return list;
    }

    public HttpClientResult getForecastCellListStr() {
        String url = "http://localhost:8080/forecast/forecastCellList";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = HttpClientUtils.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpClientResult;
    }

    public static void main(String[] args) {

        ExecuteTask executeTask = new ExecuteTask();
        executeTask.process();

    }

}
