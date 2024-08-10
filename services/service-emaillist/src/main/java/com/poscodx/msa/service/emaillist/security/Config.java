package com.poscodx.msa.service.emaillist.security;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.poscodx.msa.service.emaillist.dto.JsonResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootConfiguration
@EnableWebSecurity
public class Config {

	@Bean
	SecurityFilterChain scurityFilterChain(HttpSecurity http, Converter<Jwt, JwtAuthenticationToken> jwtAuthenticationConverter, AccessDeniedHandler accessDeniedHandler, AuthenticationEntryPoint authenticationEntryPoint) throws Exception {

        http
        	.csrf(csrf -> csrf.disable())
        	.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        	.logout(logout -> logout.disable())
        	.anonymous(anonymous -> anonymous.disable())
        	.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
        		authorizationManagerRequestMatcherRegistry
        			.requestMatchers(new RegexRequestMatcher("^/(\\?kw=.*)?$", "GET")).hasRole("READ")
        			.requestMatchers(new RegexRequestMatcher("^/$", "POST")).hasRole("WRITE")
        			.anyRequest().denyAll();
        	});
		
		http
			.oauth2ResourceServer(oauth2 -> {
				oauth2
					// The oauth2ResourceServer Method will Validate the Bound JWT Token against Keycloak Server
					.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
					
					// 403
					.accessDeniedHandler(accessDeniedHandler)
					
					// 401
					.authenticationEntryPoint(authenticationEntryPoint);
			});

		return http.build();
	}
	
	@Component
	static private class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401(Invalid Token)
		    response.setContentType("application/json");
		    response.setCharacterEncoding("utf-8");
		    
			String json = new ObjectMapper().writeValueAsString(JsonResult.fail("401 Unauthorized"));
			response.getOutputStream().write(json.getBytes("utf-8"));
		}
	}
	
	@Component
	static private class CustomAccessDeniedHandler implements AccessDeniedHandler {
		@Override
		public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403(Wrong API Endpoint...)
			response.setContentType("application/json");
		    response.setCharacterEncoding("utf-8");
		    
			String json = new ObjectMapper().writeValueAsString(JsonResult.fail("403 Forbidden"));
			response.getOutputStream().write(json.getBytes("utf-8"));
		}
	}

	@Component
	@RequiredArgsConstructor
	static private class JwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

		private final Converter<Jwt, Collection<? extends GrantedAuthority>> jwtGrantedAuthoritiesConverter;

		@Override
		public JwtAuthenticationToken convert(Jwt jwt) {
			final var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
			final String username = JsonPath.read(jwt.getClaims(), "preferred_username");
			JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, authorities, username);

			log.info("authorities:" + authorities);
			log.info("username:" + username);
			log.info("token:" + token);

			return token;
		}

		@Component
		static private class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<? extends GrantedAuthority>> {

			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Collection<? extends GrantedAuthority> convert(Jwt jwt) {

				return Stream.of("$.realm_access.roles", "$.resource_access.*.roles").flatMap(claimPaths -> {

					Object claim = null;

					try {
						claim = JsonPath.read(jwt.getClaims(), claimPaths);
					} catch (PathNotFoundException e) {
					}

					if (claim == null) {
						return Stream.empty();
					}

					if (claim instanceof String strClaim) {
						return Stream.of(strClaim.split(","));
					}

					if (claim instanceof String[] strClaim) {
						return Stream.of(strClaim);
					}

					if (!Collection.class.isAssignableFrom(claim.getClass())) {
						return Stream.empty();
					}

					final var iter = ((Collection) claim).iterator();

					if (!iter.hasNext()) {
						return Stream.empty();
					}

					final var firstItem = iter.next();

					if (firstItem instanceof String) {
						return (Stream<String>) ((Collection) claim).stream();
					}

					if (Collection.class.isAssignableFrom(firstItem.getClass())) {
						return (Stream<String>) ((Collection) claim).stream().flatMap(colItem -> ((Collection) colItem).stream()).map(String.class::cast);
					}

					return Stream.empty();
					
				}).map(strAuthority -> {
					
					// supporting Spring Security's Checking Authority "ROLE_" Based.
					return ("ROLE_" + strAuthority);
					
				}).map(SimpleGrantedAuthority::new).map(GrantedAuthority.class::cast).toList();
			}
		}
	}
}