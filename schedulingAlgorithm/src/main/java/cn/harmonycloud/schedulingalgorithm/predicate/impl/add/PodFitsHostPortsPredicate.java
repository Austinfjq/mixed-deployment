package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.ContainerPort;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.ProtocolPort;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PodFitsHostPortsPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        ContainerPort[] wantPorts = pod.getWantPorts();
        Map<String, String> existingPorts = node.getUsedPorts();
        if (wantPorts != null) {
            for (ContainerPort wp : wantPorts) {
                if (checkConflict(existingPorts, wp.getHostIP(), wp.getProtocol(), wp.getHostPort())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * if there is any conflict, return true
     */
    private boolean checkConflict(Map<String, String> existingPorts, String ip, String protocol, int port) {
        if (port <= 0) {
            return false;
        }
        if (ip.isEmpty()) {
            ip = Constants.DEFAULT_BIND_ALL_HOST_IP;
        }
        if (protocol.isEmpty()) {
            protocol = Constants.PROTOCOL_TCP;
        }
        for (Map.Entry entry : existingPorts.entrySet()) {
            if (String.valueOf(port).equals(entry.getKey()) && protocol.equals(entry.getValue())) {
                return true;
            }
        }
        return false;
//        if (ip.equals(Constants.DEFAULT_BIND_ALL_HOST_IP)) {
//            for (List<ProtocolPort> protocolPortList : existingPorts.values()) {
//                for (ProtocolPort pp : protocolPortList) {
//                    if (protocol.equals(pp.getProtocol()) && port == pp.getPort()) {
//                        return true;
//                    }
//                }
//            }
//        } else {
//            for (ProtocolPort pp : existingPorts.get(ip)) {
//                if (protocol.equals(pp.getProtocol()) && port == pp.getPort()) {
//                    return true;
//                }
//            }
//            for (ProtocolPort pp : existingPorts.get(Constants.DEFAULT_BIND_ALL_HOST_IP)) {
//                if (protocol.equals(pp.getProtocol()) && port == pp.getPort()) {
//                    return true;
//                }
//            }
//        }
//        return false;
    }
}
