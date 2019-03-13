package cn.harmonycloud.schedulingalgorithm.constant;

public class Constants {
    public static final int OPERATION_ADD = 1;
    public static final int OPERATION_DELETE = 2;

    public static final int INTENSIVE_TYPE_CPU = 1;
    public static final int INTENSIVE_TYPE_MEMORY = 2;
    public static final int INTENSIVE_TYPE_DOWN_NET_IO = 3;
    public static final int INTENSIVE_TYPE_UP_NET_IO = 4;

    public static final String DEFAULT_BIND_ALL_HOST_IP = "0.0.0.0";
    public static final String PROTOCOL_TCP = "TCP";
    public static final String PROTOCOL_UDP = "UDP";
    public static final String PROTOCOL_SCTP = "SCTP";
}
