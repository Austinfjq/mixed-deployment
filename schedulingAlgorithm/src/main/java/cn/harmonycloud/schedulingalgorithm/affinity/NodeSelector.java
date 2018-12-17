package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NodeSelector {
    private List<Requirement> requirements;

    public NodeSelector(Map<String, String> nodeSelectorMap) {
        requirements = new ArrayList<>();
        if (nodeSelectorMap == null || nodeSelectorMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : nodeSelectorMap.entrySet()) {
            String label = entry.getKey();
            String value = entry.getValue();
            Requirement r = new Requirement();
            r.setKey(label);
            r.setOperator(SelectOperation.Equals);
            r.setStrValues(new String[]{value});
            requirements.add(r);
        }
        requirements.sort((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getKey(), b.getKey()));
    }

    public boolean matches(Map<String, String> labels) {
        for (Requirement r : requirements) {
            if (!matches(r, labels)) {
                return false;
            }
        }
        return true;
    }

    private boolean matches(Requirement r, Map<String, String> ls) {
        switch (r.getOperator()) {
            case In:
            case Equals:
            case DoubleEquals:
                if (!ls.containsKey(r.getKey())) {
                    return false;
                }
                return hasValue(r, ls.get(r.getKey()));
            case NotIn:
            case NotEquals:
                if (!ls.containsKey(r.getKey())) {
                    return true;
                }
                return !hasValue(r, ls.get(r.getKey()));
            case Exists:
                return ls.containsKey(r.getKey());
            case DoesNotExist:
                return !ls.containsKey(r.getKey());
            case GreaterThan:
            case LessThan:
                if (!ls.containsKey(r.getKey())) {
                    return false;
                }
                int lsValue = Integer.valueOf(ls.get(r.getKey()));
                if (r.getStrValues().length != 1) {
                    return false;
                }
                int rValue = Integer.valueOf(r.getStrValues()[0]);
                return (r.getOperator() == SelectOperation.GreaterThan && lsValue > rValue)
                        || (r.getOperator() == SelectOperation.LessThan && lsValue < rValue);
            default:
                return false;
        }
    }

    private boolean hasValue(Requirement r, String value) {
        for (String s : r.getStrValues()) {
            if (value.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
