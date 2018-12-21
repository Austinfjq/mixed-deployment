package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.List;

public class NodeSelectorTerm {
    private List<NodeSelectorRequirement> matchExpressions;
    private List<NodeSelectorRequirement> matchFields;

    public List<NodeSelectorRequirement> getMatchExpressions() {
        return matchExpressions;
    }

    public void setMatchExpressions(List<NodeSelectorRequirement> matchExpressions) {
        this.matchExpressions = matchExpressions;
    }

    public List<NodeSelectorRequirement> getMatchFields() {
        return matchFields;
    }

    public void setMatchFields(List<NodeSelectorRequirement> matchFields) {
        this.matchFields = matchFields;
    }
}
