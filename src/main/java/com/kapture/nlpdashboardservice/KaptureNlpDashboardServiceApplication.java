package com.kapture.nlpdashboardservice;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableEurekaClient
//@ComponentScan(basePackages = {"com.kapturecrm.*", "com.kapture.*"})
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableJpaRepositories(basePackages = {"com.kapture.nlpdashboardservice.repository.mysql"})
public class KaptureNlpDashboardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KaptureNlpDashboardServiceApplication.class, args);
    }

}
