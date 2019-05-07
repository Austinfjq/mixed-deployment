package cn.harmonycloud.tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @classname：PropertyFileUtil
 * @author：WANGYUZHONG
 * @date：2019/4/15 8:25
 * @description:TODO
 * @version:1.0
 **/
public class PropertyFileUtil {

    private static final String PROPERRY_FILE_PATH = "/root/mix-deploy/mixed-deployment/loadForecasting/src/main/resource/application.properties";

    /**
     * @return
     * @Author WANGYUZHONG
     * @Description //从Property配置文件中获取某个key对应的值
     * @Date 8:27 2019/4/15
     * @Param
     **/
    public static String getValue(String key) {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            //读取属性文件application.properties
            in = new BufferedInputStream(new FileInputStream(PROPERRY_FILE_PATH));
            prop.load(in);
            return prop.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
