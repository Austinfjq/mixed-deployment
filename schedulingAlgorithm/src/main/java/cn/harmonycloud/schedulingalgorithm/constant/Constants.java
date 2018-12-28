package cn.harmonycloud.schedulingalgorithm.constant;

public class Constants {
    public static int OPERATION_ADD = 1;
    public static int OPERATION_DELETE = 2;

    public static int INTENSIVE_TYPE_CPU = 1;
    public static int INTENSIVE_TYPE_MEMORY = 2;
    public static int INTENSIVE_TYPE_DOWN_NET_IO = 3;
    public static int INTENSIVE_TYPE_UP_NET_IO = 4;

    public static String URI_GET_SERVICE = "http://hostname:port/getService";
    public static String URI_GET_POD = "http://hostname:port/getPod";
    public static String URI_GET_NODE = "http://hostname:port/getNode";

    public static String URI_GET_POD_CONSUME = "http://hostname:port/service/getPodCosume";
    public static String URI_GET_SERVICE_TYPE = "http://hostname:port/service/getServiceType";

    public static String URI_EXECUTE_ADD = "POST /pods?JsonArray=?";
    public static String URI_EXECUTE_REMOVE = "DELETE /pods?JsonArray=?";

    public static String DEFAULT_BIND_ALL_HOST_IP = "0.0.0.0";
    public static String PROTOCOL_TCP = "TCP";
    public static String PROTOCOL_UDP = "UDP";
    public static String PROTOCOL_SCTP = "SCTP";

    public static int PRIORITY_MAX_SCORE = 10;
}
