package cn.harmonycloud.controller;

import cn.harmonycloud.beans.Node;
import cn.harmonycloud.beans.Service;
import cn.harmonycloud.service.IOfflineRegulateControl;
import cn.harmonycloud.service.IOnlineRegulateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @classname：EvaluateController
 * @author：WANGYUZHONG
 * @date：2019/4/10 21:32
 * @description:TODO
 * @version:1.0
 **/

@RestController
public class EvaluateController {

    private final static Logger LOGGER = LoggerFactory.getLogger(EvaluateController.class);

    @Autowired
    IOnlineRegulateControl iOnlineRegulateControl;

    @Autowired
    IOfflineRegulateControl iOfflineRegulateControl;

    @PostMapping("/illHealthService")
    public boolean regulateIllHealthService(@RequestParam("masterIP") String masterIP,
                              @RequestParam("namespace") String namespace,
                              @RequestParam("serviceName") String serviceName) {
        LOGGER.debug("masterIP="+masterIP+" ,"+
                "namespace="+namespace+" ,"+
                "serviceName="+serviceName);

        Service service = new Service();
        service.setMasterIp(masterIP);
        service.setNamespace(namespace);
        service.setServiceName(serviceName);
        service.setServiceType(1);

        return iOnlineRegulateControl.dealService(service);
    }

    @PostMapping("/illHealthNode")
    public boolean regulateIllHealthNode(@RequestParam("masterIP") String masterIP,
                                            @RequestParam("hostName") String hostName) {
        LOGGER.debug("masterIP="+masterIP+" ,"+
                "hostName="+hostName);

        Node node = new Node();
        node.setMasterIp(masterIP);
        node.setHostName(hostName);

        return iOfflineRegulateControl.dealNode(node);
    }
}
