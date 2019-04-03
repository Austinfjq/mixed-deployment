package cn.harmonycloud.schedulingalgorithm.affinity;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class NodeSelectorTerm {
    public static Gson gson = new Gson();

    private NodeSelectorRequirement[] matchExpressions;
    private NodeSelectorRequirement[] matchFields;
    private Map<String, Object> additionalProperties;

    public NodeSelectorRequirement[] getMatchExpressions() {
        return matchExpressions;
    }

    public void setMatchExpressions(NodeSelectorRequirement[] matchExpressions) {
        this.matchExpressions = matchExpressions;
    }

    public NodeSelectorRequirement[] getMatchFields() {
        // deal with: additionalProperties matchFields
        if (matchFields == null && additionalProperties != null) {
            try {
                matchFields = gson.fromJson(gson.toJson(additionalProperties.get("matchFields")), NodeSelectorRequirement[].class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return matchFields;
    }

    public void setMatchFields(NodeSelectorRequirement[] matchFields) {
        this.matchFields = matchFields;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
