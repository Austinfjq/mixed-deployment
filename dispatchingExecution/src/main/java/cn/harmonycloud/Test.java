package cn.harmonycloud;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hc on 19-1-16.
 */
public class Test {
    public static void main(String[] args){
//        String json ="{'namespace':'wy','servicename':'nginx-service','nodeList':'',}";
//        JSONObject jsonObject = JSON.parseObject(json);
//        List<NameValuePair> paramList = new ArrayList<>();
//        paramList.add(new BasicNameValuePair("namespace","wy"));
//        paramList.add(new BasicNameValuePair("servicename","nginx-service"));
//        paramList.add(new BasicNameValuePair("nodeList",""));
        JSONArray nodes = new JSONArray();
        String node1 = "{'hostname':'10.10.103.25-master','score':'3'}";
        String node2 = "{'hostname':'10.10.103.28-build','score':'4'}";
        nodes.add(JSONObject.parseObject(node1));
        nodes.add(JSONObject.parseObject(node2));
//        URLEncodedUtils.format();
        String nodeList = nodes.toString();
        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("nodeList",nodeList));
        System.out.println(URLEncodedUtils.format(paramList,"utf-8"));
        System.out.println(nodes);
    }


}
