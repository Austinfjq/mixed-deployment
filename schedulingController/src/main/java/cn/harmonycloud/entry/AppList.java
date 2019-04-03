package cn.harmonycloud.entry;

import com.alibaba.fastjson.JSON;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import static cn.harmonycloud.tools.Constant.CONFIG_FIME_PATH;
import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;

public class AppList {
    private ArrayList<Map<String, String>> onlineApp;
    private ArrayList<Map<String, String>> offlineApp;

    public AppList() {
    }

    public ArrayList<Map<String, String>> getOnlineApp() {
        return onlineApp;
    }

    public void setOnlineApp(ArrayList<Map<String, String>> onlineApp) {
        this.onlineApp = onlineApp;
    }

    public ArrayList<Map<String, String>> getOfflineApp() {
        return offlineApp;
    }

    public void setOfflineApp(ArrayList<Map<String, String>> offlineApp) {
        this.offlineApp = offlineApp;
    }

    public void init() {

        try {
            SAXReader reader = new SAXReader();
//            URL fileURL = LoadConfigFile.class.getClass().getResource("strategyConfig.xml");
//            System.out.println(fileURL);
            InputStream inputStream = new BufferedInputStream(new FileInputStream(CONFIG_FIME_PATH));
//            InputStream inputStream = ClassLoader.getSystemResourceAsStream(CONFIG_FIME_PATH);
            Document document = reader.read(inputStream);

            Element root = document.getRootElement();
//            System.out.println("获取了根元素:" + root.getName());
            List<Element> list = root.elements();

            //get online and offline app

            Element onlineElem = root.element("onlineService");
            Iterator iterOnline = onlineElem.elementIterator("index");

            ArrayList<Map<String, String>> onlineTemp = new ArrayList<>();
            while (iterOnline.hasNext()) {
                Element itemEle = (Element) iterOnline.next();
                HashMap<String, String> tp = new HashMap<>();
                tp.put("name", itemEle.elementTextTrim("name"));
                tp.put("namespace", itemEle.elementTextTrim("namespace"));
                onlineTemp.add(tp);
            }

            Element offlineElem = root.element("offlineService");
            Iterator iterOffline = offlineElem.elementIterator("index");

            ArrayList<Map<String, String>> offlineTemp = new ArrayList<>();
            while (iterOffline.hasNext()) {
                Element itemEle = (Element) iterOffline.next();
                HashMap<String, String> tp = new HashMap<>();
                tp.put("name", itemEle.elementTextTrim("name"));
                tp.put("namespace", itemEle.elementTextTrim("namespace"));
                offlineTemp.add(tp);
            }

            this.setOnlineApp(onlineTemp);
            this.setOfflineApp(offlineTemp);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        AppList t = new AppList();
        t.init();
        System.out.println(JSON.toJSONString(t, WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty));

    }
}

