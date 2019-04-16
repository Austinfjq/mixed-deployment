package cn.harmonycloud;

import cn.harmonycloud.DAO.IllHealthyDealDAO;
import cn.harmonycloud.DAO.ServiceDAO;
import cn.harmonycloud.DAO.imp.RegulateIllHealthDaoImp;
import cn.harmonycloud.DAO.imp.ServiceDAOImp;
import cn.harmonycloud.beans.EvaluateStrategy;
import cn.harmonycloud.beans.Service;
import cn.harmonycloud.tools.PropertyFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author wangyuzhong
 * @date 18-12-5 下午3:03
 * @Despriction
 */
public class ServiceEvaluateTaskThread implements Runnable{
    private final static Logger LOGGER = LoggerFactory.getLogger(NodeEvaluateTaskThread.class);

    @Override
    public void run() {

        ServiceDAO serviceDAO = new ServiceDAOImp();

        List<Service> services = serviceDAO.getAllOnlineService(PropertyFileUtil.getValue("ClusterIp"));


        List<EvaluateStrategy> serviceStrategies = AchieveStrategy.getServiceStrategies();

        if (null == serviceStrategies) {
            throw new RuntimeException("The service evaluate strategy is null!");
        }

        for (Service service:services) {
            for (EvaluateStrategy evaluateStrategy:serviceStrategies) {
                Class clazz = null;
                Method method = null;
                Object result = null;
                try {
                    clazz = Class.forName(evaluateStrategy.getClassName());
                    method = clazz.getMethod("evaluate", Service.class);
                    result = method.invoke(clazz.newInstance(),service);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                boolean isHealthy = (Boolean)result;
                if (!isHealthy) {
                    LOGGER.info("the "+service.toString()+" is ill health!");
                    IllHealthyDealDAO illHealthyDealDAO = new RegulateIllHealthDaoImp();
                    boolean isDeal = illHealthyDealDAO.regulateIllHealthService(service.getMasterIp(), service.getNamespace(), service.getServiceName());

                    if (isDeal) {
                        LOGGER.info("the "+service.toString()+" is succeed dealed!");
                    }
                    LOGGER.info("the "+service.toString()+" is failed dealed!");
                    break;
                }
            }
        }
    }
}
