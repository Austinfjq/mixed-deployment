package cn.harmonycloud;

import cn.harmonycloud.beans.NodeLoad;
import cn.harmonycloud.strategy.AbstractNodeStrategy;

import java.util.List;

/**
 * @author wangyuzhong
 * @date 19-1-22 下午2:46
 * @Despriction
 */
public class NodeEvaluateTaskThread implements Runnable {

    private List<NodeLoad> nodeLoads;

    public NodeEvaluateTaskThread(List<NodeLoad> nodeLoads) {
        this.nodeLoads = nodeLoads;
    }

    @Override
    public void run() {
        if (nodeLoads == null) {
            System.out.println("nodeLoads is null!");
            return;
        }


        List<AbstractNodeStrategy> abstractNodeStrategies = AchieveStrategy.getAbstractNodeStrategies();

        if (null == abstractNodeStrategies) {
            throw new RuntimeException("The node evaluate strategy is null!");
        }

        for (NodeLoad nodeLoad : nodeLoads) {
            for (AbstractNodeStrategy abstractNodeStrategy : abstractNodeStrategies) {
                boolean isHealthy = abstractNodeStrategy.evaluate(nodeLoad);
                if (!isHealthy) {
                    System.out.println("调用调控模块node接口！");
                }
            }
        }
    }
}
