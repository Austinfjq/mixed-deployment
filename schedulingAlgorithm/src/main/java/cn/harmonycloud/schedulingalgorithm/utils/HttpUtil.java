package cn.harmonycloud.schedulingalgorithm.utils;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    public static String get(String uri) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(uri);
            CloseableHttpResponse response = httpClient.execute(httpget);
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            return null;
        }
    }

    public static String post(String uri) {
        return post(uri, new HashMap<>());
    }

    public static String post(String uri, Map<String, String> parameters) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpost = new HttpPost(uri);
            List<NameValuePair> nvps = new ArrayList<>();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            CloseableHttpResponse response = httpClient.execute(httpost);
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            return null;
        }
    }
}
