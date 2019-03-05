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

    public static String combineUrl(String server){
        return "https://"+server;

    }
}
