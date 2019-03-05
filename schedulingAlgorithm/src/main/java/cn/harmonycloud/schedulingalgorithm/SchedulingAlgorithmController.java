package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.affinity.PodAntiAffinity;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.SchedulePod;
import cn.harmonycloud.schedulingalgorithm.utils.CheckUtil;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SchedulingAlgorithmController {
    /**
     * @param schedulePods 请求
     * @return 是否成功
     */
    @RequestMapping("/schedulepod")
    public Map<String, Object> schedulePod(@RequestBody List<SchedulePod> schedulePods) {
        Map<String, Object> responseMap = new HashMap<>();
        // check parameter
        if (CheckUtil.nullOrEmpty(schedulePods)) {
            responseMap.put("isSucceed", false);
            return responseMap;
        }
        for (SchedulePod pod : schedulePods) {
            if (pod == null || pod.getOperation() == null || pod.getNamespace() == null || pod.getServiceName() == null
                    || !CheckUtil.range(pod.getOperation(), Constants.OPERATION_ADD, Constants.OPERATION_DELETE)) {
                responseMap.put("isSucceed", false);
                return responseMap;
            }
        }
        // 放入队列
        try {
            List<Pod> podList = new ArrayList<>();
            for (SchedulePod sp : schedulePods) {
                if (sp.getNumber() == null) {
                    sp.setNumber(1);
                }
                for (int i = 0; i < sp.getNumber(); i++) {
                    Pod pod = new Pod(sp.getOperation(), sp.getNamespace(), sp.getServiceName());
                    podList.add(pod);
                }
            }
            boolean result = SchedulingAlgorithmApp.requestQueue.addAll(podList);
            if (result) {
                SchedulingAlgorithmApp.semaphore.release();
            }
            responseMap.put("isSucceed", result);
            return responseMap;
        } catch (Exception e) {
            // TODO
            responseMap.put("isSucceed", false);
            return responseMap;
        }
    }
}
