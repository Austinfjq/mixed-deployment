package cn.harmonycloud.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PythonRunner {

    public  static void pythonExec(String[] parms){
        try {
            //执行命令
            Process p = Runtime.getRuntime().exec(parms);
            //取得命令结果的输出流
            InputStream fis=p.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr=new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br=new BufferedReader(isr);

            String line;
            //直到读完为止
            while((line=br.readLine())!=null)
            {
                System.out.println(line);
            }

            br.close();
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("end");

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String[] parms = new String[] { "/usr/bin/python2.7", "/home/wangyuzhong/ideaProjects/mixed-deployment/loadForecasting/src/main/resource/python/train_predict.py"};
//        String[] parms = new String[] { "/usr/bin/python2.7", "/home/wangyuzhong/ideaProjects/mixed-deployment/loadForecasting/src/main/resource/python/test.py"};
        PythonRunner.pythonExec(parms);
    }
}
