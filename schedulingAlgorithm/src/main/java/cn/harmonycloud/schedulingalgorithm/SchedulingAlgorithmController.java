package cn.harmonycloud.schedulingalgorithm;

import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.utils.CheckUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schedulingalgorithm")
public class SchedulingAlgorithmController {
    /**
     * @param podList 请求
     * @return 是否成功
     */
    @RequestMapping("/schedulepod")
    public boolean schedulePod(List<Pod> podList) {
        // check parameter
        if (CheckUtil.nullOrEmpty(podList)) {
            return false;
        }
        for (Pod pod : podList) {
            if (pod == null || pod.getOperation() == null || pod.getNamespace() == null || pod.getServiceName() == null
                    || CheckUtil.range(pod.getOperation(), Constants.OPERATION_ADD, Constants.OPERATION_DELETE)) {
                return false;
            }
        }
        // 放入队列
        try {
            boolean result = SchedulingAlgorithmApp.requestQueue.addAll(podList);
            if (result) {
                SchedulingAlgorithmApp.semaphore.release();
            }
            return result;
        } catch (Exception e) {
            // TODO
            return false;
        }
    }
}
