package cn.harmonycloud.schedulingalgorithm.utils;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.constant.GlobalSetting;
import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExecuteUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExecuteUtil.class);

    public static void scheduleExecute(Pod pod, HostPriority host, Cache cache) {
        if (host == null) {
            return;
        }
        try {
            LOGGER.info("start scheduleExecute: " + (pod.getOperation() == Constants.OPERATION_ADD ? "add" : "delete") + ": Service=" + DOUtils.getServiceFullName(pod) + ", host=" + host.getHostname());
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("namespace", pod.getNamespace()));
            paramList.add(new BasicNameValuePair("servicename", pod.getServiceName()));
            paramList.add(new BasicNameValuePair("clusterMasterIP", pod.getClusterMasterIP()));
            String uri;
            if (pod.getOperation() == Constants.OPERATION_ADD) {
                JSONArray jsonArray = new JSONArray();
                JSONObject nodeJson = new JSONObject();
                nodeJson.put("hostname", host.getHostname());
                nodeJson.put("score", host.getScore().toString());
                jsonArray.add(nodeJson);
                paramList.add(new BasicNameValuePair("nodeList", jsonArray.toString()));
                uri = GlobalSetting.URI_EXECUTE_ADD;
            } else {
                Optional<String> op = cache.getNodeMapPodList().get(host.getHostname()).stream().filter(p -> DOUtils.getServiceFullName(pod).equals(DOUtils.getServiceFullName(p))).map(Pod::getPodName).findFirst();
                String podName = op.orElse(null);
                paramList.add(new BasicNameValuePair("podName", podName));
                uri = GlobalSetting.URI_EXECUTE_REMOVE;
            }
            String result = HttpUtil.get(uri, paramList);
            LOGGER.info("scheduleExecute result: " + result);
            if (result == null || !result.equals("true")) {
                LOGGER.warn("scheduleExecute fail!");
            }
        } catch (Exception e) {
            LOGGER.debug("scheduleExecute fail");
            e.printStackTrace();
        }
    }
}
