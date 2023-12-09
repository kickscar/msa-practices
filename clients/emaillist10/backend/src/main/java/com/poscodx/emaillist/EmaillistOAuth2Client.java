package com.poscodx.emaillist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EmaillistOAuth2Client {

    public static void main(String[] args) {
        SpringApplication.run(EmaillistOAuth2Client.class, args);
    }

    @Bean
    RestTemplate restTemplte() {
		return new RestTemplate();
	}	
}
