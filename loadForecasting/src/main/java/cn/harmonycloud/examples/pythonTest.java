package cn.harmonycloud.examples;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author wangyuzhong
 * @date 18-12-19 下午2:20
 * @Despriction
 */
public class pythonTest {
    public static void main(String[] args) {
        try {
            System.out.println("start");
            Process pr = Runtime.getRuntime().exec("python test.py");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
            System.out.println("end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
