package cn.harmonycloud.DAO;

import cn.harmonycloud.beans.Node;

import java.util.List;

public interface NodeDAO {

    /**
     * @Author WANGYUZHONG
     * @Description //获取某个集群中所有的工作节点
     * @Date 10:08 2019/4/11
     * @Param
     * @return
     **/
    List<Node> getNodeList(String clusterIp);


    /**
     * @Author WANGYUZHONG
     * @Description //获取节点的某个指标最新值
     * @Date 8:08 2019/4/15
     * @Param 查询语句
     * @return 
     **/
    double getNodeIndexValue(String queryStr);
}
