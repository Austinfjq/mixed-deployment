package cn.harmonycloud;

import cn.harmonycloud.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        LOGGER.info("Dispatching execution is running");
        SpringApplication.run(App.class, args);
    }
}
