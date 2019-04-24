package cn.harmonycloud.datacenter.controller;

import cn.harmonycloud.datacenter.entity.es.PodData;
import cn.harmonycloud.datacenter.entity.es.SearchPod;
import cn.harmonycloud.datacenter.service.IPodDataService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:20
 *@Modify By:
 */

@RestController
public class PodDataController {

    @Autowired
    private IPodDataService podDataService;

    @PutMapping("/pod")
    public Map<String, Object> saveOnePodData(@RequestBody PodData podData){
        Map<String, Object> responseMap = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uuid = UUID.randomUUID().toString();
        podData.setId(uuid);
        podData.setTime(df.format(new Date()));
        podDataService.saveOnePodData(podData);

        Optional<PodData> podDataOptional = podDataService.findById(uuid);
        if(podDataOptional.isPresent()){
            responseMap.put("isSucceed", true);
        }else responseMap.put("isSucceed", false);

        return responseMap;
    }

    @PutMapping("/pods")
    public Map<String, Object> saveAllPodDatas(@RequestBody List<PodData> podDatas){
        Map<String, Object> responseMap = new HashMap<>();
        if(podDatas.size() > 0) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            for (PodData podData : podDatas) {
                podData.setId(UUID.randomUUID().toString());
                podData.setTime(time);
            }

            Iterable<PodData> podDataIterable = podDataService.saveAllPodDatas(podDatas);
            if(Lists.newArrayList(podDataIterable).size() == podDatas.size()){
                responseMap.put("isSucceed",true);
            }else{
                responseMap.put("isSucceed",false);
            }
        }else{
            responseMap.put("isSucceed",false);
        }

        return responseMap;
    }

    @GetMapping("/pods")
    public List<PodData> findAllPods(){
        List<PodData> pods = podDataService.findAllPodDatas();
        // TODO: 需修改实现
        for(PodData podData:pods){
            podData.changeLabels();
        }
        return pods;
    }

    /**
     * 获取当前pod数据
     *
     * @return
     */
    @GetMapping("/nowPod")
    public List<PodData> getNowServices(){
        List<PodData> pods = podDataService.getNowServices();
        // TODO: 需修改实现
        for(PodData podData:pods){
            podData.changeLabels();
        }
        return pods;
    }


    @GetMapping("/test")
    public void test(){
        PodData podData = new PodData();
        Map<String,String> label = new HashMap<>();
        label.put("app","xxx");

        podData.setLabels(label);
        podDataService.saveOnePodData(podData);

    }
    /**
     * 获取某个节点上某个service的所有Pod实例数
     *
     * @return
     */
    @GetMapping("/node/service/pods")
    public List<Map<String, String>> getNowServices(@RequestParam("clusterIp") String clusterIp, @RequestParam("namespace") String namespace
        , @RequestParam("serviceName") String serviceName, @RequestParam("hostName") String hostName){
    List<Map<String, String>> map=new ArrayList<Map<String, String>>();
    SearchPod pdd=new SearchPod();
    pdd.setClusterIp(clusterIp);
    pdd.setNamespace(namespace);
    pdd.setServiceName(serviceName);
    pdd.setHostName(hostName);
    List<PodData> pod=podDataService.getNowServices();
    for(PodData pd:pod)
    {
        Map<String, String> responseMap = new HashMap<>();
        //System.out.println(pd.toString()+ "\n");
        if(clusterIp.equals(pd.getClusterMasterIP())&&
                namespace.equals(pd.getNamespace())&&serviceName.equals(pd.getServiceName())
        &&hostName.equals(pd.getNodeName()))
        {
            responseMap.put("podName",pd.getPodName());
            map.add(responseMap);
        }
    }
    return map;
    }
    /**
     * 获取某个服务正在运行的Pod实例数
     *
     * @return
     */
    @GetMapping("/service/podNums")
    public Map<String, Integer> getServicePodNums(@RequestParam("clusterIp") String clusterIp, @RequestParam("namespace") String namespace
            , @RequestParam("serviceName") String serviceName){
        Map<String, Integer> responseMap = new HashMap<>();
        SearchPod pdd=new SearchPod();
        pdd.setClusterIp(clusterIp);
        pdd.setNamespace(namespace);
        pdd.setServiceName(serviceName);
        List<PodData> pod=podDataService.getNowServices();
        Integer ins=0;
        for(PodData pd:pod) {
            if(clusterIp.equals(pd.getClusterMasterIP())&&
                    namespace.equals(pd.getNamespace())&&serviceName.equals(pd.getServiceName())) {
                ins++;
            }
        }
        responseMap.put("podNums",ins);
        return responseMap;
    }
    /**
     * 获取某个服务在一定请求量下所需的Pod实例数
     *
     * @return
     */
    @GetMapping("/service/requestPodNums")
    public Map<String, Integer> getServiceRequestPodNums(@RequestParam("clusterIp") String clusterIp, @RequestParam("namespace") String namespace
            , @RequestParam("serviceName") String serviceName, @RequestParam("requestNums") Double requestNums){
        Map<String, Integer> responseMap = new HashMap<>();
        SearchPod pdd=new SearchPod();
        pdd.setClusterIp(clusterIp);
        pdd.setNamespace(namespace);
        pdd.setServiceName(serviceName);
        pdd.setRequestNums(requestNums);
        List<PodData> pod=podDataService.getNowServices();
        Integer ins=0;
        for(PodData pd:pod) {
            if(clusterIp.equals(  pd.getClusterMasterIP())&&requestNums==pd.getResponseBytes()&&
                    namespace.equals(pd.getNamespace())&&serviceName.equals(pd.getServiceName())) {
                ins++;
            }
        }
        responseMap.put("podNums",ins);
        return responseMap;
    }
}
