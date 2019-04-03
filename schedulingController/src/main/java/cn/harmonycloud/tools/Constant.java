package cn.harmonycloud.tools;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Constant {
    public static double CpuUsageMaxThreshold;
    public static double CpuUsageMinThreshold;
    public static double MemUsageMaxThreshold;
    public static double MemUsageMinThreshold;

    public static String URL_HOST;
    public static String URL_PORT;
    public static String URL_PORT2;
    public static String K8S_MASTER;

    public static String CONFIG_FIME_PATH;

    static {
        try {
//            String propertiesFilePath = "C:/Users/chang/Downloads/CO-LOCATION/mixed-deployment/schedulingController/src/main/resources/schedulingController.properties";
            String propertiesFilePath = "/usr/local/scheduling-controller/config/schedulingController.properties";
            LoadConstant.load(Constant.class,propertiesFilePath);
        } catch (Exception e) {
            System.out.println("Properties file error!");
        }
    }

    public static void main(String[] args) {
        System.out.println(Constant.K8S_MASTER);
    }


}
