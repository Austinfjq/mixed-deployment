package cn.harmonycloud.schedulingalgorithm.constant;

public class URIs {
    public static final String URI_GET_SERVICE = "http://10.10.101.115:8080/nowService";
    public static final String URI_GET_POD = "http://10.10.101.115:8080/nowPod";
    public static final String URI_GET_NODE = "http://10.10.101.115:8080/nowNode";
    public static final String URI_GET_NODE_FORECAST = "http://10.10.101.115:8080/forecast/forecastValues";

    public static final String URI_GET_POD_CONSUME = "http://hostname:port/service/getPodCosume";
    public static final String URI_GET_SERVICE_TYPE = "http://hostname:port/service/getServiceType";

    public static final String URI_EXECUTE_ADD = "http://10.10.101.115:8082/dispatching/createPod";
    public static final String URI_EXECUTE_REMOVE = "http://10.10.101.115:8082/dispatching/deletePod";
}
