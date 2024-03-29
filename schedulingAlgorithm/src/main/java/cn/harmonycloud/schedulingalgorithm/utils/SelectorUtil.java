package cn.harmonycloud.schedulingalgorithm.utils;

import cn.harmonycloud.schedulingalgorithm.affinity.InternalSelector;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelectorOperator;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelectorRequirement;
import cn.harmonycloud.schedulingalgorithm.affinity.Requirement;
import cn.harmonycloud.schedulingalgorithm.affinity.SelectOperation;
import cn.harmonycloud.schedulingalgorithm.affinity.Selector;

import java.util.Arrays;
import java.util.List;

public class SelectorUtil {
    public static Selector nodeSelectorRequirementsAsSelector(List<NodeSelectorRequirement> nsm) {
        if (nsm == null || nsm.isEmpty()) {
            return null;
        }
        Selector selector = new InternalSelector();
        for (NodeSelectorRequirement expr : nsm) {
            SelectOperation op = null;
//            if (NodeSelectorOperator.NodeSelectorOpIn.equals(expr.getOperator())) {
//                op = SelectOperation.In;
//            } else if (NodeSelectorOperator.NodeSelectorOpNotIn.equals(expr.getOperator())) {
//                op = SelectOperation.NotIn;
//            } else if (NodeSelectorOperator.NodeSelectorOpExists.equals(expr.getOperator())) {
//                op = SelectOperation.Exists;
//            } else if (NodeSelectorOperator.NodeSelectorOpDoesNotExist.equals(expr.getOperator())) {
//                op = SelectOperation.DoesNotExist;
//            } else if (NodeSelectorOperator.NodeSelectorOpGt.equals(expr.getOperator())) {
//                op = SelectOperation.GreaterThan;
//            } else if (NodeSelectorOperator.NodeSelectorOpLt.equals(expr.getOperator())) {
//                op = SelectOperation.LessThan;
//            }
            switch (expr.getOperatorObject()) {
                case NodeSelectorOpIn:
                    op = SelectOperation.In;
                    break;
                case NodeSelectorOpNotIn:
                    op = SelectOperation.NotIn;
                    break;
                case NodeSelectorOpExists:
                    op = SelectOperation.Exists;
                    break;
                case NodeSelectorOpDoesNotExist:
                    op = SelectOperation.DoesNotExist;
                    break;
                case NodeSelectorOpGt:
                    op = SelectOperation.GreaterThan;
                    break;
                case NodeSelectorOpLt:
                    op = SelectOperation.LessThan;
                    break;
                default:
                    return null;
            }
            Requirement r = newRequirement(expr.getKey(), op, expr.getValues());
            selector.add(r);
        }
        return selector;
    }

    public static Requirement newRequirement(String key, SelectOperation op, String[] values) {
        switch (op) {
            case In:
            case NotIn:
                if (values == null || values.length == 0) {
                    return null;
                }
                break;
            case Equals:
            case DoubleEquals:
            case NotEquals:
                if (values == null || values.length != 1) {
                    return null;
                }
                break;
            case Exists:
            case DoesNotExist:
                if (values == null || values.length == 0) {
                    return null;
                }
                break;
            case GreaterThan:
            case LessThan:
                if (values == null || values.length != 1) {
                    return null;
                }
                for (String s : values) {
                    try {
                        Integer.valueOf(s);
                    } catch (Exception e) {
                        return null;
                    }
                }
                break;
            default:
                return null;
        }
        Requirement requirement = new Requirement();
        requirement.setKey(key);
        requirement.setOperatorObject(op);
        requirement.setStrValues(values);
        return requirement;
    }
}
