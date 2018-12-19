package cn.harmonycloud.output;

import cn.harmonycloud.entry.ForecastCell;
import cn.harmonycloud.tools.HttpSend;

/**
 * @author wangyuzhong
 * @date 18-12-14 上午11:13
 * @Despriction
 */
public class SaveDataToEs {

    public static String saveDataToEs(String datalist){
        String params = "data="+datalist;
        String url = "http://localhost:8080/forecast/test";
        return HttpSend.sendPost(url,params);
    }
}
