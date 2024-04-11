package com.kapturecrm.nlpdashboardservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableEurekaClient
//@ComponentScan(basePackages = {"com.kapturecrm.*", "com.kapture.*"})
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@EnableJpaRepositories
public class KaptureNlpDashboardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KaptureNlpDashboardServiceApplication.class, args);
    }

}
