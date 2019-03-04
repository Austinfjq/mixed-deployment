package cn.harmonycloud.strategy;

import cn.harmonycloud.beans.NodeLoad;

/**
 * @author wangyuzhong
 * @date 19-1-21 上午9:56
 * @Despriction
 */
public class NodeDiskStrategy extends AbstractNodeStrategy{
    @Override
    public boolean evaluate(NodeLoad nodeLoad) {
        System.out.println("node disk evaluate executed!");
        if (nodeLoad.getDiskUsage() > this.getMaxValue()) {
            return false;
        }

        return true;
    }
}
