package cn.harmonycloud.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wangyuzhong
 * @date 19-1-23 上午9:58
 * @Despriction
 */
public class DateUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
    private static Calendar currentTime = null;

    public static String getCurrentTime() {
       Calendar nowTime = Calendar.getInstance();
       currentTime = nowTime;
       Date nowDate = nowTime.getTime();
       String time = sdf.format(nowDate);
       return time;
    }

    public static String getLastPeriodTime() {
        if (null == currentTime) {
            getCurrentTime();
        }

        Calendar calendar = currentTime;
        calendar.add(Calendar.SECOND, -Constant.EVALUATE_PERIOD);

        Date nowDate = calendar.getTime();
        String time = sdf.format(nowDate);
        return time;
    }

    public static void main(String[] args) {
        System.out.println(getCurrentTime());
        System.out.println(getLastPeriodTime());
    }

}
