package cn.harmonycloud.schedulingalgorithm.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalSetting {
    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalSetting.class);

    /**
     * 优选最高分
     */
    public static int PRIORITY_MAX_SCORE;
    /**
     * filter 的计算比例
     */
    public static double FILTER_PERCENTAGE;
    /**
     * 是否开启
     */
    public static boolean LOG_DETAIL;
    /**
     * 并行优化
     */
    public static boolean PARALLEL;

    /**
     * URI
     */
    public static String URI_GET_SERVICE;
    public static String URI_GET_POD;
    public static String URI_GET_NODE;
    public static String URI_GET_NODE_FORECAST;
    public static String URI_GET_POD_CONSUME;
    public static String URI_GET_SERVICE_TYPE;
    public static String URI_EXECUTE_ADD;
    public static String URI_EXECUTE_REMOVE;

    static {
        try {
            // 生产环境运行配置
            String propertiesFilePath = "/usr/local/scheduling-algorithm/config/schedulingAlgorithm.properties";
            // 本地运行配置
//            propertiesFilePath = "schedulingAlgorithm/src/main/resources/schedulingAlgorithm.properties.default";
//            propertiesFilePath = "/root/schedulingAlgorithm.properties";
            ConfigLoader.load(GlobalSetting.class, propertiesFilePath);
        } catch (Exception e) {
            LOGGER.error("Properties file error!", e);
        }
    }
}
