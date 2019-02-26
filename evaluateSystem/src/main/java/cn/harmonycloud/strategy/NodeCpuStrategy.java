package cn.harmonycloud.strategy;

import cn.harmonycloud.beans.NodeLoad;

/**
 * @author wangyuzhong
 * @date 19-1-21 上午9:55
 * @Despriction
 */
public class NodeCpuStrategy extends AbstractNodeStrategy{
    @Override
    public boolean evaluate(NodeLoad nodeLoad) {
        if (nodeLoad.getCPUUsage() > this.getMaxValue()) {
            return false;
        }

        return true;
    }
}
