package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.List;
import java.util.Map;

public class LabelSelector {
    private Map<String, String> matchLabels;
    private LabelSelectorRequirement[] MatchExpressions;

    public Map<String, String> getMatchLabels() {
        return matchLabels;
    }

    public void setMatchLabels(Map<String, String> matchLabels) {
        this.matchLabels = matchLabels;
    }

    public LabelSelectorRequirement[] getMatchExpressions() {
        return MatchExpressions;
    }

    public void setMatchExpressions(LabelSelectorRequirement[] matchExpressions) {
        MatchExpressions = matchExpressions;
    }

    /**
     * unused properpy
     */
    private Object additionalProperties;

    public Object getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Object additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
