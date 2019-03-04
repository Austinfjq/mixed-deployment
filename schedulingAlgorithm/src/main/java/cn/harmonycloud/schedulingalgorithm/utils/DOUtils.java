package cn.harmonycloud.schedulingalgorithm.utils;

import cn.harmonycloud.schedulingalgorithm.affinity.Affinity;
import cn.harmonycloud.schedulingalgorithm.affinity.PodAntiAffinity;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import com.google.gson.Gson;
import net.sf.json.JSONObject;

public class DOUtils {
    public static String NAME_SPLIT = "_";

    public static String getServiceFullName(Service service) {
        return service.getServiceName() + NAME_SPLIT + service.getNamespace();
    }

    public static String getServiceFullName(Pod pod) {
        return pod.getServiceName() + NAME_SPLIT + pod.getNamespace();
    }

    public static String getPodFullName(Pod pod) {
        return pod.getPodName() + NAME_SPLIT + pod.getNamespace();
    }

    public static void main(String[] args) {
        // 测试k8sObjectToJson()
        String s = "Affinity(nodeAffinity=null, podAffinity=null, podAntiAffinity=PodAntiAffinity(preferredDuringSchedulingIgnoredDuringExecution=[WeightedPodAffinityTerm(podAffinityTerm=PodAffinityTerm(labelSelector=LabelSelector(matchExpressions=[LabelSelectorRequirement(key=app, operator=In, values=[group-schedule], additionalProperties={})], matchLabels=null, additionalProperties={}), namespaces=[cbl], topologyKey=kubernetes.io/hostname, additionalProperties={}), weight=50, additionalProperties={}), WeightedPodAffinityTerm(podAffinityTerm=PodAffinityTerm(labelSelector=LabelSelector(matchExpressions=[LabelSelectorRequirement(key=app, operator=In, values=[group-schedule], additionalProperties={})], matchLabels=null, additionalProperties={}), namespaces=[cbl], topologyKey=harmonycloud.cn/group, additionalProperties={}), weight=50, additionalProperties={})], requiredDuringSchedulingIgnoredDuringExecution=[], additionalProperties={}), additionalProperties={})";
        s = DOUtils.k8sObjectToJson(s);
        Affinity affinity = new Gson().fromJson(s, Affinity.class);
    }

    /**
     * 把来自K8S监控数据的某种数据格式转成JSON
     *
     * @param src 源String
     * @return JSON格式
     */
    public static String k8sObjectToJson(String src) {
        // Type(***) --> {***}
        // abc --> "abc"
        // = --> :
        if (src == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(src);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '(') {
                int last = findFirst(sb, i);
                int length = i - last;
                sb.delete(last, i);
                i -= length;
                sb.setCharAt(i, '{');
            } else if (sb.charAt(i) == ')') {
                sb.setCharAt(i, '}');
            } else if (sb.charAt(i) == '=') {
                sb.insert(i, '\"');
                int first = findFirst(sb, i);
                sb.insert(first, '\"');
                i += 2;
                sb.setCharAt(i, ':');
                int j = i + 1;
                if (sb.charAt(j) == '[') {
                    int last2 = findLast(sb, j + 1);
                    if (sb.charAt(last2) != '(') {
                        int end1 = sb.indexOf(",", j + 1);
                        int end2 = sb.indexOf("]", j + 1);
                        int end = end1 < end2 ? end1 : end2;
                        if (end2 != j + 1 && !isNumber(sb.substring(j + 1, end))) {
                            sb.insert(j + 1, '\"');
                            sb.insert(last2 + 1, '\"');
                            for (int k = j + 1; k < last2; k++) {
                                if (sb.charAt(k) == ',') {
                                    sb.insert(k, "\"");
                                    k += 2;
                                    while (sb.charAt(k) == ' ') {
                                        k++;
                                    }
                                    sb.insert(k, "\"");
                                }
                            }
                        }
                    }
                } else {
                    int end = findLast(sb, j);
                    if (!sb.substring(j, end).equals("null") && !sb.substring(j, end).equals("")) {
                        if (!isNumber(sb.substring(j, end))) {
                            sb.insert(j, '\"');
                            sb.insert(end + 1, '\"');
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private static char[] symbols = {'=', ',', '[', ']', '(', ')', '{', '}', ' ', '\n', ':'};

    private static boolean containSymbols(char c) {
        for (char cc : symbols) {
            if (cc == c) {
                return true;
            }
        }
        return false;
    }

    private static int findFirst(StringBuilder sb, int i) {
        for (int j = i - 1; j >= 0; j--) {
            if (containSymbols(sb.charAt(j))) {
                return j + 1;
            }
        }
        return 0;
    }

    private static int findLast(StringBuilder sb, int i) {
        for (int j = i; j < sb.length(); j++) {
            if (containSymbols(sb.charAt(j))) {
                return j;
            }
        }
        return sb.length();
    }

    private static boolean isNumber(String s) {
        try {
            Double.valueOf(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
