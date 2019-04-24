package cn.harmonycloud.service.serviceImp;

import cn.harmonycloud.beans.Service;
import cn.harmonycloud.service.IResolveYamlFile;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Map;

/**
 * @classname：ResolveYamlFileServiceImp
 * @author：WANGYUZHONG
 * @date：2019/4/9 17:39
 * @description:TODO
 * @version:1.0
 **/

@org.springframework.stereotype.Service
public class ResolveYamlFileServiceImp implements IResolveYamlFile {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResolveYamlFileServiceImp.class);

    public String getResourceKind(JSONObject jsonObject) {
        if (jsonObject == null) {
            LOGGER.error("json object is null!");
            return null;
        }

        return jsonObject.getString("kind");
    }

    public JSONObject changeReplicas(JSONObject jsonObject) {
        if (jsonObject == null) {
            LOGGER.error("json object is null!");
        }
        JSONObject specJsonObject = jsonObject.getJSONObject("spec");
        specJsonObject.put("replicas",0);
        jsonObject.put("spec", specJsonObject);
        return jsonObject;
    }

    @Override
    public String getCustomResourceNamespace(JSONObject jsonObject) {
        if (jsonObject == null) {
            LOGGER.error("json object is null!");
        }

        JSONObject metaDataJsObject = jsonObject.getJSONObject("metadata");

        String namespace = metaDataJsObject.getString("namespace");

        return namespace;
    }

    @Override
    public String getCustomResourceName(JSONObject jsonObject) {
        if (jsonObject == null) {
            LOGGER.error("json object is null!");
        }

        JSONObject metaDataJsObject = jsonObject.getJSONObject("metadata");

        String name = metaDataJsObject.getString("name");

        return name;
    }

    @Override
    public Service resolveService(String masterIP, JSONObject jsonObject) {
        if (jsonObject == null) {
            LOGGER.error("json object is null!");
            return null;
        }

        JSONObject metaDataJsObject = jsonObject.getJSONObject("metadata");

        String namespace = metaDataJsObject.getString("namespace");
        String serviceName = metaDataJsObject.getString("name");
        int type = metaDataJsObject.getIntValue("type");

        Service service = new Service();
        service.setMasterIp(masterIP);
        service.setNamespace(namespace);
        service.setServiceName(serviceName);
        service.setServiceType(type);

        LOGGER.debug("service reslove succeed!service=" + service.toString());
        return service;
    }

    /**
     * @Author WANGYUZHONG
     * @Description //解析yaml文件为service
     * @Date 18:42 2019/4/9
     * @Param
     * @return
     **/
    public int getReplicas(JSONObject jsonObject) {
        if (jsonObject == null) {
            LOGGER.error("json object is null!");
        }
        int replicas = jsonObject.getJSONObject("spec").getInteger("replicas");
        return replicas;
    }

    public static Map<String,Object> createYaml(String jsonString) throws JsonProcessingException, IOException {
        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
        String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);

        Yaml yaml = new Yaml();
        Map<String,Object> map = (Map<String, Object>) yaml.load(jsonAsYaml);
        return map;
    }

}
