package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.NodeForecastData;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class FakeCache extends Cache {
    private final static Logger LOGGER = LoggerFactory.getLogger(FakeCache.class);
    private static Gson gson = new Gson();

    private String nowService;
    private String nowNode;
    private String nowPod;

    FakeCache() {
        try {
            nowService = new Scanner(new File("schedulingAlgorithm/src/main/resources/data/nowService.txt")).useDelimiter("\\Z").next();
            nowNode = new Scanner(new File("schedulingAlgorithm/src/main/resources/data/nowNode.txt")).useDelimiter("\\Z").next();
            nowPod = new Scanner(new File("schedulingAlgorithm/src/main/resources/data/nowPod.txt")).useDelimiter("\\Z").next();
        } catch (Exception e) {
            LOGGER.error("Data file error!", e);
        }
    }

    @Override
    <T> List<Object> fetchOne(String uri, Class<T> clazz) {
        LOGGER.info("Fake data instead of " + uri);
        List<Object> result = new ArrayList<>();
        String jsonStr = null;
        try {
            if (uri.equals(GlobalSetting.URI_GET_SERVICE)) {
                jsonStr = nowService;
            } else if (uri.equals(GlobalSetting.URI_GET_NODE)) {
                jsonStr = nowNode;
            } else if (uri.equals(GlobalSetting.URI_GET_POD)) {
                jsonStr = nowPod;
            }
//            jsonStr = HttpUtil.get(uri);
        } catch (Exception e) {
            LOGGER.debug("Http request fail:\"" + uri + "\"");
            e.printStackTrace();
        }
        try {
            Object[] objects = (Object[]) gson.fromJson(jsonStr, clazz);
            Collections.addAll(result, objects);
        } catch (Exception e) {
            LOGGER.debug("Data format error:");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    HashMap<String, NodeForecastData> fetchNodeForecast() {
        return new HashMap<>();
    }
}