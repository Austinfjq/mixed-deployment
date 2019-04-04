package cn.harmonycloud.reference;

import cn.harmonycloud.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hc on 19-1-14.
 * 获取所属关系
 */
public class Reference {
    private final static Logger LOGGER = LoggerFactory.getLogger(Reference.class);
    public static JSONObject getOwnerOfPod(String masterIp,String namespace,String serviceName)  {
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("masterIp",masterIp);
        paramMap.put("namespace",namespace);
        paramMap.put("serviceName", serviceName);

        String result = null;
        result = HttpClientUtil.httpGet(paramMap);
        if(result == null){
            LOGGER.debug("Owner information is null");
            return null;
        }
        return  JSON.parseObject(result);
    }
    public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = getOwnerOfPod("","wordpress","wordpress-wp");
        System.out.println("ResourceKing:"+data.getString("resourceKind"));
        System.out.println("ResourceName:"+data.getString("resourceName"));
    }
}
