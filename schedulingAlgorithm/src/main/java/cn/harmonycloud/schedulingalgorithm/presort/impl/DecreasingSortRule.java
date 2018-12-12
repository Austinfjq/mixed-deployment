package cn.harmonycloud.schedulingalgorithm.presort.impl;

import cn.harmonycloud.schedulingalgorithm.algorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.presort.PresortRule;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;

import java.util.List;
import java.util.Map;

public class DecreasingSortRule implements PresortRule {
    //TODO need test
    @Override
    public List<Pod> sort(List<Pod> pods, Cache cache) {
        Map<String, Service> serviceMap = cache.getServiceMap();
        double cpuSum = pods.stream()
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_CPU == s.getIntensiveType())
                .map(Service::getCpuCosume)
                .mapToDouble(Double::valueOf)
                .sum();
        double memSum = pods.stream()
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_MEMORY == s.getIntensiveType())
                .map(Service::getMemCosume)
                .mapToDouble(Double::valueOf)
                .sum();
        double downSum = pods.stream()
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_DOWN_NET_IO == s.getIntensiveType())
                .map(Service::getDownNetIOCosume)
                .mapToDouble(Double::valueOf)
                .sum();
        double upSum = pods.stream()
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_UP_NET_IO == s.getIntensiveType())
                .map(Service::getUPNetIOCosume)
                .mapToDouble(Double::valueOf)
                .sum();
        pods.sort((Pod l, Pod r) -> {
            // 删除优先于增加，operation: 1增加，2减少
            if (!l.getOperation().equals(r.getOperation())) {
                return -Integer.compare(l.getOperation(), r.getOperation());
            }
            // 相对占用的资源大小，占用资源相对大的优先
            // 通过ServiceName查询服务画像里的[资源密集类型, 当前负载资源占用, CPU Mem request值]。
            // 如果是cpu密集型的，优先级 = 此pod要占用的cpu / cpu密集型pods要占用的总cpu，优先级越高，在list中要越靠前。
            else {
                Service lService = serviceMap.get(DOUtils.getServiceFullName(l));
                Service rService = serviceMap.get(DOUtils.getServiceFullName(r));
                double lPriority = getPodSortPriority(lService, cpuSum, memSum, downSum, upSum);
                double rPriority = getPodSortPriority(rService, cpuSum, memSum, downSum, upSum);
                return -Double.compare(lPriority, rPriority);
            }
        });
        return pods;
    }

    private double getPodSortPriority (Service service, double cpuSum, double memSum, double downSum, double upSum) {
        if (Constants.INTENSIVE_TYPE_CPU == service.getIntensiveType()) {
            return Double.valueOf(service.getCpuCosume()) / cpuSum;
        } else if (Constants.INTENSIVE_TYPE_MEMORY == service.getIntensiveType()) {
            return Double.valueOf(service.getMemCosume()) / memSum;
        } else if (Constants.INTENSIVE_TYPE_DOWN_NET_IO == service.getIntensiveType()) {
            return Double.valueOf(service.getUPNetIOCosume()) / downSum;
        } else if (Constants.INTENSIVE_TYPE_UP_NET_IO == service.getIntensiveType()) {
            return Double.valueOf(service.getUPNetIOCosume()) / upSum;
        } else {
            return 0D;
        }
    }
}
