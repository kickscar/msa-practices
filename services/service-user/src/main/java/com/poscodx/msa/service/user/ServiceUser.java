package com.poscodx.msa.service.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ServiceUser {

    public static void main(String[] args) {
        SpringApplication.run(ServiceUser.class, args);
    }

    @Bean
    RestTemplate restTemplte() {
		return new RestTemplate();
	}	
}
