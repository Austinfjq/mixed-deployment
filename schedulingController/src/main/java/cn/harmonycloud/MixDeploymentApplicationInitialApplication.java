package cn.harmonycloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class MixDeploymentApplicationInitialApplication {
    public static void main(String[] args) {
        SpringApplication.run(MixDeploymentApplicationInitialApplication.class, args);
    }
}
