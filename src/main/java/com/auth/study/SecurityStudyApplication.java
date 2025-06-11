package com.auth.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.auth.study")
public class SecurityStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityStudyApplication.class, args);
	}

}
