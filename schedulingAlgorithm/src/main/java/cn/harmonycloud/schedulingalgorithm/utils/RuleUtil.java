package cn.harmonycloud.schedulingalgorithm.utils;

import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelectorOperator;
import cn.harmonycloud.schedulingalgorithm.basic.Cache;
import cn.harmonycloud.schedulingalgorithm.affinity.InternalSelector;
import cn.harmonycloud.schedulingalgorithm.affinity.LabelSelector;
import cn.harmonycloud.schedulingalgorithm.affinity.LabelSelectorOperator;
import cn.harmonycloud.schedulingalgorithm.affinity.LabelSelectorRequirement;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelectorRequirement;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelectorTerm;
import cn.harmonycloud.schedulingalgorithm.affinity.PodAffinityTerm;
import cn.harmonycloud.schedulingalgorithm.affinity.Requirement;
import cn.harmonycloud.schedulingalgorithm.affinity.SelectOperation;
import cn.harmonycloud.schedulingalgorithm.affinity.Selector;
import cn.harmonycloud.schedulingalgorithm.affinity.Taint;
import cn.harmonycloud.schedulingalgorithm.affinity.Toleration;
import cn.harmonycloud.schedulingalgorithm.constant.Constants;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
import cn.harmonycloud.schedulingalgorithm.dataobject.NodeCondition;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Resource;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RuleUtil {
    public static Resource getRequestedAfterOp(Pod pod, Node node, int op) {
        Resource requested = new Resource();

        if (op == Constants.OPERATION_ADD) {
            requested.setMilliCPU((long) (1000 * (node.getCpuUsage() + pod.getCpuRequest())));
            requested.setMemory(node.getMemUsage().longValue() + pod.getMemRequest().longValue());
        } else {
            requested.setMilliCPU((long) (1000 * (node.getCpuUsage() - pod.getCpuRequest())));
            requested.setMemory(node.getMemUsage().longValue() - pod.getMemRequest().longValue());
        }
        return requested;
    }

    public static void updateRequestedAfterOp(Pod pod, Resource resource, int op) {
        if (op == Constants.OPERATION_ADD) {
            resource.setMilliCPU(resource.getMilliCPU() + (long) (pod.getCpuRequest() * 1000));
            resource.setMemory(resource.getMemory() + pod.getMemRequest().longValue());
        } else {
            resource.setMilliCPU(resource.getMilliCPU() - (long) (pod.getCpuRequest() * 1000));
            resource.setMemory(resource.getMemory() - pod.getMemRequest().longValue());
        }
    }

    public static Resource getNodeResource(Node node) {
        Resource resource = new Resource();
        resource.setMilliCPU((long) (1000 * (node.getCpuUsage())));
        resource.setMemory(node.getMemUsage().longValue());
        return resource;
    }

    public static Resource getNodeFinalResource(Node node, Map<String, List<Pod>> hostPodMap) {
        Resource finalResource = RuleUtil.getNodeResource(node);
        if (hostPodMap.containsKey(node.getNodeName())) {
            List<Pod> pods1 = hostPodMap.get(node.getNodeName());
            for (Pod pod : pods1) {
                RuleUtil.updateRequestedAfterOp(pod, finalResource, pod.getOperation());
            }
        }
        return finalResource;
    }

    public static Map<String, List<Pod>> getHostsToPodsMap(List<Pod> pods, List<String> hosts) {
        HashMap<String, List<Pod>> hostPodMap = new HashMap<>();
        for (int i = 0; i < pods.size(); i++) {
            if (!hostPodMap.containsKey(hosts.get(i))) {
                hostPodMap.put(hosts.get(i), new ArrayList<>());
            }
            hostPodMap.get(hosts.get(i)).add(pods.get(i));
        }
        return hostPodMap;
    }

    private static final String LabelZoneRegion = "failure-domain.beta.kubernetes.io/region";
    private static final String LabelZoneFailureDomain = "failure-domain.beta.kubernetes.io/zone";

    public static String getZoneKey(Node node) {
        Map<String, String> labels = node.getLabels();
        if (labels == null) {
            return null;
        }
        String region = labels.get(LabelZoneRegion);
        String failureDomain = labels.get(LabelZoneFailureDomain);
        if (StringUtils.isEmpty(region) && StringUtils.isEmpty(failureDomain)) {
            return null;
        }

        // We include the null character just in case region or failureDomain has a colon
        // (We do assume there's no null characters in a region or failureDomain)
        // As a nice side-benefit, the null character is not printed by fmt.Print or glog
        return region + ":\\x00:" + failureDomain;
    }

    public static List<Integer> normalizeReduce(Pod pod, List<Node> nodes, Cache cache, List<Integer> mapResult, int maxPriority, boolean reverse) {
        int maxCount = 0;
        for (int i = 0; i < mapResult.size(); i++) {
            if (mapResult.get(i) > maxCount) {
                maxCount = mapResult.get(i);
            }
        }
        if (maxCount == 0) {
            if (reverse) {
                for (int i = 0; i < mapResult.size(); i++) {
                    mapResult.set(i, maxPriority);
                }
            }
            return mapResult;
        }

        for (int i = 0; i < mapResult.size(); i++) {
            int score = mapResult.get(i);

            score = maxPriority * score / maxCount;
            if (reverse) {
                score = maxPriority - score;
            }

            mapResult.set(i, score);
        }
        return mapResult;
    }

    public static boolean tolerationsTolerateTaint(List<Toleration> tolerations, Taint taint) {
        return tolerations.stream().anyMatch(toleration -> toleratesTaint(toleration, taint));
    }

    private static boolean toleratesTaint(Toleration toleration, Taint taint) {
        if (taint == null) {
            return true;
        }
        if (Objects.equals(toleration.getEffect(), taint.getEffectObject().getEffect())) {
            return false;
        }
        if (Objects.equals(toleration.getKey(), taint.getKey())) {
            return false;
        }
        if (toleration.getOperatorObject() == null) {
            // operator == null 当作 operator == TolerationOperator.Equal
            return Objects.equals(toleration.getValue(), taint.getValue());
        }
        switch (toleration.getOperatorObject()) {
            case Equal:
                return Objects.equals(toleration.getValue(), taint.getValue());
            case Exists:
                return true;
            default:
                return false;
        }
    }

    public static Set<String> getNamespacesFromPodAffinityTerm(Pod pod, PodAffinityTerm term) {
        Set<String> names = new HashSet<>();
        if (term.getNamespaces().length == 0) {
            names.add(pod.getNamespace());
        } else {
            names.addAll(Arrays.asList(term.getNamespaces()));
        }
        return names;
    }

    static class NothingSelector implements Selector {
        public boolean matches(Map<String, String> labels) {
            return false;
        }

        public boolean isEmpty() {
            return false;
        }

        public boolean add(Requirement... r) {
            return false;
        }

        public List<Requirement> Requirements() {
            return null;
        }
    }

    public static Selector newNothingSelector() {
        return new NothingSelector();
    }

    public static Selector labelSelectorAsSelector(LabelSelector ps) {
        if (ps == null) {
            return new NothingSelector();
        }
        if (ps.getMatchLabels().isEmpty() && ps.getMatchExpressions().length == 0) {
            return new InternalSelector();
        }
        Selector selector = new InternalSelector();
        for (Map.Entry<String, String> entry : ps.getMatchLabels().entrySet()) {
            String k = entry.getKey();
            String[] vs = new String[1];
            vs[0] = entry.getValue();
            Requirement r = SelectorUtil.newRequirement(k, SelectOperation.Equals, vs);
            selector.add(r);
        }
        for (LabelSelectorRequirement expr : ps.getMatchExpressions()) {
            SelectOperation op;
            switch (expr.getOperatorObject()) {
                case LabelSelectorOpIn:
                    op = SelectOperation.In;
                    break;
                case LabelSelectorOpNotIn:
                    op = SelectOperation.NotIn;
                    break;
                case LabelSelectorOpExists:
                    op = SelectOperation.Exists;
                    break;
                case LabelSelectorOpDoesNotExist:
                    op = SelectOperation.DoesNotExist;
                    break;
                default:
                    return null;
            }
            Requirement r = SelectorUtil.newRequirement(expr.getKey(), op, expr.getValues());
            selector.add(r);
        }
        return selector;
    }

    public static boolean podMatchesTermsNamespaceAndSelector(Pod pod, Set<String> namespaces, Selector selector) {
        if (!namespaces.contains(pod.getNamespace())) {
            return false;
        }
        Map<String, String> podLabels = pod.getLabels();
        if (!selector.matches(podLabels)) {
            return false;
        }
        return true;
    }

    public static boolean nodesHaveSameTopologyKey(Node nodeA, Node nodeB, String topologyKey) {
        Map<String, String> nodeALabels = nodeA.getLabels();
        Map<String, String> nodeBLabels = nodeB.getLabels();
        if (topologyKey == null || topologyKey.isEmpty() || nodeALabels == null || nodeBLabels == null) {
            return false;
        }
        String nodeALabel = nodeALabels.get(topologyKey);
        String nodeBLabel = nodeBLabels.get(topologyKey);
        if (nodeALabel != null && nodeBLabel != null) {
            return nodeALabel.equals(nodeBLabel);
        }
        return false;
    }

    public static boolean matchNodeSelectorTerms(List<NodeSelectorTerm> nodeSelectorTerms, Map<String, String> nodeLabels, Map<String, String> nodeFields) {
        for (NodeSelectorTerm req : nodeSelectorTerms) {
            if ((req.getMatchExpressions() == null || req.getMatchExpressions().length == 0)
                    && (req.getMatchFields() == null || req.getMatchFields().length == 0)) {
                continue;
            }
            if (req.getMatchExpressions() != null && !(req.getMatchExpressions().length == 0)) {
                Selector labelSelector = SelectorUtil.nodeSelectorRequirementsAsSelector(Arrays.asList(req.getMatchExpressions()));
                if (labelSelector == null || !labelSelector.matches(nodeLabels)) {
                    continue;
                }
            }
            if (req.getMatchFields() != null && !(req.getMatchFields().length == 0)) {
                Selector fieldSelector = nodeSelectorRequirementsAsFieldSelector(Arrays.asList(req.getMatchFields()));
                if (fieldSelector == null || !fieldSelector.matches(nodeFields)) {
                    continue;
                }
            }
            return true;
        }
        return false;
    }

    private static Selector nodeSelectorRequirementsAsFieldSelector(List<NodeSelectorRequirement> nsm) {
        if (nsm == null || nsm.isEmpty()) {
            return null;
        }
        List<Selector> selectors = new ArrayList<>();
        for (NodeSelectorRequirement expr : nsm) {
//            if (NodeSelectorOperator.NodeSelectorOpIn.equals(expr.getOperator())) {
//                if (expr.getValues() == null || expr.getValues().length != 1) {
//                    return null;
//                }
//                selectors.add(oneTermEqualSelector(expr.getKey(), expr.getValues()[0]));
//            } else if (NodeSelectorOperator.NodeSelectorOpNotIn.equals(expr.getOperator())) {
//                if (expr.getValues() == null || expr.getValues().length != 1) {
//                    return null;
//                }
//                selectors.add(oneTermNotEqualSelector(expr.getKey(), expr.getValues()[0]));
//            } else {
//                return null;
//            }
            switch (expr.getOperatorObject()) {
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

    static class AndTerm implements Selector {
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

    private static Selector andSelectors(List<Selector> selectors) {
        AndTerm andTerm = new AndTerm();
        andTerm.selectors = selectors;
        return andTerm;
    }

    static class HasTerm implements Selector {
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

    private static Selector oneTermEqualSelector(String key, String value) {
        HasTerm hasTerm = new HasTerm();
        hasTerm.field = key;
        hasTerm.value = value;
        return hasTerm;
    }

    static class NotHasItem implements Selector {
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

    private static Selector oneTermNotEqualSelector(String key, String value) {
        NotHasItem notHasItem = new NotHasItem();
        notHasItem.field = key;
        notHasItem.value = value;
        return notHasItem;
    }

    public static Resource getNodeAllocatableResource(Node node) {
        Resource allocatable = new Resource();
        allocatable.setMilliCPU((long) (node.getAllocatableCpuCores() * 1000));
        allocatable.setMemory(node.getAllocatableMem().longValue());
        return allocatable;
    }

    public static boolean checkNodeCondition(NodeCondition nodeCondition, String[] types, String[] statuses) {
        if (nodeCondition == null) {
            return true;
        }
        for (String type : types) {
            if (type.equalsIgnoreCase(nodeCondition.getType())) {
                for (String status : statuses) {
                    if (status.equalsIgnoreCase(nodeCondition.getType())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static String getLastName(String className) {
        try {
            String[] names = className.split("\\.");
            String[] names1 = names[names.length - 1].split("@");
            return names1[0];
        } catch (Exception e) {
            return className;
        }
    }
}
