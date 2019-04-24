package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.entity.mysql.services;
import cn.harmonycloud.datacenter.service.test.serviceImpl.InsertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/service", method = { RequestMethod.GET, RequestMethod.POST })
public class InsertServiceController {
    @Autowired
    private InsertService insertService;
/*    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        insertService = this;
        serverHandler.healthDataService = this.healthDataService;
    }*/
    @RequestMapping(value = "/service", method = RequestMethod.PUT)
/*    public Map<String, Object> insert(@RequestBody List<services> list)
    {
        Map<String, Object> responseMap = new HashMap<>();
        if(list.size() > 0)
        {
            for (services ser : list)
            {
                ser.setServiceId(UUID.randomUUID().toString());
                System.out.println(list.toString() + "1\n");
                Integer i = new Integer(insertService.insertService(ser));
                if (i == 1)
                {
                    responseMap.put("isSucceed", true);
                }
                else
                {
                    responseMap.put("isSucceed", false);
                }
            }
        }
        return responseMap;
    }*/
    public Map<String, Object> insert(@RequestParam("clusterIp") String clusterIp, @RequestParam("namespace") String namespace
    , @RequestParam("serviceName") String serviceName, @RequestParam("serviceType") String serviceType)
    {
        services ser=new services();
        ser.setClusterIp(clusterIp);
        ser.setNamespace(namespace);
        ser.setServiceName(serviceName);
        ser.setServiceType(serviceType);
        System.out.println(ser.toString()+"1\n");
        ser.setServiceId(UUID.randomUUID().toString());
        Integer i=new Integer(insertService.insertService(ser));
        Map<String, Object> responseMap = new HashMap<>();
        if(i==1){
            responseMap.put("isSucceed",true);
        }else{
            responseMap.put("isSucceed",false);
        }
        return responseMap;
    }
/*    public services insert(@RequestBody services ser)
    {
        System.out.println(ser.toString()+"1\n");
        return insertService.insertService(ser);
    }*/

}
