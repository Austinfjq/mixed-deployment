package cn.harmonycloud.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author wangyuzhong
 * @date 18-12-26 下午3:14
 * @Despriction
 */
public class DataUtil {
    public static <E> String objectToJson(E e) {
        return JSON.toJSONString(e);
    }

    public static <E> String listToJson(List<E> list) {
        return JSON.toJSONString(list);
    }

    public static <E> E jsonStringtoObject(String jsonStr) {
        Type type = new TypeReference<E>() {
        }.getType();
        return JSON.parseObject(jsonStr, type);
    }

    public static <E> List<E> jsonStringtoListObject(String listJsonStr) {
        Type type = new TypeReference<List<E>>() {
        }.getType();
        return JSON.parseObject(listJsonStr, type);
    }

    public static String mapToJson(Map<String,Object> map) {
        return "["+(new JSONObject(map)).toJSONString()+"]";
    }
}
