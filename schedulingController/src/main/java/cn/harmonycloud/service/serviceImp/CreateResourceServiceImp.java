package cn.harmonycloud.service.serviceImp;

import cn.harmonycloud.beans.OnlineStrategy;
import cn.harmonycloud.beans.Service;
import cn.harmonycloud.dao.ServiceDAO;
import cn.harmonycloud.dao.StrategyDAO;
import cn.harmonycloud.service.ICreatResource;
import cn.harmonycloud.service.IResolveYamlFile;
import cn.harmonycloud.service.IStrategyProduce;
import cn.harmonycloud.tools.K8sClient;
import com.alibaba.fastjson.JSONObject;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


/**
 * @classname：CreateResourceServiceImp
 * @author：WANGYUZHONG
 * @date：2019/4/9 11:49
 * @description:TODO
 * @version:1.0
 **/
@org.springframework.stereotype.Service
public class CreateResourceServiceImp implements ICreatResource {

    @Autowired
    private IResolveYamlFile iResolveYamlFile;

    @Autowired
    private ServiceDAO serviceDAO;

    @Autowired
    private IStrategyProduce iStrategyProduce;

    @Autowired
    private StrategyDAO iStrategyDeal;

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateResourceServiceImp.class);

    public boolean creatOthersResource(KubernetesClient client, String yaml) {
        InputStream is = new ByteArrayInputStream(yaml.getBytes());
        Object o = client.load(is).createOrReplace();
        if (o == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean creatServiceResource(String masterIP, KubernetesClient client, String yaml) {
        JSONObject jsonObject = JSONObject.parseObject(yaml);
        InputStream is = new ByteArrayInputStream(yaml.getBytes());
        Object o = client.load(is).createOrReplace();
        if (o == null) {
            return false;
        }

        Service service = iResolveYamlFile.resolveService(masterIP, jsonObject);
        serviceDAO.addService(service);

        LOGGER.debug("service:" + service.toString() + " create succeed!");

        return true;
    }


    public boolean creatCustomResource(String masterIP, KubernetesClient client, String yaml) {
        JSONObject jsonObject = JSONObject.parseObject(yaml);
        int replicas = iResolveYamlFile.getReplicas(jsonObject);

        JSONObject changedJSONObject = iResolveYamlFile.changeReplicas(jsonObject);
        InputStream is = new ByteArrayInputStream(changedJSONObject.toString().getBytes());
        Object o = client.load(is).createOrReplace();
        if (o == null) {
            return false;
        }

        String namespace = iResolveYamlFile.getCustomResourceNamespace(jsonObject);

        String name = iResolveYamlFile.getCustomResourceName(jsonObject);

        OnlineStrategy onlineStrategy = iStrategyProduce.produceOnlineDilatationStrategy(masterIP, namespace, name, replicas);

        if (onlineStrategy == null) {
            LOGGER.error("produce onlineStrategy fialed!");
            return false;
        }

        LOGGER.debug("succeed produce onlineStrategy:" + onlineStrategy.toString());

        boolean strategyDeal = iStrategyDeal.dealOnlineStrategy(onlineStrategy);

        if (!strategyDeal) {
            LOGGER.error("deal onlineStrategy fialed!");
            return false;
        }
        return true;
    }


    /**
     * @return
     * @Author WANGYUZHONG
     * @Description //部署应用所有的组件
     * @Date 11:47 2019/4/9
     * @Param
     **/
    public boolean createResource(String masterIP, String port, String namespace, String yaml) {

        KubernetesClient client = K8sClient.createClient(masterIP, port, namespace);
        if (client == null) {
            LOGGER.error("client is null!");
        }
        LOGGER.debug("client create succeed!");
        return deal(masterIP, client, yaml);

    }

    public boolean deal(String masterIP, KubernetesClient client, String yaml) {
        JSONObject jsonObject = JSONObject.parseObject(yaml);

        String resouceKind = iResolveYamlFile.getResourceKind(jsonObject);

        switch (resouceKind) {
            case "Service":
                return creatServiceResource(masterIP, client, yaml);
            case "Deployment":
                return creatCustomResource(masterIP, client, yaml);
            case "StatefulSet":
                return creatCustomResource(masterIP, client, yaml);
            default:
                return creatOthersResource(client, yaml);
        }
    }

}
