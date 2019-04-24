package cn.harmonycloud.datacenter.service.test.serviceImpl;

import cn.harmonycloud.datacenter.entity.mysql.services;
import cn.harmonycloud.datacenter.mapper.InsertServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertService {
    @Autowired
    private InsertServiceMapper insertServiceMapper;
    public int insertService(services ser)
    {
        System.out.println(ser.toString()+"2\n");
        return insertServiceMapper.insertService(ser);
    }
}
