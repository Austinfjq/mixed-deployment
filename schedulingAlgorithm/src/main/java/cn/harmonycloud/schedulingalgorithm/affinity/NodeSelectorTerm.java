package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.List;

public class NodeSelectorTerm {
    private NodeSelectorRequirement[] matchExpressions;
    private NodeSelectorRequirement[] matchFields;

    public NodeSelectorRequirement[] getMatchExpressions() {
        return matchExpressions;
    }

    public void setMatchExpressions(NodeSelectorRequirement[] matchExpressions) {
        this.matchExpressions = matchExpressions;
    }

    public NodeSelectorRequirement[] getMatchFields() {
        return matchFields;
    }

    public void setMatchFields(NodeSelectorRequirement[] matchFields) {
        this.matchFields = matchFields;
    }
}
