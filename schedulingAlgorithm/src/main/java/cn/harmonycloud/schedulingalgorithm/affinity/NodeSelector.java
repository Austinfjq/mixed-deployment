package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.List;

public class NodeSelector {
    private List<NodeSelectorTerm> NodeSelectorTerms;

    public List<NodeSelectorTerm> getNodeSelectorTerms() {
        return NodeSelectorTerms;
    }

    public void setNodeSelectorTerms(List<NodeSelectorTerm> nodeSelectorTerms) {
        NodeSelectorTerms = nodeSelectorTerms;
    }
}
