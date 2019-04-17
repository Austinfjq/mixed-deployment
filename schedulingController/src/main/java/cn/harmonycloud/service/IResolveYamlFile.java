package cn.harmonycloud.service;

import cn.harmonycloud.beans.Service;
import com.alibaba.fastjson.JSONObject;

public interface IResolveYamlFile {

    /**
     * @Author WANGYUZHONG
     * @Description //解析yaml文件，获取资源类型
     * @Date 17:35 2019/4/9
     * @Param
     * @return
     **/
    String getResourceKind(JSONObject jsonObject);

    /**
     * @Author WANGYUZHONG
     * @Description //解析yaml文件为service
     * @Date 18:42 2019/4/9
     * @Param 
     * @return 
     **/
    int getReplicas(JSONObject jsonObject);

    /**
     * @Author WANGYUZHONG
     * @Description //解析yaml文件为service
     * @Date 18:42 2019/4/9
     * @Param
     * @return
     **/
    JSONObject changeReplicas(JSONObject jsonObject);

    /**
     * @Author WANGYUZHONG
     * @Description //解析deployment，或者statefulSet的namespace
     * @Date 18:42 2019/4/9
     * @Param
     * @return
     **/
    String getCustomResourceNamespace(JSONObject jsonObject);

    /**
     * @Author WANGYUZHONG
     * @Description //解析deployment，或者statefulSet的name
     * @Date 18:42 2019/4/9
     * @Param
     * @return
     **/
    String getCustomResourceName(JSONObject jsonObject);

    /**
     * @Author WANGYUZHONG
     * @Description //从yaml文件中解析出service的信息
     * @Date 16:36 2019/4/10
     * @Param
     * @return
     **/
    Service resolveService(String masterIP, JSONObject jsonObject);
}
