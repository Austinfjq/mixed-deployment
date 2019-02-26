package cn.harmonycloud.schedulingalgorithm.utils;

import java.util.Collection;

public class CheckUtil {
    public static boolean nullOrEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    /**
     * if o is one of array, then return true, otherwise return false
     */
    public static boolean range(Object o, Object... array) {
        if (o == null) {
            for (Object a : array) {
                if (a == null) {
                    return true;
                }
            }
            return false;
        } else {
            for (Object a : array) {
                if (o.equals(a)) {
                    return true;
                }
            }
            return false;
        }
    }
}
