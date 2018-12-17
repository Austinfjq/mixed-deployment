package cn.harmonycloud.bean;

import java.util.HashMap;
import java.util.Map;

public class Rule {
    private String ownType;
    private String ownName;
    private String namespace;
    private int replicas;
    private Map<String,Integer> nodeList = new HashMap<>();//ip->score
}
