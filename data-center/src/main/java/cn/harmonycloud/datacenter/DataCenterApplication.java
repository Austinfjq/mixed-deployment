package cn.harmonycloud.datacenter;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.Properties;

@SpringBootApplication
public class DataCenterApplication {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        InputStream in = null;
        String path = "./usr/local/data-center/application.properties";
        if(args.length != 0){//args[0]-->配置文件地址
            in = new FileInputStream(new File(args[0]));
        }else {
            //in = DataCenterApplication.class.getClassLoader().getResourceAsStream("application.properties");
            in = new FileInputStream(new File(path));
        }
        properties.load(in);
        SpringApplication app = new SpringApplication(DataCenterApplication.class);
        app.setDefaultProperties(properties);
        app.run(args);
    }
}

