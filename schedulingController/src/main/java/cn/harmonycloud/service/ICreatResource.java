package cn.harmonycloud.service;

import io.fabric8.kubernetes.client.KubernetesClient;


public interface ICreatResource {


    /**
     * @Author WANGYUZHONG
     * @Description //创建应用的其他资源对象
     * @Date 10:43 2019/4/9
     * @Param
     * @return 创建结果状态码 200表示成功
     **/
    boolean creatOthersResource(KubernetesClient client, String yaml);

    /**
     * @Author WANGYUZHONG
     * @Description //创建应用的service
     * @Date 10:43 2019/4/9
     * @Param
     * @return 创建结果状态码 200表示成功
     **/
    boolean creatServiceResource(String masterIP, KubernetesClient client, String yaml);

    /**
     * @Author WANGYUZHONG
     * @Description //创建应用的deployment,statfulset
     * @Date 10:43 2019/4/9
     * @Param
     * @return 创建结果状态码 200表示成功
     **/
    boolean creatCustomResource(String masterIP, KubernetesClient client, String yaml);


    /**
     * @Author WANGYUZHONG
     * @Description //部署应用所有的组件
     * @Date 11:47 2019/4/9
     * @Param
     * @return
     **/
    boolean createResource(String masterIP, String port, String namespace, String yaml);

}
