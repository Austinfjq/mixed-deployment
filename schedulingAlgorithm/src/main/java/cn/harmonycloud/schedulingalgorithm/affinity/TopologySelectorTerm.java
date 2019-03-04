package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.List;

public class TopologySelectorTerm {
    private List<TopologySelectorLabelRequirement> matchLabelExpressions;

    public List<TopologySelectorLabelRequirement> getMatchLabelExpressions() {
        return matchLabelExpressions;
    }

    public void setMatchLabelExpressions(List<TopologySelectorLabelRequirement> matchLabelExpressions) {
        this.matchLabelExpressions = matchLabelExpressions;
    }
}
