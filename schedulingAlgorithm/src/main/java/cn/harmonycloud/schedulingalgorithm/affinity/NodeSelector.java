package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.List;

public class NodeSelector {
    private NodeSelectorTerm[] nodeSelectorTerms;

    public NodeSelectorTerm[] getNodeSelectorTerms() {
        return nodeSelectorTerms;
    }

    public void setNodeSelectorTerms(NodeSelectorTerm[] nodeSelectorTerms) {
        this.nodeSelectorTerms = nodeSelectorTerms;
    }
}
