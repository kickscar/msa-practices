package me.kickscar.emaillist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EmailListApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(EmailListApplication.class, args);
	}

	@LoadBalanced
	@Bean
	public RestTemplate restTemplte() {
		return new RestTemplate();
	}
}
