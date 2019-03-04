package cn.harmonycloud.datacenter.mapper;

import cn.harmonycloud.datacenter.entity.mysql.Service;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

}
