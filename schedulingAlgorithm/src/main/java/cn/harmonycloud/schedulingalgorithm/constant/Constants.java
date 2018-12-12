package cn.harmonycloud.schedulingalgorithm.constant;

public class Constants {
    public static int INTENSIVE_TYPE_CPU = 1;
    public static int INTENSIVE_TYPE_MEMORY = 2;
    public static int INTENSIVE_TYPE_DOWN_NET_IO = 3;
    public static int INTENSIVE_TYPE_UP_NET_IO = 4;

    public static String URI_GET_SERVICE = "http://hostname:port/getService";
    public static String URI_GET_POD = "http://hostname:port/getPod";
    public static String URI_GET_NODE = "http://hostname:port/getNode";

    public static String URI_GET_POD_CONSUME = "http://hostname:port/service/getPodCosume";
    public static String URI_GET_SERVICE_TYPE = "http://hostname:port/service/getServiceType";
}
