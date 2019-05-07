package cn.harmonycloud.datacenter.mapper;

import cn.harmonycloud.datacenter.entity.mysql.Service;
import org.apache.ibatis.annotations.Insert;
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

    @Insert("INSERT INTO service (service_id,namespace,service_name,cluster_ip,owner_type,owner_name,cpu_request,cpu_limit,mem_request,mem_limit,period,response_time,type)" +
            "VALUES (#{service_id},#{namespace},#{serviceName},#{clusterIp},#{ownerType},#{ownerName},#{cpuRequest},#{cpuLimit}),#{memRequest},#{memLimit},#{period},#{responseTime},#{type}")
    public int saveOneService(Service service);
}
