package cn.harmonycloud.tools;

import cn.harmonycloud.metric.Constant;
import com.alibaba.fastjson.JSON;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

public class Write2ES {
    public static String run(Object list, String saveUrl) {
        String returnValue = JSON.toJSONString(list, WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty);
        HttpSend.sendPost("http://" + Constant.URL_HOST + ":" + Constant.URL_PORT + "/" + saveUrl, returnValue);
        return returnValue;
    }
}
