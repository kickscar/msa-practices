package com.poscodx.emaillist.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
@EnableWebSecurity
public class Config {

	@Bean
	SecurityFilterChain scurityFilterChain(HttpSecurity http, RestTemplate restTemplate) throws Exception {		
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.logout()
			.disable()
			.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
				authorizationManagerRequestMatcherRegistry.anyRequest().permitAll();
			});

//		applies the needed logic for OAuth2 Authorization Code Grant Flow
		http.oauth2Login(oauth2LoginCustomizer -> {
			oauth2LoginCustomizer
				.authorizationEndpoint().baseUri("/oauth2/authorize")
				
				.and()
				.redirectionEndpoint().baseUri("/login/oauth2/code/*")	// must starts with '/login'

				.and()
				.successHandler(authenticationSuccessHandler());		// successHandler가 응답하기 전까지는 OAuth2AuthorizedClientService에 OAuth2AuthorizedClient가 있기 때문에 JWT를 가져올 수 있음
//				.defaultSuccessUrl("/");								// session을 사용하지 않기 때문에 redirect로 다시 접근할 때는 OAuth2AuthorizedClientService에 OAuth2AuthorizedClient가 없기 때문에 JWT가 없음
		});
		
		return http.build();
	}

	@Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
    	return new AuthenticationSuccessHandler() {
    	    @Autowired
    	    private ApplicationContext applicationContext;
    	    
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
	            
				OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
//	            System.out.println(oAuth2AuthenticationToken);
//				DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User)oAuth2AuthenticationToken.getPrincipal();
//	            System.out.println(defaultOAuth2User);

				OAuth2AuthorizedClientService oAuth2AuthorizedClientService = applicationContext.getBean(OAuth2AuthorizedClientService.class);
				OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
				
//				Discard: Grant Flow의 여기서는 User Agent(Browser)에서 실행되는 클라이언트(React) 애플리케이션에게 Access Token 전달이 부자연스럽기 때문에 버림! 
//	    		OAuth2AccessToken accessToken = oAuth2AuthorizedClient.getAccessToken();
	    		OAuth2RefreshToken refreshToken = oAuth2AuthorizedClient.getRefreshToken();
	    		
	    		// HTTP Response
	            Cookie cookie = new Cookie("refreshToken", refreshToken.getTokenValue());	// refresh token는 HttpOnly Cookie로 구워 클라이언트(React) 애플리케이션에게 전달
	            cookie.setHttpOnly(true);
	            cookie.setSecure(false);
	            cookie.setPath("/");
	            cookie.setMaxAge(60*5); // 5mins is just for testing
	            
	            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
	            response.setHeader("Cache=Control", "no-cache, no-store, must-revalidate");	            
	            response.setHeader("Pragma", "no-cache");	            
	            // response.setHeader("Authorization", "Bearer " + accessToken.getTokenValue());
	            response.addCookie(cookie);
	            response.sendRedirect("/");													// 클라이언트(React) 애플리케이션 다시 랜딩!
			}
    	};
    }

    @Bean
    RestTemplate restTemplte() {
		return new RestTemplate();
	}	
    
    // for Testing...
	@Bean
	@ConditionalOnProperty(prefix="spring.config.activate", name="on-profile", havingValue="test")
	ClientRegistrationRepository clientRegistrationRepository() {
		ClientRegistration dummyRegistration = ClientRegistration
				.withRegistrationId("dummy")
				.clientId("dummy")
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationUri("/dummy")
				.redirectUri("/dummy")
				.scope("openid")
				.tokenUri("/dummy")
				.build();

		return new InMemoryClientRegistrationRepository(dummyRegistration);
	}
}