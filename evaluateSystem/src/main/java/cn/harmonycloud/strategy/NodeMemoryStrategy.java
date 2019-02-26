package cn.harmonycloud.strategy;

import cn.harmonycloud.beans.NodeLoad;

/**
 * @author wangyuzhong
 * @date 19-1-21 上午9:53
 * @Despriction
 */
public class NodeMemoryStrategy extends AbstractNodeStrategy{
    @Override
    public boolean evaluate(NodeLoad nodeLoad) {
        if (nodeLoad.getMemUsage() > this.getMaxValue()) {
            return false;
        }

        return true;
    }
}
