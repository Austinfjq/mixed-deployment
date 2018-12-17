package cn.harmonycloud.schedulingalgorithm.presort.impl;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.presort.PresortRule;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class DecreasingSortRule implements PresortRule {
    //TODO need test
    @Override
    public List<Pod> sort(List<Pod> pods, Cache cache) {
        Map<String, Service> serviceMap = cache.getServiceMap();
        double[][] resourceSum = new double[5][3];
        calculateSum(resourceSum, pods, cache.getServiceMap());
        pods.sort((Pod l, Pod r) -> {
            // 删除优先于增加，operation: 1增加，2减少
            if (!l.getOperation().equals(r.getOperation())) {
                return -Integer.compare(l.getOperation(), r.getOperation());
            } else if (DOUtils.getServiceFullName(l).equals(DOUtils.getServiceFullName(r))) {
                return 0;
            }
            // 相对占用的资源大小，占用资源相对大的优先
            // 通过ServiceName查询服务画像里的[资源密集类型, 当前负载资源占用, CPU Mem request值]。
            // 如果是cpu密集型的，优先级 = 此pod要占用的cpu / cpu密集型pods要占用的总cpu，优先级越高，在list中要越靠前。
            else {
                Service lService = serviceMap.get(DOUtils.getServiceFullName(l));
                Service rService = serviceMap.get(DOUtils.getServiceFullName(r));
                double lPriority = getPodSortPriority(lService, l.getOperation(), resourceSum);
                double rPriority = getPodSortPriority(rService, r.getOperation(), resourceSum);
                System.out.println(DOUtils.getServiceFullName(lService) + "," + lPriority);
                System.out.println(DOUtils.getServiceFullName(rService) + "," + rPriority);
                return -Double.compare(lPriority, rPriority);
            }
        });
        return pods;
    }

    /**
     * 计算排序优先级
     */
    private double getPodSortPriority(Service service, Integer operation, double[][] resourceSum) {
        if (Constants.INTENSIVE_TYPE_CPU == service.getIntensiveType()) {
            return Double.valueOf(service.getCpuCosume()) / resourceSum[Constants.INTENSIVE_TYPE_CPU][operation];
        } else if (Constants.INTENSIVE_TYPE_MEMORY == service.getIntensiveType()) {
            return Double.valueOf(service.getMemCosume()) / resourceSum[Constants.INTENSIVE_TYPE_MEMORY][operation];
        } else if (Constants.INTENSIVE_TYPE_DOWN_NET_IO == service.getIntensiveType()) {
            return Double.valueOf(service.getUPNetIOCosume()) / resourceSum[Constants.INTENSIVE_TYPE_DOWN_NET_IO][operation];
        } else if (Constants.INTENSIVE_TYPE_UP_NET_IO == service.getIntensiveType()) {
            return Double.valueOf(service.getUPNetIOCosume()) / resourceSum[Constants.INTENSIVE_TYPE_UP_NET_IO][operation];
        } else {
            return 0D;
        }
    }

    private void calculateSum(double[][] resourceSum, List<Pod> pods, Map<String, Service> serviceMap) {
        resourceSum[Constants.INTENSIVE_TYPE_CPU][Constants.OPERATION_ADD] = pods.stream()
                .filter(p->p.getOperation() == Constants.OPERATION_ADD)
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_CPU == s.getIntensiveType())
                .map(Service::getCpuCosume)
                .mapToDouble(Double::valueOf)
                .sum();
        resourceSum[Constants.INTENSIVE_TYPE_CPU][Constants.OPERATION_DELETE] = pods.stream()
                .filter(p->p.getOperation() == Constants.OPERATION_DELETE)
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_CPU == s.getIntensiveType())
                .map(Service::getCpuCosume)
                .mapToDouble(Double::valueOf)
                .sum();

        resourceSum[Constants.INTENSIVE_TYPE_MEMORY][Constants.OPERATION_ADD] = pods.stream()
                .filter(p->p.getOperation() == Constants.OPERATION_ADD)
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_MEMORY == s.getIntensiveType())
                .map(Service::getMemCosume)
                .mapToDouble(Double::valueOf)
                .sum();
        resourceSum[Constants.INTENSIVE_TYPE_MEMORY][Constants.OPERATION_DELETE] = pods.stream()
                .filter(p->p.getOperation() == Constants.OPERATION_DELETE)
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_MEMORY == s.getIntensiveType())
                .map(Service::getMemCosume)
                .mapToDouble(Double::valueOf)
                .sum();

        resourceSum[Constants.INTENSIVE_TYPE_DOWN_NET_IO][Constants.OPERATION_ADD] = pods.stream()
                .filter(p->p.getOperation() == Constants.OPERATION_ADD)
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_DOWN_NET_IO == s.getIntensiveType())
                .map(Service::getDownNetIOCosume)
                .mapToDouble(Double::valueOf)
                .sum();
        resourceSum[Constants.INTENSIVE_TYPE_DOWN_NET_IO][Constants.OPERATION_DELETE] = pods.stream()
                .filter(p->p.getOperation() == Constants.OPERATION_DELETE)
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_DOWN_NET_IO == s.getIntensiveType())
                .map(Service::getDownNetIOCosume)
                .mapToDouble(Double::valueOf)
                .sum();

        resourceSum[Constants.INTENSIVE_TYPE_UP_NET_IO][Constants.OPERATION_ADD] = pods.stream()
                .filter(p->p.getOperation() == Constants.OPERATION_ADD)
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_UP_NET_IO == s.getIntensiveType())
                .map(Service::getUPNetIOCosume)
                .mapToDouble(Double::valueOf)
                .sum();
        resourceSum[Constants.INTENSIVE_TYPE_UP_NET_IO][Constants.OPERATION_DELETE] = pods.stream()
                .filter(p->p.getOperation() == Constants.OPERATION_DELETE)
                .map(p -> serviceMap.get(DOUtils.getServiceFullName(p)))
                .filter(s -> Constants.INTENSIVE_TYPE_UP_NET_IO == s.getIntensiveType())
                .map(Service::getUPNetIOCosume)
                .mapToDouble(Double::valueOf)
                .sum();
    }
}
