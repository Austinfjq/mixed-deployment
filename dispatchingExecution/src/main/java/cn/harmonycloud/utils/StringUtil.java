package cn.harmonycloud.utils;

import java.util.Random;

public class StringUtil {
    public static String randomStringGenerator(int n){
        if (n <= 0)return null;
        String mother = "1234567890qwertyuiopasdfghjklzxcvbnm";
        Random rand = new Random();
        int index ;
        String result = "";
        for (int i = 0; i < n;i++){
            index = rand.nextInt(mother.length());
            result = result+mother.charAt(index);
        }
        return result;
    }
    public static void main(String[] args){
        int n = 5;
        for (int i = 0; i < 100;i++){
            System.out.println(StringUtil.randomStringGenerator(5));
        }
    }
}
