package cn.harmonycloud.datacenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class DataCenterApplication {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        InputStream in = null;
        //String path = "./usr/local/data-center/application.properties";
        //String path = "/root/application/application.properties";
        String path = "./data-center/application.properties";
        //String path = "E:\\mixed-deployment\\data-center\\application.properties";
        if(args.length != 0){//args[0]-->配置文件地址
            in = new FileInputStream(new File(args[0]));
        }else {
            //in = DataCenterApplication.class.getClassLoader().getResourceAsStream("application.properties");
            in = new FileInputStream(new File(path));
        }
        properties.load(in);
        SpringApplication app = new SpringApplication(DataCenterApplication.class);
//      app.setDefaultProperties(properties);
        app.run(args);
    }
}

