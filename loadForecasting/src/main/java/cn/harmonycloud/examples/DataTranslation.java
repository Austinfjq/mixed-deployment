package cn.harmonycloud.examples;

import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author wangyuzhong
 * @date 18-12-17 下午2:47
 * @Despriction
 */
public class DataTranslation {

    public static DataSet getDataFromTxt(){
        File file = new File("/home/wangyuzhong/ideaProjects/mixed-deployment/loadForecasting/src/main/resource/data.txt");
        DataSet res = new DataSet();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            int i=0;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                DataPoint dataPoint = new DataPoint(Double.valueOf(s),i);
                i++;
                res.add(dataPoint);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        res.setPeriodsPerYear(7);
        return res;
    }

    public static DataSet getDataFromCsv() {
        File csv = new File("/home/wangyuzhong/ideaProjects/mixed-deployment/loadForecasting/src/main/resource/cpu_usage.csv");  // CSV文件路径
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DataSet dataSet = new DataSet();
        String line = "";
        DataPoint dp = null;
        try {
            List<String> allString = new ArrayList<>();
            while ((line = br.readLine()) != null)
            {
                StringTokenizer stringTokenizer = new StringTokenizer(line,";");
                stringTokenizer.nextToken();
                String time = stringTokenizer.nextToken().replace("\"", "");
                Date date = null;
                try {
                    date = formatter.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Double cpu_usage = Double.valueOf(stringTokenizer.nextToken());
                dp = new DataPoint(cpu_usage,(date.getTime())/1000);
                System.out.println(date.getTime()/1000);
                dataSet.add(dp);
            }
            System.out.println("csv表格中所有行数："+allString.size());
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        dataSet.setPeriodsPerYear(8);
        return dataSet;
    }

//    public void string2Date(String time) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        Date date = formatter.parse("2018-11-07T18:37:42.803+08:00");
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(sDate);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        int startDay = 0;
//        int endDay = 0;
//
//        try {
//            Date dateStart = format.parse(startDate);
//            Date datEnd = format.parse(endDate);
//
//            startDay = (int) (dateStart.getTime() / 1000);
//            endDay = (int) (datEnd.getTime() / 1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.err.println(startDay);
//        System.err.println(endDay);
//    }

    public static void main(String[] args) {
        DataSet ds = getDataFromCsv();

        for (DataPoint dataPoint:ds) {
            System.out.println(dataPoint.getValue());
        }
    }
}
