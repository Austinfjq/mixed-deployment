package cn.harmonycloud.datacenter.mapper;

import cn.harmonycloud.datacenter.entity.mysql.services;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InsertServiceMapper {
    @Insert("INSERT INTO service(service_id,cluster_ip,namespace,service_name,type)"+
            " VALUES (#{serviceId},#{clusterIp},#{namespace, jdbcType=VARCHAR},#{serviceName, jdbcType=VARCHAR},#{serviceType})")
    public int insertService(services ser);
}
