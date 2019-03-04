package cn.harmonycloud.tools;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

public class Write2ES {
    public static String run(Object list, String saveUrl) {
        String returnValue = JSON.toJSONString(list, WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty);
        HttpSend.sendPost("http://" + Constant.HOST + ":" + Constant.PORT + "/" + saveUrl, returnValue);
        return returnValue;
    }
}
