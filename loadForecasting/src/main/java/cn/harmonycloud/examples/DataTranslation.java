package cn.harmonycloud.examples;

import cn.harmonycloud.entry.DataPoint;
import cn.harmonycloud.entry.DataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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

    public static void main(String[] args) {
        System.out.println(getDataFromTxt().toString());
    }
}
