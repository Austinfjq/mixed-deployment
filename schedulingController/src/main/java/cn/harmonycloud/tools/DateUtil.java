package cn.harmonycloud.tools;

import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wangyuzhong
 * @date 19-1-23 上午9:58
 * @Despriction
 */
public class DateUtil {

    @Value("${SchedulePeriod}")
    private static int schedulePeriod;

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
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
        calendar.add(Calendar.SECOND, -schedulePeriod);

        Date nowDate = calendar.getTime();
        String time = sdf.format(nowDate);
        return time;
    }

    public static String getNextPeriodTime() {
        if (null == currentTime) {
            getCurrentTime();
        }

        Calendar calendar = currentTime;
        calendar.add(Calendar.SECOND, schedulePeriod);

        Date nowDate = calendar.getTime();
        String time = sdf.format(nowDate);
        return time;
    }

}
