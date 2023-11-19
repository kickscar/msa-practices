package com.poscodx.msa.service.emaillist;

import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.WebApplicationContext;


@SpringBootApplication
public class EmaillistApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmaillistApplication.class, args);
	}
	
	//
	// Test Security Filters...
	//
	@Bean
    ApplicationRunner applicationRunner() {
        return new ApplicationRunner () {
        	
        	@Autowired
        	WebApplicationContext context;
        	
        	//
        	// oAuth2 Client Registration
        	//
        	@Autowired
        	ClientRegistrationRepository clientRegistrationRepository;

        	
            @Override
            public void run(ApplicationArguments args) throws Exception {
            	FilterChainProxy filterChainProxy = (FilterChainProxy)context.getBean("springSecurityFilterChain", Filter.class);
            	
            	System.out.println("FilterChains Count:" + filterChainProxy.getFilterChains().size());
            	
            	SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(0);
            	List<Filter> filters =  securityFilterChain.getFilters();
            	
    	        // All Filters
    	        for(Filter filter : filters) {
    	            System.out.println(filter.getClass());
    	        }
    	        
    	        System.out.println(clientRegistrationRepository.findByRegistrationId("keycloak")); 
            }
        };
    } 
}