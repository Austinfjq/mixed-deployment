package cn.harmonycloud.tools;


import java.text.ParseException;
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
    private static Calendar currentTime = null;

    public static String getCurrentTime() {
       Calendar nowTime = Calendar.getInstance();
       currentTime = nowTime;
       Date nowDate = nowTime.getTime();
       String time = sdf.format(nowDate);
       return time;
    }

    public static String getStartTime(String endTime, int time) {

        Date date = null;
        try {
            date = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, -time);

        Date startTimeDate = calendar.getTime();
        String startTime = sdf.format(startTimeDate);
        return startTime;
    }

    public static String getNextPeriodTime(int period) {
        if (null == currentTime) {
            getCurrentTime();
        }

        Calendar calendar = currentTime;
        calendar.add(Calendar.SECOND, period);

        Date nowDate = calendar.getTime();
        String time = sdf.format(nowDate);
        return time;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String dataStr){
        String res;
        long lt = new Long(dataStr);
        Date date = new Date(lt*1000);
        res = sdf.format(date);
        return res;
    }


    public static void main(String[] args) {
        String timestr = "2019-04-16 17:54:23";

        int time = 3600;

        System.out.println(getStartTime(timestr, time));
    }

}
