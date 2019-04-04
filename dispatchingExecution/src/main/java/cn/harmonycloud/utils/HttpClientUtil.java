package cn.harmonycloud.utils;

import cn.harmonycloud.config.Config;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hc on 19-1-15.
 */
public class HttpClientUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
    private static String URL = StringUtil.combineUrl(Config.DB_SERVER+":8080/management");

    public static String httpGet(Map<String,String> paramMap)  {
        List<NameValuePair> paramList = new ArrayList<>();
        for (Map.Entry<String,String> entry : paramMap.entrySet()){
            paramList.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        URIBuilder uriBuilder = null;
        HttpGet httpget = null;
        String result = null;
        try {
            uriBuilder = new URIBuilder(URL);
            uriBuilder.setParameters(paramList);
//            System.out.println("uri:"+uriBuilder.build().toString());
            httpget = new HttpGet(uriBuilder.build());
            response = httpClient.execute(httpget);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity,"utf-8");
        } catch (ClientProtocolException e) {
            LOGGER.debug("Protocal is not available!");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            LOGGER.debug("URI["+URL+"] syntax is error!");
            e.printStackTrace();
        }catch (IOException e) {
            LOGGER.debug("There is something wrong with IO");
            e.printStackTrace();
        }finally {
            //close
            if (null != response){
                try {
                    response.close();
                    httpClient.close();
                } catch (IOException e) {
                    LOGGER.debug("Close connection ERROR!");
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

}
