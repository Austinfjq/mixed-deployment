package cn.harmonycloud.reference;

import cn.harmonycloud.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hc on 19-1-14.
 * 获取所属关系
 */
public class Reference {
    private final static Logger LOGGER = LoggerFactory.getLogger(Reference.class);
    public static JSONObject getOwnerOfPod(String namespace,String serviceName) {
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("namespace",namespace);
        paramMap.put("servicename", serviceName);

        String result = HttpClientUtil.httpGet(paramMap);
        LOGGER.info("Get Owner information["+result+"]");
        return  JSON.parseObject(result);
    }
}
