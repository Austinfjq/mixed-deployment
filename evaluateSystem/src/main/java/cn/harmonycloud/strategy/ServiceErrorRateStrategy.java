package cn.harmonycloud.strategy;

import cn.harmonycloud.beans.ServiceLoad;

/**
 * @author wangyuzhong
 * @date 19-1-21 上午9:57
 * @Despriction
 */
public class ServiceErrorRateStrategy extends AbstractServiceStrategy{
    @Override
    public boolean evaluate(ServiceLoad serviceLoad) {
        if (serviceLoad.getNormalErrorRate() < serviceLoad.getErrorRate()) {
            return false;
        }

        return true;
    }
}
