package cn.harmonycloud.beans;

import cn.harmonycloud.entry.Config;
import com.alibaba.fastjson.JSON;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static cn.harmonycloud.tools.Constant.*;
import static com.alibaba.fastjson.serializer.SerializerFeature.*;

public class LoadConfigFile {

    public static ArrayList<Config> run() {

        ArrayList<Config> configList = new ArrayList<>();

        try {
            SAXReader reader = new SAXReader();

//            InputStream inputStream = ClassLoader.getSystemResourceAsStream(CONFIG_FILE_PATH);
            InputStream inputStream = new FileInputStream(new File(CONFIG_FILE_PATH));
            Document document = reader.read(inputStream);

            Element root = document.getRootElement();
//            System.out.println("获取了根元素:" + root.getName());
            List<Element> list = root.elements();

            Element conElem = root.element("times");
            Iterator iter = conElem.elementIterator("index");

            while (iter.hasNext()) {
                Config config = new Config();
//                Map<String, Long> podAddList = new HashMap<>();
//                Map<String, Long> podDelList = new HashMap<>();

                Element recordEle = (Element) iter.next();
                config.setTime(recordEle.elementTextTrim("time"));

                Iterator onlineIters = recordEle.elementIterator("onlineService");
                Iterator offlineIters = recordEle.elementIterator("offlineService");

                while (onlineIters.hasNext()) {
                    Element itemEle = (Element) onlineIters.next();
//                    String add = itemEle.elementTextTrim("add"); // 拿到head下的子节点的值
                    String num = itemEle.elementTextTrim("num");
//                    if (add.equals("true")) {
//                        podAddList.put("online", Long.parseLong(num));
//                    } else if (add.equals("false")) {
//                        podDelList.put("online", Long.parseLong(num));
//                    }
                    config.setOnlineNum(Integer.parseInt(num));
                }


                while (offlineIters.hasNext()) {
                    Element itemEle = (Element) offlineIters.next();
//                    String add = itemEle.elementTextTrim("add");
                    String num = itemEle.elementTextTrim("num");
//                    if (add.equals("true")) {
//                        podAddList.put("offline", Long.parseLong(num));
//                    } else if (add.equals("false")) {
//                        podDelList.put("offline", Long.parseLong(num));
//                    }
                    config.setOfflineNum(Integer.parseInt(num));
                }

//                config.setPodAddList(podAddList);
//                config.setPodDelList(podDelList);
                configList.add(config);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return configList;

    }

    public static void main(String args[]) {
        System.out.println(JSON.toJSONString(run(), WriteMapNullValue,
                WriteNullNumberAsZero, WriteNullStringAsEmpty, WriteNullListAsEmpty));
    }

}
