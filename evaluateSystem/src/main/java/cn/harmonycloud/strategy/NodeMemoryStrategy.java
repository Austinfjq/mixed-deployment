package cn.harmonycloud.strategy;

import cn.harmonycloud.DAO.NodeDAO;
import cn.harmonycloud.DAO.imp.NodeDaoImp;
import cn.harmonycloud.beans.Node;
import cn.harmonycloud.tools.PropertyFileUtil;

/**
 * @author wangyuzhong
 * @date 19-1-21 上午9:53
 * @Despriction
 */
public class NodeMemoryStrategy{
    public boolean evaluate(Node node) {
        String queryStr = "sum(node_memory_MemTotal_bytes-node_memory_MemAvailable_bytes{kubernetes_pod_host_ip=" + node.getHostName() +"})by(kubernetes_pod_node_name)\n" +
                "/sum(node_memory_MemTotal_bytes{kubernetes_pod_host_ip=" + node.getHostName() +"})by(kubernetes_pod_node_name)";

        NodeDAO nodeDAO = new NodeDaoImp();

        double nodeCpuUsage = nodeDAO.getNodeIndexValue(queryStr);
        if (nodeCpuUsage > Double.valueOf(PropertyFileUtil.getValue("MemUsageMaxThreshold"))) {
            return false;
        }
        return true;
    }
}
