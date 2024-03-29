package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.ContainerPort;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;

import java.util.Map;

public class PodFitsHostPortsPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        ContainerPort[] wantPorts = pod.getWantPorts();
        Map<String, String> existingPorts = node.getUsedPorts();
        if (wantPorts != null) {
            for (ContainerPort wp : wantPorts) {
                if (wp != null && checkConflict(existingPorts, wp.getHostIP(), wp.getProtocol(), wp.getHostPort())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * if there is any conflict, return true
     */
    private boolean checkConflict(Map<String, String> existingPorts, String ip, String protocol, Integer port) {
        if (port == null || port <= 0) {
            return false;
        }
        if (ip == null || ip.isEmpty()) {
            ip = Constants.DEFAULT_BIND_ALL_HOST_IP;
        }
        if (protocol == null || protocol.isEmpty()) {
            protocol = Constants.PROTOCOL_TCP;
        }
        // assume: one node has only one hostIP.
        for (Map.Entry<String, String> entry : existingPorts.entrySet()) {
            if (String.valueOf(port).equals(entry.getKey()) && protocol.equals(entry.getValue())) {
                return true;
            }
        }
        return false;

        // if one node may have several hostIPs, use codes below:

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
