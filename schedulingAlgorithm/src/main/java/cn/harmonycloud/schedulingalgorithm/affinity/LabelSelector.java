package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.List;
import java.util.Map;

public class LabelSelector {
    private Map<String, String> matchLabels;
    private List<LabelSelectorRequirement> MatchExpressions;

    public Map<String, String> getMatchLabels() {
        return matchLabels;
    }

    public void setMatchLabels(Map<String, String> matchLabels) {
        this.matchLabels = matchLabels;
    }

    public List<LabelSelectorRequirement> getMatchExpressions() {
        return MatchExpressions;
    }

    public void setMatchExpressions(List<LabelSelectorRequirement> matchExpressions) {
        MatchExpressions = matchExpressions;
    }
}
