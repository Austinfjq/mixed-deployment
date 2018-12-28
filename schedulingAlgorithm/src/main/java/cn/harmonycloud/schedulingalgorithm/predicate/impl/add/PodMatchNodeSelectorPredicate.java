package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.Cache;
import cn.harmonycloud.schedulingalgorithm.affinity.Affinity;
import cn.harmonycloud.schedulingalgorithm.affinity.InternalSelector;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeAffinity;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelectorRequirement;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelectorTerm;
import cn.harmonycloud.schedulingalgorithm.affinity.Requirement;
import cn.harmonycloud.schedulingalgorithm.affinity.SelectOperation;
import cn.harmonycloud.schedulingalgorithm.affinity.Selector;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 没有测试过，逻辑比较复杂，bug应该很多
 */
public class PodMatchNodeSelectorPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // 1. node selector
        // TODO 亲和性信息 如何拿到pod的node selector
        Map<String, String> podNodeSelectorMap = new HashMap<>();
        // TODO 亲和性信息 如何拿到node的labels
        Map<String, String> labels = new HashMap<>();
        if (!podNodeSelectorMap.isEmpty()) {
            Selector selector = new InternalSelector(podNodeSelectorMap);
            if (!selector.matches(labels)) {
                return false;
            }
        }
        // 2. affinity terms
        // TODO: 亲和性信息 如何拿到pod.Spec.Affinity
        Affinity affinity = new Affinity();
        if (affinity != null && affinity.getNodeAffinity() != null) {
            NodeAffinity nodeAffinity = affinity.getNodeAffinity();
            if (nodeAffinity.getRequiredDuringSchedulingIgnoredDuringExecution() == null) {
                return true;
            } else {
                List<NodeSelectorTerm> nodeSelectorTerms = nodeAffinity.getRequiredDuringSchedulingIgnoredDuringExecution().getNodeSelectorTerms();
                return nodeMatchesNodeSelectorTerms(node, nodeSelectorTerms);
            }
        }
        return true;
    }

    private boolean nodeMatchesNodeSelectorTerms(Node node, List<NodeSelectorTerm> nodeSelectorTerms) {
        Map<String, String> nodeFields = new HashMap<>();
        nodeFields.put("metadata.name", node.getNodeName());
        // TODO 亲和性信息 如何拿到node的labels
        Map<String, String> nodeLabels = new HashMap<>();
        for (NodeSelectorTerm req : nodeSelectorTerms) {
            if ((req.getMatchExpressions() == null || req.getMatchExpressions().isEmpty())
                    && (req.getMatchFields() == null || req.getMatchFields().isEmpty())) {
                continue;
            }
            if (req.getMatchExpressions() != null && !req.getMatchExpressions().isEmpty()) {
                Selector labelSelector = NodeSelectorRequirementsAsSelector(req.getMatchExpressions());
                if (labelSelector == null || !labelSelector.matches(nodeLabels)) {
                    continue;
                }
            }
            if (req.getMatchFields() != null && !req.getMatchFields().isEmpty()) {
                Selector fieldSelector = NodeSelectorRequirementsAsFieldSelector(req.getMatchFields());
                if (fieldSelector == null || !fieldSelector.matches(nodeLabels)) {
                    continue;
                }
            }
            return true;
        }
        return false;
    }

    private Selector NodeSelectorRequirementsAsFieldSelector(List<NodeSelectorRequirement> nsm) {
        if (nsm == null || nsm.isEmpty()) {
            return null;
        }
        List<Selector> selectors = new ArrayList<>();
        for (NodeSelectorRequirement expr : nsm) {
            switch (expr.getOperator()) {
                case NodeSelectorOpIn:
                    if (expr.getValues() == null || expr.getValues().length != 1) {
                        return null;
                    }
                    selectors.add(oneTermEqualSelector(expr.getKey(), expr.getValues()[0]));
                    break;
                case NodeSelectorOpNotIn:
                    if (expr.getValues() == null || expr.getValues().length != 1) {
                        return null;
                    }
                    selectors.add(oneTermNotEqualSelector(expr.getKey(), expr.getValues()[0]));
                    break;
                default:
                    return null;
            }
        }
        return andSelectors(selectors);
    }

    class AndTerm implements Selector {
        List<Selector> selectors;

        public boolean matches(Map<String, String> labels) {
            for (Selector s : selectors) {
                if (!s.matches(labels)) {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty() {
            return selectors.isEmpty();
        }

        public boolean add(Requirement... r) {
            return false;
        }

        public List<Requirement> Requirements() {
            List<Requirement> ls = new ArrayList<>();
            for (Selector s : selectors) {
                ls.addAll(s.Requirements());
            }
            return ls;
        }
    }

    private Selector andSelectors(List<Selector> selectors) {
        AndTerm andTerm = new AndTerm();
        andTerm.selectors = selectors;
        return andTerm;
    }

    class HasTerm implements Selector {
        String field;
        String value;

        public boolean matches(Map<String, String> labels) {
            return value.equals(labels.get(field));
        }

        public boolean isEmpty() {
            return false;
        }

        public boolean add(Requirement... r) {
            return false;
        }

        public List<Requirement> Requirements() {
            List<Requirement> ls = new ArrayList<>();
            Requirement r = new Requirement();
            r.setKey(field);
            String[] values = new String[1];
            values[0] = value;
            r.setStrValues(values);
            return ls;
        }
    }

    private Selector oneTermEqualSelector(String key, String value) {
        HasTerm hasTerm = new HasTerm();
        hasTerm.field = key;
        hasTerm.value = value;
        return hasTerm;
    }

    class NotHasItem implements Selector {
        String field;
        String value;

        public boolean matches(Map<String, String> labels) {
            return !value.equals(labels.get(field));
        }

        public boolean isEmpty() {
            return false;
        }

        public boolean add(Requirement... r) {
            return false;
        }

        public List<Requirement> Requirements() {
            List<Requirement> ls = new ArrayList<>();
            Requirement r = new Requirement();
            r.setKey(field);
            String[] values = new String[1];
            values[0] = value;
            r.setStrValues(values);
            return ls;
        }
    }

    private Selector oneTermNotEqualSelector(String key, String value) {
        NotHasItem notHasItem = new NotHasItem();
        notHasItem.field = key;
        notHasItem.value = value;
        return notHasItem;
    }

    private Selector NodeSelectorRequirementsAsSelector(List<NodeSelectorRequirement> nsm) {
        if (nsm == null || nsm.isEmpty()) {
            return null;
        }
        Selector selector = new InternalSelector();
        for (NodeSelectorRequirement expr : nsm) {
            SelectOperation op;
            switch (expr.getOperator()) {
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

    static Requirement newRequirement(String key, SelectOperation op, String[] values) {
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
        requirement.setOperator(op);
        requirement.setStrValues(values);
        return requirement;
    }
}
