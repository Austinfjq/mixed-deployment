package cn.harmonycloud.schedulingalgorithm.constant;

public class Constants {
    public static final int OPERATION_ADD = 1;
    public static final int OPERATION_DELETE = 2;

    public static final int INTENSIVE_TYPE_CPU = 1;
    public static final int INTENSIVE_TYPE_MEMORY = 2;
    public static final int INTENSIVE_TYPE_DOWN_NET_IO = 3;
    public static final int INTENSIVE_TYPE_UP_NET_IO = 4;

    public static final String URI_GET_SERVICE = "http://10.10.101.115:8080/nowService";
    public static final String URI_GET_POD = "http://10.10.101.115:8080/nowPod";
    public static final String URI_GET_NODE = "http://10.10.101.115:8080/nowNode";
    public static final String URI_GET_NODE_FORECAST = "http://10.10.101.115:8080/forecast/forecastValues";

    public static final String URI_GET_POD_CONSUME = "http://hostname:port/service/getPodCosume";
    public static final String URI_GET_SERVICE_TYPE = "http://hostname:port/service/getServiceType";

    public static final String URI_EXECUTE_ADD = "http://192.168.1.20:8080/dispatching/createPod";
    public static final String URI_EXECUTE_REMOVE = "http://192.168.1.20:8080/dispatching/deletePod";

    public static final String DEFAULT_BIND_ALL_HOST_IP = "0.0.0.0";
    public static final String PROTOCOL_TCP = "TCP";
    public static final String PROTOCOL_UDP = "UDP";
    public static final String PROTOCOL_SCTP = "SCTP";

    public static final int PRIORITY_MAX_SCORE = 10;
    /**
     * filter 的计算比例，
     */
    public static final double FILTER_PERCENTAGE = 1.0D;

    public static final boolean LOG_PRIORITY_RESULT = true;
}
