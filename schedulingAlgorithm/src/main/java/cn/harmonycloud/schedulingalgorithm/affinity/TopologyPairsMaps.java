package cn.harmonycloud.schedulingalgorithm.affinity;

import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TopologyPairsMaps {
    private Map<TopologyPair, Set<Pod>> topologyPairToPods;
    private Map<String, Set<TopologyPair>> podToTopologyPairs;

    public Map<TopologyPair, Set<Pod>> getTopologyPairToPods() {
        return topologyPairToPods;
    }

    public void setTopologyPairToPods(Map<TopologyPair, Set<Pod>> topologyPairToPods) {
        this.topologyPairToPods = topologyPairToPods;
    }

    public Map<String, Set<TopologyPair>> getPodToTopologyPairs() {
        return podToTopologyPairs;
    }

    public void setPodToTopologyPairs(Map<String, Set<TopologyPair>> podToTopologyPairs) {
        this.podToTopologyPairs = podToTopologyPairs;
    }

    public void addTopologyPair(TopologyPair pair, Pod pod) {
        String podFullName = DOUtils.getPodFullName(pod);
        if (!this.topologyPairToPods.containsKey(pair)) {
            this.topologyPairToPods.put(pair, new HashSet<>());
        }
        this.topologyPairToPods.get(pair).add(pod);
        if (!this.podToTopologyPairs.containsKey(podFullName)) {
            this.podToTopologyPairs.put(podFullName, new HashSet<>());
        }
        this.podToTopologyPairs.get(podFullName).add(pair);
    }

    public void appendMaps(TopologyPairsMaps toAppend) {
        if (toAppend == null) {
            return;
        }
        for (TopologyPair pair: toAppend.topologyPairToPods.keySet()) {
            for (Pod pod : toAppend.topologyPairToPods.get(pair)) {
                this.addTopologyPair(pair, pod);
            }
        }
    }
}
