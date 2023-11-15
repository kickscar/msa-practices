package com.poscodx.msa.service.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.poscodx.msa.service.storage.httpd.SimpleHttpd;

@SpringBootApplication
public class ServiceStorage {

	public static void main(String[] args) {
		SpringApplication.run(ServiceStorage.class, args);
	}

	@Bean
	ApplicationRunner scriptRunner() {
		return new ApplicationRunner() {
			@Autowired
			private SimpleHttpd httpd;

			@Override
			public void run(ApplicationArguments args) throws Exception {
				httpd.start();
			}
		};
	}
}
