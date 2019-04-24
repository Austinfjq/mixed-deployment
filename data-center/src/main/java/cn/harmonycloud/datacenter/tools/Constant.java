package cn.harmonycloud.datacenter.tools;

public class Constant {
//    public static final String MASTER = "https://10.10.102.25:6443/";
//    public static final String HOST="127.0.0.1";   //10.10.102.25
//    public static final String PORT="8080";
//    public static final String StepTime="300000"; //毫秒
////    public static final String StepTime="1000"; //毫秒

    public static final String SERVICE_INDEX="service";
    public static final String SERVICE_TYPE="serviceData";
    public static final String NODE_INDEX="node";
    public static final String NODE_TYPE="nodeData";
    public static final String POD_INDEX="pod";
    public static final String POD_TYPE="podData";
    public static final String SCHEDULE_POD_INDEX="schedulepod";
    public static final String SCHEDULE_POD_TYPE="schedulePodData";
    public static final String FORECAST_RESULT_CELL_INDEX="forecast";
    public static final String FORECAST_RESULT_CELL_TYPE="forecastResultCell";
    public static final String SEARCH_POD_INDEX="searchPod";
    public static final String SEARCH_POD_TYPE="searchPod";

    //for test
    public static final String RESULT_NODE_INDEX="testnode";
    public static final String RESULT_NODE_TYPE="resultNode";
    public static final String RESULT_POD_INDEX="testpod";
    public static final String RESULT_POD_TYPE="resultPod";

    public static final int TIME_INTERVAL = 5 * 60;//单位秒

    public static final String K8S_MASTER = "https://10.10.102.25:6443/";
}
