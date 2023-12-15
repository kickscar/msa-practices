package com.poscodx.emaillist;


import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class EmaillistOAuth2Client {

    public static void main(String[] args) {
        SpringApplication.run(EmaillistOAuth2Client.class, args);
    }
    
    @Bean
    RestTemplate restTemplte() {
		return new RestTemplate();
	}	

    @Bean
    ApplicationRunner applicationRunner() {
        return new ApplicationRunner () {
            @Autowired
            private FilterChainProxy filterChainProxy;
            
            @Autowired
            ClientRegistrationRepository clientRegistrationRepository;
            
            @Override
            public void run(ApplicationArguments args) throws Exception {
                List<SecurityFilterChain> SecurityFilterChains = filterChainProxy.getFilterChains();
                
                SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(0);
                List<Filter> filters =  securityFilterChain.getFilters();

                // All Filters
                log.info(filters.toString());
                for(Filter filter : filters) {
                    log.info("security filter::::::" + filter.getClass());
                }
                
                System.out.println(clientRegistrationRepository);
                
                ClientRegistration c = clientRegistrationRepository.findByRegistrationId("emaillist-oauth2-client");
                
                System.out.println(c);
            }
        };
    }
}
