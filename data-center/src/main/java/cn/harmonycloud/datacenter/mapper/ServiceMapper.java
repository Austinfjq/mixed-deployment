package cn.harmonycloud.datacenter.mapper;

import cn.harmonycloud.datacenter.entity.mysql.Service;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/1/25 22:57
*@Modify By:
*/
@Mapper
public interface ServiceMapper {

    @Select("SELECT * FROM service WHERE namespace=#{namespace} AND service_name=#{serviceName}")
    public Service findServiceByNamespaceAndServiceName(String namespace, String serviceName);
    @Select("SELECT * FROM service WHERE cluster_ip=#{clusterIp}")
    public List<Service> findServiceByClusterIp(String clusterIp);
    @Select("SELECT pod_instances FROM serviceload_mapping_podinstances,service WHERE cluster_ip=#{clusterIp} AND namespace=#{namespace} AND service_name=#{serviceName} AND serviceload_mapping_podinstances.service_id=service.service_id")
    public int findPodNumsByService(String clusterIp,String namespace, String serviceName);
}
