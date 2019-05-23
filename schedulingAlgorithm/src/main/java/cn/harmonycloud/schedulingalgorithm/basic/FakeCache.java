package cn.harmonycloud.schedulingalgorithm.basic;

import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.ContainerPort;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.NodeForecastData;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FakeCache extends Cache {
    private final static Logger LOGGER = LoggerFactory.getLogger(FakeCache.class);
    private static Gson gson = new Gson();
    private static Random random = new Random();

    private String nowService;
    private String nowNode;
    private String nowPod;

    public FakeCache() {
        try {
            nowService = new Scanner(new File("/Users/fanjinqian/mixed-deployment/schedulingAlgorithm/src/main/resources/data/nowService.txt")).useDelimiter("\\Z").next();
            nowNode = new Scanner(new File("/Users/fanjinqian/mixed-deployment/schedulingAlgorithm/src/main/resources/data/nowNode.txt")).useDelimiter("\\Z").next();
            nowPod = new Scanner(new File("/Users/fanjinqian/mixed-deployment/schedulingAlgorithm/src/main/resources/data/nowPod.txt")).useDelimiter("\\Z").next();
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
            if (uri.equals(GlobalSetting.URI_GET_NODE)) {
                // 扩大集群节点数量
                Node node = (Node) Arrays.stream(objects).filter(n -> ((Node) n).getNodeName().equals("10.10.103.26-slave")).collect(Collectors.toList()).get(0);
                for (int i = 0; i < 12 - 7; i++) {
                    Node newNode = gson.fromJson(gson.toJson(node), Node.class);
                    newNode.setNodeName(newNode.getNodeName() + "-" + i);
                    Collections.addAll(result, newNode);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("Data format error:");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getPortrait(List<Pod> pods) {
        super.getPortrait(pods);
        // 修改资源请求量 fake data
        for (int i = 0; i < pods.size(); i++) {
            Pod p = pods.get(i);
            if (i % 2 == 0) {
                p.setCpuRequest(random.nextDouble() * 4 * 0.7);
                p.setMemRequest(random.nextDouble() * 7.6E9 * 0.01);
            } else if (i % 2 == 1) {
                p.setCpuRequest(random.nextDouble() * 4 * 0.01);
                p.setMemRequest(random.nextDouble() * 7.6E9 * 0.7);
            }
            else {
                p.setCpuRequest(random.nextDouble() * 4 * 0.5);
                p.setMemRequest(random.nextDouble() * 7.6E9 * 0.5);
            }
        }
    }

    @Override
    HashMap<String, NodeForecastData> fetchNodeForecast() {
        return new HashMap<>();
    }
}