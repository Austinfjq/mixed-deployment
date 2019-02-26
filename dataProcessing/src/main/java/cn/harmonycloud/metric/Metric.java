package cn.harmonycloud.metric;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @Author: changliu
 * @Date: 2018/12/18 14:27
 * @Description:
 */
public class Metric {

    private String hostIp;
    private String port;

    private int queryNum;
    private int labelQueryNum;

    private ArrayList<List<String>> queryList;
    private ArrayList<List<String>> queryKey;
    private ArrayList<List<String>> dataType;

    private ArrayList<List<String>> labelQueryKey;
    private ArrayList<List<String>> labelName;
    private ArrayList<List<String>> labelQueryList;
    private ArrayList<List<String>> labelTypeList;

    public Metric() {
    }

    public String getHostIp() {
        return hostIp;
    }

    public String getPort() {
        return port;
    }

    public int getQueryNum() {
        return queryNum;
    }

    public int getLabelQueryNum() {
        return labelQueryNum;
    }

    public ArrayList<List<String>> getQueryList() {
        return queryList;
    }

    public ArrayList<List<String>> getQueryKey() {
        return queryKey;
    }

    public ArrayList<List<String>> getDataType() {
        return dataType;
    }

    public ArrayList<List<String>> getLabelQueryKey() {
        return labelQueryKey;
    }

    public ArrayList<List<String>> getLabelName() {
        return labelName;
    }

    public ArrayList<List<String>> getLabelQueryList() {
        return labelQueryList;
    }

    public ArrayList<List<String>> getLabelTypeList() {
        return labelTypeList;
    }

    public void init(String configFile) {
        Properties pps = new Properties();
        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(configFile);
            pps.load(inputStream);

            hostIp = pps.getProperty("hostIp");
            port = pps.getProperty("port");

            queryNum = Integer.parseInt(pps.getProperty("queryNum"));
            labelQueryNum = Integer.parseInt(pps.getProperty("labelQueryNum"));

            queryKey = new ArrayList<>();
            queryList = new ArrayList<>();
            dataType = new ArrayList<>();

            for (int i = 0; i < queryNum; i++) {
                List<String> temp = Arrays.asList(pps.getProperty("queryKey" + String.valueOf(i))
                        .split(";"));
                queryKey.add(temp);
                temp = Arrays.asList(pps.getProperty("queryList" + String.valueOf(i))
                        .split(";"));
                queryList.add(temp);
                temp = Arrays.asList(pps.getProperty("dataTypeList" + String.valueOf(i))
                        .split(";"));
                dataType.add(temp);
            }

            labelQueryKey = new ArrayList<>();
            labelName = new ArrayList<>();
            labelQueryList = new ArrayList<>();
            labelTypeList = new ArrayList<>();

            for (int i = 0; i < labelQueryNum; i++) {
                List<String> temp = Arrays.asList(pps.getProperty("labelQueryKey" + String.valueOf(i))
                        .split(";"));
                labelQueryKey.add(temp);
                temp = Arrays.asList(pps.getProperty("labelName" + String.valueOf(i))
                        .split(";"));
                labelName.add(temp);
                temp = Arrays.asList(pps.getProperty("labelQueryList" + String.valueOf(i))
                        .split(";"));
                labelQueryList.add(temp);
                temp = Arrays.asList(pps.getProperty("labelTypeList" + String.valueOf(i))
                        .split(";"));
                labelTypeList.add(temp);
            }

        } catch (FileNotFoundException e) {
            System.out.println("properties path error");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Metric configFile = new Metric();
        configFile.init("metric.properties");
    }


}
