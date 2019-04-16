package cn.harmonycloud;

import cn.harmonycloud.beans.Node;
import cn.harmonycloud.dao.NodeDAO;
import cn.harmonycloud.dao.imp.NodeDaoImp;
import cn.harmonycloud.service.serviceImp.OfflineRegulateControlServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @classname：OfflineRegulateTaskThread
 * @author：WANGYUZHONG
 * @date：2019/4/10 21:19
 * @description:处理离线应用调控分析的线程
 * @version:1.0
 **/
public class OfflineRegulateTaskThread implements Runnable{

    private final static Logger LOGGER = LoggerFactory.getLogger(OfflineRegulateTaskThread.class);

    @Autowired
    OfflineRegulateControlServiceImp iOfflineRegulateControl;

    @Value("${ClusterIp}")
    private String clusterIp;

    @Override
    public void run() {
        NodeDAO nodeDAO = new NodeDaoImp();
        List<Node> nodes = nodeDAO.getNodeList(clusterIp);

        if (nodes == null) {
            LOGGER.error("get all node failed!");
            return;
        }

        if (nodes.size() == 0) {
            LOGGER.error("there is no any node!");
            return;
        }

        for (Node node :nodes) {
            iOfflineRegulateControl.dealNode(node);
        }
    }
}
