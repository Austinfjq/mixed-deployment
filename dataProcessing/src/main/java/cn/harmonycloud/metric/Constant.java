package cn.harmonycloud.metric;

import cn.harmonycloud.tools.LoadConfig;

import java.io.InputStream;

public class Constant {
    public static String K8S_MASTER_URL;
    public static String K8S_MASTER;
    public static String URL_HOST;
    public static String URL_PORT;
    public static String STEP_TIME;
    public static String PROMETHEUS_HOST;
    public static String PROMETHEUS_PORT;
    public static String PROMETHEUS_NODE_CONFIG_PATH;
    public static String PROMETHEUS_POD_CONFIG_PATH;
    public static String PROMETHEUS_SERVICE_CONFIG_PATH;

    static {
        try {
//            String propertiesFilePath = "dataProcessing.properties";

            String propertiesFilePath = "/usr/local/data-processing/config/dataProcessing.properties";
            LoadConfig.load(Constant.class, propertiesFilePath);
        } catch (Exception e) {
            System.out.println("Properties file error!");
        }
    }

    public static void main(String[] args) {
        System.out.println(Constant.K8S_MASTER_URL);
    }
}
