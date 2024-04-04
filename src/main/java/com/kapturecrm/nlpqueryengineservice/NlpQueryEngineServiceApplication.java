package com.kapturecrm.nlpqueryengineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NlpQueryEngineServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NlpQueryEngineServiceApplication.class, args);
	}

}
