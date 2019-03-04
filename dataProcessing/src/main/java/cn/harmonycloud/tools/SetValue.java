package cn.harmonycloud.tools;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: changliu
 * @Date: 2018/12/22 19:31
 * @Description:
 */
public class SetValue {
    public static void run(Object item, String parmName, String parmValue) {
        try {
            Class clazz = item.getClass();
            Field field = clazz.getDeclaredField(parmName);
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
            Method wM = pd.getWriteMethod();//获得写方法

            if (wM.getParameterTypes()[0] == Long.class) {
                wM.invoke(item, Long.valueOf(parmValue));
            } else if (wM.getParameterTypes()[0] == Double.class) {
                wM.invoke(item, Double.valueOf(parmValue));
            } else {
                wM.invoke(item, parmValue);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
