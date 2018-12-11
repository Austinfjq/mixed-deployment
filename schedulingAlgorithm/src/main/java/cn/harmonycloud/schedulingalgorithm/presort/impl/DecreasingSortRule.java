package cn.harmonycloud.schedulingalgorithm.presort.impl;

import cn.harmonycloud.schedulingalgorithm.algorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import cn.harmonycloud.schedulingalgorithm.presort.PresortRule;

import java.util.List;
import java.util.Map;

public class DecreasingSortRule implements PresortRule {
    //TODO need test
    @Override
    public List<Pod> sort(List<Pod> pods, Cache cache) {
        Map<String, Service> serviceMap = cache.getServiceMap();
        Double cpuSum = pods.stream()
                .map(p -> serviceMap.get(p.getServiceName()))
                .filter(s -> Constants.INTENSIVE_TYPE_CPU.equals(s.getIntensiveType()))
                .mapToDouble(Service::getPodCpu)
                .sum();
        Double memSum = pods.stream()
                .map(p -> serviceMap.get(p.getServiceName()))
                .filter(s -> Constants.INTENSIVE_TYPE_MEMORY.equals(s.getIntensiveType()))
                .mapToDouble(Service::getPodMemory)
                .sum();
//        Double diskSum = pods.stream()
//                .map(p -> serviceMap.get(p.getServiceName()))
//                .filter(s -> Constants.INTENSIVE_TYPE_DISK.equals(s.getIntensiveType()))
//                .mapToDouble(Service::getPodDisk)
//                .sum();
        Double ioSum = pods.stream()
                .map(p -> serviceMap.get(p.getServiceName()))
                .filter(s -> Constants.INTENSIVE_TYPE_IO.equals(s.getIntensiveType()))
                .mapToDouble(Service::getPodIo)
                .sum();
        pods.sort((Pod l, Pod r) -> {
            // 删除优先于增加，operation: 0增加，1减少
            if (!l.getOperation().equals(r.getOperation())) {
                return l.getOperation() - r.getOperation();
            }
            // 相对占用的资源大小，占用资源相对大的优先
            // 通过ServiceName查询服务画像里的[资源密集类型, 当前负载资源占用, CPU Mem request值]。
            // 如果是cpu密集型的，优先级 = 此pod要占用的cpu / cpu密集型pods要占用的总cpu，优先级越高，在list中要越靠前。
            else {
                Service lService = serviceMap.get(l.getServiceName());
                Service rService = serviceMap.get(r.getServiceName());
                double lPriority, rPriority;
                if (Constants.INTENSIVE_TYPE_CPU.equals(lService.getIntensiveType())) {
                    lPriority = lService.getPodCpu() / cpuSum;
                    rPriority = rService.getPodCpu() / cpuSum;
                } else if (Constants.INTENSIVE_TYPE_MEMORY.equals(lService.getIntensiveType())) {
                    lPriority = lService.getPodMemory() / memSum;
                    rPriority = rService.getPodMemory() / memSum;
                }
//                else if (Constants.INTENSIVE_TYPE_DISK.equals(lService.getIntensiveType())) {
//                    lPriority = lService.getPodDisk() / diskSum;
//                    rPriority = rService.getPodDisk() / diskSum;
//                }
                else if (Constants.INTENSIVE_TYPE_IO.equals(lService.getIntensiveType())) {
                    lPriority = lService.getPodIo() / ioSum;
                    rPriority = rService.getPodIo() / ioSum;
                } else {
                    // something wrong
                    lPriority = rPriority = 0;
                }
                return -Double.compare(lPriority, rPriority);
            }
        });
        return pods;
    }
}
