package cn.harmonycloud.tools;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @author wangyuzhong
 * @date 18-12-26 下午3:14
 * @Despriction
 */
public class DateUtil {
    public static <E> String forecastResultCellToJson(E e) {
        return JSON.toJSONString(e);
    }

    public static <E> String forecastResultCellListToJson(List<E> list) {
        return JSON.toJSONString(list);
    }
}
