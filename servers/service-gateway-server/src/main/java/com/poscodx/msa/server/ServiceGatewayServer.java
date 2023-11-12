package com.poscodx.msa.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ServiceGatewayServer {

	public static void main(String[] args) {
		SpringApplication.run(ServiceGatewayServer.class, args);
	}

}
