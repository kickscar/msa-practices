package com.poscodx.msa.service.storage;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.poscodx.msa.service.storage.httpd.SimpleHttpd;

@EnableEurekaClient
@SpringBootApplication
public class ServiceStorage {

	public static void main(String[] args) {
		SpringApplication.run(ServiceStorage.class, args);
	}

    @Bean
    TomcatServletWebServerFactory servletContainer() {
		return new TomcatServletWebServerFactory() {
			@Override
			protected void customizeConnector(Connector connector) {
				super.customizeConnector(connector);
				connector.setParseBodyMethods("POST,PUT,DELETE");
			}
		};
	}
    
	@Bean
	ApplicationRunner httpdRunner() {
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
