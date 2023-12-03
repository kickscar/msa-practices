package com.poscodx.msa.service.oauth2client.emaillist.security;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@SpringBootConfiguration
@EnableWebSecurity
public class Config {

	@Bean
	SecurityFilterChain scurityFilterChain(HttpSecurity http, DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient) throws Exception {			
		http
			.csrf(csrf -> csrf.disable())
			//.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
				authorizationManagerRequestMatcherRegistry
					.anyRequest().permitAll();
			});
		

//		
//		OAuth2LoginAuthenticationFilter enable
//		This filter intercepts requests and applies the needed logic for OAuth2 authentication
//		
		http
			.oauth2Login(oauth2LoginCustomizer -> {
				oauth2LoginCustomizer
				
					.authorizationEndpoint().baseUri("/oauth2/authorize")
					
					.and() 
					.tokenEndpoint(tokenEndpointCustomizer -> {
						tokenEndpointCustomizer.accessTokenResponseClient(accessTokenResponseClient);
					})
		            
		            .redirectionEndpoint().baseUri("/login/oauth2/code/*")  // must starts with '/login'

		            .and()
		            .defaultSuccessUrl("/landing");
                    //.successHandler(successHandler());


					


			});

		http.oauth2Client(oauth2ClientCustomizer -> {
			oauth2ClientCustomizer.authorizationCodeGrant(authorizationCode -> {
				authorizationCode
					.accessTokenResponseClient(accessTokenResponseClient);
			});
		});
		
//		http.oauth2Client(oauth2ClientCustomizer -> {
//			oauth2ClientCustomizer.authorizationCodeGrant(authorizationCodeGrantCustomizer -> {
//				authorizationCodeGrantCustomizer
//					
//					.accessTokenResponseClient(null);
//				
//			});
//		});
		
		return http.build();
	}

	@Bean
	public DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient() {
		return new DefaultAuthorizationCodeTokenResponseClient();
	}
	
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            
            System.out.println(authentication);
//            String id = defaultOAuth2User.getAttributes().get("id").toString();
//            String body = """
//                    {"id":"%s"}
//                    """.formatted(id);
 
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
 
            PrintWriter writer = response.getWriter();
            writer.println("success");
            writer.flush();
        });
    }	
}
