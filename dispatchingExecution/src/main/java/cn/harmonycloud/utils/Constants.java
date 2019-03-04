package cn.harmonycloud.utils;

public class Constants {
    //Thread pool
    public static int THREAD_POOL_NUMBER  = 30;
    //kubernets 集群master的ip
    public static String MASTER = "https://10.10.102.25:6443/";
    //pod名称的随机字符长度
    public static int LENGTH = 5;
    //namespace
    public static String DEFAULT_NAME_SPACE = "default";

    //Rule
<<<<<<< HEAD
    public final static String API_VERSION = "crd.k8s.io/v1";
    public final static String KIND = "Rule";
    public final static String NAME_PREFIX = "rule-";

    //RuleDefinition
    public final static String RULE_API_VERSION = "apiextensions.k8s.io/v1beta1";
    public final static String RULE_KIND = "CustomResourceDefinition";
    public final static String RULE_NAME = "rules.crd.k8s.io";

=======
    public static String API_VERSION = "crd.k8s.io/v1";
    public static String KIND = "Rule";
    public static String NAME_PREFIX = "rule-";

    //RuleDefinition
    public static String RULE_API_VERSION = "apiextensions.k8s.io/v1beta1";
    public static String RULE_KIND = "CustomResourceDefinition";
    public static String RULE_NAME = "rules.crd.k8s.io";
>>>>>>> 3136146d1eeb88331b20a0ab8a05b5127c68b61d
}
