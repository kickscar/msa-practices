package com.poscodx.emaillist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EmaillistApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(EmaillistApplication.class, args);
	}

	@LoadBalanced
    @Bean
    RestTemplate restTemplte() {
		return new RestTemplate();
	}	
}