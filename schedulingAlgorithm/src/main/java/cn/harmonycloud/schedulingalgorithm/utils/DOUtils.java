package cn.harmonycloud.schedulingalgorithm.utils;

import cn.harmonycloud.schedulingalgorithm.affinity.Affinity;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeAffinity;
import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelectorRequirement;
import cn.harmonycloud.schedulingalgorithm.affinity.PodAntiAffinity;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
import cn.harmonycloud.schedulingalgorithm.dataobject.Service;
import com.google.gson.Gson;
import net.sf.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Stream;

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

    public static String getPodFullName(String podName, String namespace) {
        return podName + NAME_SPLIT + namespace;
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
                            if (j + 1 != last2) {
                                sb.insert(j + 1, '\"');
                                sb.insert(last2 + 1, '\"');
                            }
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

    public static int binaryLessThan(double[] a, double key) {
        return binaryLessThan(a, 0, a.length, key);
    }

    public static int binaryLessThan(double[] a, int fromIndex, int toIndex, double key) {
        if (key < a[fromIndex]) {
            return fromIndex;
        } else if (key > a[toIndex - 1]) {
            return toIndex;
        }

        int low = fromIndex;
        int high = toIndex - 1;

        while (low < high - 1) {
            int mid = (low + high) >>> 1;
            double midVal = a[mid];

            if (midVal < key) {
                low = mid;
            } else if (midVal > key) {
                high = mid;
            } else {
                return mid; // key found
            }
        }
        return high;  // key not found.
    }
}
