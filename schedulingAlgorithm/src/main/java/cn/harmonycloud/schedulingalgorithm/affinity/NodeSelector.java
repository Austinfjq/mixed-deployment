package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.List;

public class NodeSelector {
    private NodeSelectorTerm[] NodeSelectorTerms;

    public NodeSelectorTerm[] getNodeSelectorTerms() {
        return NodeSelectorTerms;
    }

    public void setNodeSelectorTerms(NodeSelectorTerm[] nodeSelectorTerms) {
        NodeSelectorTerms = nodeSelectorTerms;
    }
}
