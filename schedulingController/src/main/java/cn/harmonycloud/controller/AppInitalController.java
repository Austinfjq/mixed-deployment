package cn.harmonycloud.controller;


import cn.harmonycloud.service.ICreatResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @classname：AppInitalController
 * @author：WANGYUZHONG
 * @date：2019/4/9 9:55
 * @description:TODO
 * @version:1.0
 **/

@RestController
public class AppInitalController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AppInitalController.class);

    @Autowired
    private ICreatResource iCreatResource;

    @PostMapping("/yamlFile")
    public boolean initialApp(@RequestParam("masterIP") String masterIP,
                              @RequestParam("yaml") String yaml) {
        LOGGER.debug("masterIP=" + masterIP + " ," +
                "yaml=" + yaml);

        return iCreatResource.createResource(masterIP, yaml);
    }

}
