package com.poscodx.msa.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ServiceConfigServer {
	public static void main(String[] args) {
		SpringApplication.run(ServiceConfigServer.class, args);
	}
}