package com.poscodx.emaillist.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootConfiguration
@EnableWebSecurity
public class Config {

	@Bean
	SecurityFilterChain scurityFilterChain(HttpSecurity http, RestTemplate restTemplate) throws Exception {
        http
        	.csrf(csrf -> csrf.disable())
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .logout(logout -> logout.disable())
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            	authorizationManagerRequestMatcherRegistry
                	.anyRequest()
                    .permitAll();
             });
		
		http.oauth2Login(oauth2LoginCustomizer -> {
			
			// OAuth2 Authorization Code Grant Type 적용
			oauth2LoginCustomizer
				.authorizationEndpoint().baseUri("/oauth2/authorize")
				
				.and()
				.redirectionEndpoint().baseUri("/login/oauth2/code/*")	// 반드시 /login 으로 시작할 것 

				.and()
				
				//
				// session을 사용하지 않기 때문에 redirect로 다시 접근할 때는 
				// OAuth2AuthorizedClientService에 OAuth2AuthorizedClient가 없기 때문에
				// AccessToken과 RefreshToken이 없음
				//
				// .defaultSuccessUrl("/");
				
				//
				// 대신,
				// SuccessHandler가 응답하기 전까지는 OAuth2AuthorizedClientService에 
				// OAuth2AuthorizedClient가 있기 때문에 AccessToken과 RefreshToken을 가져올 수 있다.
				//
				.successHandler(authenticationSuccessHandler());

		});
		
		return http.build();
	}

	@Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
    	return new AuthenticationSuccessHandler() {
    	    @Autowired
    	    private ApplicationContext applicationContext;
    	    
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
	            
				OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
				DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User)oAuth2AuthenticationToken.getPrincipal();

				log.info("OIDC: ID JWT:" + oAuth2AuthenticationToken.toString());
	            log.info("OIDC: Identity of User AuthentiCated:" + defaultOAuth2User);

				OAuth2AuthorizedClientService oAuth2AuthorizedClientService = applicationContext.getBean(OAuth2AuthorizedClientService.class);
				OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
				
				//
				// Discard Access Token
				// Grant Flow의 User Agent(Browser)에서 실행되는 JS 클라이언트(React)에게
				// Access Token 전달이 부자연스럽기 때문에 최초 Access Token은 폐기
	    		//
				OAuth2AccessToken accessToken = oAuth2AuthorizedClient.getAccessToken();
	            log.info("OAuth2: Authorized JWT: Access Token:" + accessToken.getTokenValue());
	            
	            //
				// refresh token는 HttpOnly Cookie로 구워 클라이언트(React) 애플리케이션에게 전달
				//
	            OAuth2RefreshToken refreshToken = oAuth2AuthorizedClient.getRefreshToken();
	            log.info("OAuth2: Authorized JWT: Refresh Token:" + refreshToken.getTokenValue());
	            
	            
	            /*
	            response.getWriter().println("AccessToken:" + accessToken.getTokenValue());
	            response.getWriter().println("Refresh Token:" + refreshToken.getTokenValue());
	            /*/ 
	            ResponseCookie cookie = ResponseCookie
	                    .from("refreshToken", refreshToken.getTokenValue())
	                	.path("/")
	                	.maxAge(60*60*24)	// 1day
	                	.secure(false)		// over HTTPS (x)
	                	.httpOnly(true)		// Prevent Cross-site scripting (XSS): JavaScript code cannot read or modify
	                	.sameSite("strict")	// Prevent CSRF attacks
	                	.build();

	            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY); // 302
	            response.setHeader("Cache=Control", "no-cache, no-store, must-revalidate");	            
	            response.setHeader("Pragma", "no-cache");	            
	            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());	
	            response.sendRedirect("/"); // 클라이언트(React) 애플리케이션 랜딩!									
	            //*/
			}
    	};
    }

    @Bean
    RestTemplate restTemplte() {
		return new RestTemplate();
	}	
}