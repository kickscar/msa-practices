package com.poscodx.msa.service.emaillist.security;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
@EnableWebSecurity
public class Config {

	@Bean	
    SecurityFilterChain scurityFilterChain(HttpSecurity http) throws Exception {
    
	    http
	    	.csrf(csrf -> csrf.disable())
	    	
	    	.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	    	
	    	.logout()	
	        .addLogoutHandler(keycloakLogoutHandler())	
	        .logoutSuccessUrl("/")	
	    	.and()

	    	//
	    	// OAuth2LoginAuthenticationFilter enable
	    	// This filter intercepts requests and applies the needed logic for OAuth2 authentication  
	    	//
	    	// .oauth2Login() 	
	        // .and()
	    	//
	    	
	    	
	    	
	    	.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
	    		authorizationManagerRequestMatcherRegistry
	    		
	    		.requestMatchers(
	    				new RegexRequestMatcher("^/admin$", null)
	    		).hasRole("ADMIN")
	    			
	    		.requestMatchers(
	    				new RegexRequestMatcher("^/$", "POST")
                ).hasAnyRole("ADMIN", "USER")
                
	    		.anyRequest().permitAll();
        });
        
	    // The oauth2ResourceServer method will validate the bound JWT token against Keycloak server
        http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        
        return http.build();	
    }
    
	@LoadBalanced
    @Bean
    RestTemplate restTemplte() {
		return new RestTemplate();
	} 
	
	@Bean
    LogoutHandler keycloakLogoutHandler() {
    	return new KeycloakLogoutHandler(restTemplte());
    }
	

   
    @Bean
    @ConditionalOnProperty(prefix="spring.config.activate", name="on-profile", havingValue = "test")
    ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration dummyRegistration = ClientRegistration.withRegistrationId("dummy")
                .clientId("dummy")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("/")
                .scope("openid")
                .authorizationUri("/")
                .tokenUri("/")
                .build();

        return new InMemoryClientRegistrationRepository(dummyRegistration);
    }    
}
