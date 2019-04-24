package cn.harmonycloud.DAO.imp;

import cn.harmonycloud.DAO.IllHealthyDealDAO;
import cn.harmonycloud.tools.HttpClientResult;
import cn.harmonycloud.tools.HttpClientUtils;
import cn.harmonycloud.tools.PropertyFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @classname：RegulateIllHealthServiceimp
 * @author：WANGYUZHONG
 * @date：2019/4/15 9:03
 * @description:TODO
 * @version:1.0
 **/
public class RegulateIllHealthDaoImp implements IllHealthyDealDAO {

    private final static Logger LOGGER = LoggerFactory.getLogger(RegulateIllHealthDaoImp.class);

    @Override
    public boolean regulateIllHealthService(String masterIP, String namespace, String serviceName) {
        Map<String,String> params = new HashMap<>();
        params.put("masterIP", masterIP);
        params.put("namespace", namespace);
        params.put("serviceName", serviceName);
        String url = "http://"+ PropertyFileUtil.getValue("RegulateControllerHostIP") + ":" + PropertyFileUtil.getValue("RegulateControllerPort") + "/illHealthService";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doPost(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("ill health service deal failed!");
            return false;
        }
        return Boolean.valueOf(httpClientResult.getContent());
    }

    @Override
    public boolean regulateIllHealthNode(String masterIP, String hostName) {
        Map<String,String> params = new HashMap<>();
        params.put("masterIP", masterIP);
        params.put("hostName", hostName);
        String url = "http://"+ PropertyFileUtil.getValue("RegulateControllerHostIP") + ":" + PropertyFileUtil.getValue("RegulateControllerPort") + "/illHealthNode";
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult =  HttpClientUtils.doPost(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == httpClientResult || httpClientResult.getCode() != 200) {
            LOGGER.error("ill health node deal failed!");
            return false;
        }
        return Boolean.valueOf(httpClientResult.getContent());
    }
}
