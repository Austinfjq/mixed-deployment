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
        InputStream in = DataCenterApplication.class.getClassLoader().getResourceAsStream("application.properties");
        properties.load(in);
        SpringApplication app = new SpringApplication(DataCenterApplication.class);
        app.setDefaultProperties(properties);
        app.run(args);
    }
}

