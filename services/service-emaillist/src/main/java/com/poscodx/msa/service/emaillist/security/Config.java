package com.poscodx.msa.service.emaillist.security;

import java.util.Collection;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootConfiguration
@EnableWebSecurity
public class Config {

	@Bean
	SecurityFilterChain scurityFilterChain(HttpSecurity http,
			Converter<Jwt, JwtAuthenticationToken> jwtAuthenticationConverter, LogoutHandler keycloakLogoutHandler)
			throws Exception {

		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			
			.logout().addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/").and()

				//
				// OAuth2LoginAuthenticationFilter enable
				// This filter intercepts requests and applies the needed logic for OAuth2
				// authentication
				//
				// .oauth2Login()
				// .and()
				//
			
			// .formLogin().loginProcessingUrl("/user/auth").usernameParameter("email").passwordParameter("password")
			// .and()
			
			.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
				authorizationManagerRequestMatcherRegistry
					.requestMatchers(new RegexRequestMatcher("^/admin$", null)).hasRole("ADMIN")
					
					.requestMatchers(new RegexRequestMatcher("^/$", "POST")).hasAnyRole("USER")
					
					.anyRequest().permitAll();
			});

		// The oauth2ResourceServer method will validate the bound JWT token against Keycloak Server
		http.oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

		return http.build();
	}

	@LoadBalanced
	@Bean
	RestTemplate restTemplte() {
		return new RestTemplate();
	}

	@Component
	@RequiredArgsConstructor
	static private class JwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

		private final Converter<Jwt, Collection<? extends GrantedAuthority>> jwtGrantedAuthoritiesConverter;

		@Override
		public JwtAuthenticationToken convert(Jwt jwt) {
			final var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
			final String username = JsonPath.read(jwt.getClaims(), "preferred_username");

			System.out.println("authorities--->" + authorities);
			System.out.println("username--->" + username);

			JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, authorities, username);

			System.out.println("token-->" + token);

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
						/* handle nothing */
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
						return (Stream<String>) ((Collection) claim).stream()
								.flatMap(colItem -> ((Collection) colItem).stream()).map(String.class::cast);
					}

					return Stream.empty();
					
				}).map(strAuthority -> {
					System.out.println(strAuthority);
					return "ROLE_" + strAuthority;
				}).map(SimpleGrantedAuthority::new).map(GrantedAuthority.class::cast).toList();
			}
		}
	}

	@Slf4j
	@Component
	@RequiredArgsConstructor
	static private class KeycloakLogoutHandler implements LogoutHandler {
		private final RestTemplate restTemplate;

		@Override
		public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
			logoutFromKeycloak((OidcUser) auth.getPrincipal());
		}

		private void logoutFromKeycloak(OidcUser user) {
			String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";

			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endSessionEndpoint)
					.queryParam("id_token_hint", user.getIdToken().getTokenValue());

			ResponseEntity<String> logoutResponse = restTemplate.getForEntity(builder.toUriString(), String.class);
			if (logoutResponse.getStatusCode().is2xxSuccessful()) {
				log.info("Successfulley logged out from Keycloak");
			} else {
				log.error("Could not propagate logout to Keycloak");
			}
		}

	}

	@Bean
	@ConditionalOnProperty(prefix = "spring.config.activate", name = "on-profile", havingValue = "test")
	ClientRegistrationRepository clientRegistrationRepository() {
		ClientRegistration dummyRegistration = ClientRegistration.withRegistrationId("dummy").clientId("dummy")
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).redirectUri("/").scope("openid")
				.authorizationUri("/").tokenUri("/").build();

		return new InMemoryClientRegistrationRepository(dummyRegistration);
	}
}
