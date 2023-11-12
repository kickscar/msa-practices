package com.poscodx.msa.service.emaillist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EmaillistApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmaillistApplication.class, args);
	}
}