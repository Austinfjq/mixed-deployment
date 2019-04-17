package cn.harmonycloud.strategy;

import cn.harmonycloud.DAO.NodeDAO;
import cn.harmonycloud.DAO.imp.NodeDaoImp;
import cn.harmonycloud.beans.Node;
import cn.harmonycloud.tools.PropertyFileUtil;

/**
 * @author wangyuzhong
 * @date 19-1-21 上午9:55
 * @Despriction
 */
public class NodeCpuStrategy {

    public boolean evaluate(Node node) {
        String queryStr = "sum(rate(node_cpu_seconds_total{kubernetes_pod_host_ip=" + node.getHostName() +"}[5m]))by(kubernetes_pod_node_name)";

        NodeDAO nodeDAO = new NodeDaoImp();

        double nodeCpuUsage = nodeDAO.getNodeIndexValue(queryStr);

        if (nodeCpuUsage > Double.valueOf(PropertyFileUtil.getValue("CpuUsageMaxThreshold"))) {
            return false;
        }
        return true;
    }
}
