package cn.harmonycloud;

import cn.harmonycloud.DAO.IllHealthyDealDAO;
import cn.harmonycloud.DAO.NodeDAO;
import cn.harmonycloud.DAO.imp.NodeDaoImp;
import cn.harmonycloud.DAO.imp.RegulateIllHealthDaoImp;
import cn.harmonycloud.beans.EvaluateStrategy;
import cn.harmonycloud.beans.Node;
import cn.harmonycloud.tools.PropertyFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author wangyuzhong
 * @date 19-1-22 下午2:46
 * @Despriction
 */
public class NodeEvaluateTaskThread implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(NodeEvaluateTaskThread.class);

    @Override
    public void run() {

        NodeDAO nodeDAO = new NodeDaoImp();
        List<Node> nodes = nodeDAO.getNodeList(PropertyFileUtil.getValue("ClusterIp"));

        if (null == nodes) {
            LOGGER.error("get node list failed!");
        }



        List<EvaluateStrategy> nodeStrategies = AchieveStrategy.getNodeStrategies();

        if (null == nodeStrategies) {
            LOGGER.error("the node strategy is null!");
        }

        for (Node node : nodes) {
            for (EvaluateStrategy evaluateStrategy : nodeStrategies) {
                Class clazz = null;
                Method method = null;
                Object result = null;
                try {
                    clazz = Class.forName(evaluateStrategy.getClassName());
                    method = clazz.getMethod("evaluate",Node.class);
                    result = method.invoke(clazz.newInstance(),node);
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
                    LOGGER.info("the "+node.toString()+" is ill health!");
                    IllHealthyDealDAO illHealthyDealDAO = new RegulateIllHealthDaoImp();
                    boolean isDeal = illHealthyDealDAO.regulateIllHealthNode(node.getMasterIp(), node.getHostName());

                    if (isDeal) {
                        LOGGER.info("the "+node.toString()+" is succeed dealed!");
                    }
                    LOGGER.info("the "+node.toString()+" is failed dealed!");
                    break;
                }
            }
        }
    }
}
