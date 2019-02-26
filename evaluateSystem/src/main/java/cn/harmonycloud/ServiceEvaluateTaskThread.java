package cn.harmonycloud;

import cn.harmonycloud.beans.ServiceLoad;
import cn.harmonycloud.strategy.AbstractServiceStrategy;
import java.util.List;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午3:03
 * @Despriction
 */
public class ServiceEvaluateTaskThread implements Runnable{

    private List<ServiceLoad> serviceLoads;

    public ServiceEvaluateTaskThread(List<ServiceLoad> serviceLoads) {
        this.serviceLoads = serviceLoads;
    }

    @Override
    public void run() {
        if (serviceLoads == null) {
            System.out.println("serviceLoads is null!");
            return;
        }


        List<AbstractServiceStrategy> abstractServiceStrategies = AchieveStrategy.getAbstractServiceStrategies();

        if (null == abstractServiceStrategies) {
            throw new RuntimeException("The service evaluate strategy is null!");
        }

        for (ServiceLoad serviceLoad:serviceLoads) {
            for (AbstractServiceStrategy abstractServiceStrategy:abstractServiceStrategies) {
                boolean isHealthy = abstractServiceStrategy.evaluate(serviceLoad);
                if(!isHealthy) {
                    System.out.println("调用调控模块service接口！");
                }
            }
        }
    }
}
