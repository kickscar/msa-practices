package com.poscodx.emaillist.controller;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.poscodx.emaillist.dto.JsonResult;

@RestController
public class OAuthClientController {
	@Value("${spring.security.oauth2.client.provider.keycloak-authorization-server.issuer-uri}")
	private String issuerUri;
	
	@Value("${spring.security.oauth2.client.registration.emaillist-oauth2-client.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.emaillist-oauth2-client.client-secret}")
	private String clientSecret;

	@Value("${spring.security.oauth2.client.provider.keycloak-authorization-server.token-uri}")
	private String tokenEndPoint;

	private final RestTemplate restTemplate;

	public OAuthClientController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/logout")
	public ResponseEntity<JsonResult> logout(@RequestHeader(value="Authorization", required=true, defaultValue="") String bearerToken, @CookieValue(name = "refreshToken", defaultValue = "") String refreshToken) {
		String endSessionEndpoint = issuerUri + "/protocol/openid-connect/logout";
		String accessToken = Pattern.compile("(?i)Bearer ", Pattern.UNICODE_CASE).matcher(bearerToken).replaceAll("");
		
		try {
			// header
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			if(!"".equals(bearerToken)) {
				headers.set("Authorization", "Bearer " + accessToken);
			}
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	
			// body
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("client_id", clientId);
			body.add("client_secret", clientSecret);
			body.add("refresh_token", refreshToken);

			// send request
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			restTemplate.exchange(endSessionEndpoint, HttpMethod.POST, requestEntity, Map.class);
			
			// receive response(204 NO_CONTENT)
		} catch(HttpClientErrorException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonResult.fail(ex.toString()));
		}
		
		
		// reomove refreshToken cookie
        ResponseCookie responseCookie = ResponseCookie
        		.from("refreshToken", "")
                .path("/")
                .maxAge(0)
                .build();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(JsonResult.success(null));
	}
	
	@GetMapping("/refresh-token")
	public ResponseEntity<JsonResult> refresh(@CookieValue(name = "refreshToken", defaultValue = "") String refreshToken) {
		String accessToken = "";
		
		try {
			// header
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setBasicAuth(Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(Charset.forName("US-ASCII"))));	         
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			
			// body
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("grant_type", "refresh_token");
			body.add("refresh_token", refreshToken);

			// send request
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			ParameterizedTypeReference<Map<String, Object>> responseEntity = new ParameterizedTypeReference<Map<String, Object>>() {};
			
			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(tokenEndPoint, HttpMethod.POST, requestEntity, responseEntity);
			
			// receive response
			Map<String, Object> map = response.getBody();
			accessToken = (String)map.get("access_token");
			refreshToken = (String)map.get("refresh_token");
			
		} catch(HttpClientErrorException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonResult.fail(ex.toString()));
		}
		
		// Bake RefreshToken Cookie
        ResponseCookie responseCookie = ResponseCookie
        	.from("refreshToken", refreshToken)
        	.path("/")
        	.maxAge(60*60*24)	// 1day
        	.secure(false)		// over HTTPS (x)
        	.httpOnly(true)		// Prevent Cross-site scripting (XSS): JavaScript code cannot read or modify
        	.sameSite("strict")	// Prevent CSRF attacks
        	.build();
		
		return ResponseEntity
			.status(HttpStatus.OK)
			.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
			.body(JsonResult.success(accessToken));
	}

	



//	
//	Client for Resource Owner Password Credentials Grant Type
//	
	@PostMapping("/auth")
	public ResponseEntity<JsonResult> auth(@RequestParam(value="username", required=true, defaultValue="") String username, @RequestParam(value="password", required=true, defaultValue="") String password) {
		String accessToken = "";
		String refreshToken = "";
		
		try {
			// header
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setBasicAuth(clientSecret);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	
			// body
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("grant_type", "password");
			body.add("username", username);
			body.add("password", password);
	
			// send request
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			ParameterizedTypeReference<Map<String, Object>> responseEntity = new ParameterizedTypeReference<Map<String, Object>>() {};
			
			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(tokenEndPoint, HttpMethod.POST, requestEntity, responseEntity);
			
			// receive response
			Map<String, Object> map = response.getBody();
			accessToken = (String)map.get("access_token");
			refreshToken = (String)map.get("refresh_token");
			
		} catch(HttpClientErrorException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonResult.fail(ex.toString()));
		}
		
        ResponseCookie responseCookie = ResponseCookie
        	.from("refreshToken", refreshToken)
            .path("/")
            .maxAge(60*60*24)	// 1day
            .secure(false)		// over HTTPS (x)
            .httpOnly(true)		// Prevent Cross-site scripting (XSS): JavaScript code cannot read or modify
            .sameSite("strict")	// Prevent CSRF attacks
            .build();
		
		return ResponseEntity
			.status(HttpStatus.OK)
			.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
			.body(JsonResult.success(accessToken));
	}
	
	//
	// needs session management
	// if SessionManagementFilter not set, fails!	
	//
	@GetMapping("/test-jwt")
	public ResponseEntity<JsonResult> landing(@RegisteredOAuth2AuthorizedClient("emaillist-oauth2-client") OAuth2AuthorizedClient authorizedClient) {
		OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
		OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(JsonResult.success(Map.of("accessToken", accessToken.getTokenValue(), "refreshToken", refreshToken.getTokenValue())));
	}	
}