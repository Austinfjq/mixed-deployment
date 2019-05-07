package cn.harmonycloud.datacenter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
/**
*@Author: shaodilong
*@Description:
*@Date: Created in 2019/4/30 10:12
*@Modify By:
*/
@Mapper
public interface ServiceLoadMappingPodInstancesMapper {
    @Select("SELECT pod_instances FROM serviceload_mapping_podinstances,service WHERE cluster_ip=#{clusterIp} AND namespace=#{namespace} AND service_name=#{serviceName} AND serviceload_mapping_podinstances.service_id=service.service_id")
    public int findPodNumsByService(String clusterIp,String namespace, String serviceName);

    @Select("SELECT pod_instances FROM serviceload_mapping_podinstances,service WHERE cluster_ip=#{clusterIp} AND namespace=#{namespace} AND service_name=#{serviceName} AND service_load=#{serviceLoad} AND serviceload_mapping_podinstances.service_id=service.service_id")
    public int getPodNumsUnderRequest(String clusterIp, String namespace, String serviceName, String serviceLoad);
}
