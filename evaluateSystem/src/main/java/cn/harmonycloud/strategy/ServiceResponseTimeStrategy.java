package cn.harmonycloud.strategy;

import cn.harmonycloud.beans.ServiceLoad;

/**
 * @author wangyuzhong
 * @date 19-1-21 上午9:56
 * @Despriction
 */
public class ServiceResponseTimeStrategy extends AbstractServiceStrategy{
    @Override
    public boolean evaluate(ServiceLoad serviceLoad) {
        if (serviceLoad.getNormalTimeResponse() < serviceLoad.getTimeResponse()) {
            return false;
        }

        return true;
    }
}
