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

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    private Calendar currentTime = null;



    public String getCurrentTime() {
       Calendar nowTime = Calendar.getInstance();
       currentTime = nowTime;
       Date nowDate = nowTime.getTime();
       String time = sdf.format(nowDate);
       return time;
    }

    public String getLastPeriodTime(int schedulePeriod) {
        if (null == currentTime) {
            getCurrentTime();
        }

        Calendar calendar = currentTime;
        calendar.add(Calendar.MILLISECOND, -schedulePeriod);

        Date nowDate = calendar.getTime();
        String time = sdf.format(nowDate);
        return time;
    }

    public String getNextPeriodTime(int schedulePeriod) {
        if (null == currentTime) {
            getCurrentTime();
        }

        Calendar calendar = currentTime;
        calendar.add(Calendar.MILLISECOND, schedulePeriod);

        Date nowDate = calendar.getTime();
        String time = sdf.format(nowDate);
        return time;
    }
}
