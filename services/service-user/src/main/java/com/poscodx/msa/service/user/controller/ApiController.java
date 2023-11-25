package com.poscodx.msa.service.user.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.poscodx.msa.service.user.dto.JsonResult;

@RestController
public class ApiController {

	private final RestTemplate restTemplate;

	public ApiController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@PostMapping("/auth")
	public ResponseEntity<JsonResult> auth(
			@RequestParam(value="username", required=true, defaultValue="") String username,
			@RequestParam(value="password", required=true, defaultValue="") String password) {

		String accessToken = "";
		String refreshToken = "";
		
		try {
			// header
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setBasicAuth("ZW1haWxsaXN0OmlQWkp4Z2NqdG1NajRCRExzbWJ5ZjFnV0RBOVBmbTZ5");
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	
			// body
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("grant_type", "password");
			body.add("username", username);
			body.add("password", password);
	
			// send request
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			ResponseEntity<?> response = restTemplate.exchange("http://localhost:5555/realms/mysite-realm/protocol/openid-connect/token", HttpMethod.POST, requestEntity, Map.class);
			
			// receive response
			Map<String, Object> map = (Map<String, Object>)response.getBody();
			accessToken = (String)map.get("access_token");
			refreshToken = (String)map.get("refresh_token");
			
		} catch(HttpClientErrorException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonResult.fail(ex.toString()));
		}
		
        ResponseCookie responseCookie = ResponseCookie
        		.from("refreshToken",refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60*5) // 5mins
                .build();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(JsonResult.success(accessToken));
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<JsonResult> refresh(@CookieValue(name = "refreshToken", defaultValue = "") String refreshToken) {
		String accessToken = "";
		
		try {
			// header
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setBasicAuth("ZW1haWxsaXN0OmlQWkp4Z2NqdG1NajRCRExzbWJ5ZjFnV0RBOVBmbTZ5");
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	
			// body
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("grant_type", "");
			body.add("username", username);
			body.add("password", password);
	
			// send request
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			ResponseEntity<?> response = restTemplate.exchange("http://localhost:5555/realms/mysite-realm/protocol/openid-connect/token", HttpMethod.POST, requestEntity, Map.class);
			
			// receive response
			Map<String, Object> map = (Map<String, Object>)response.getBody();
			accessToken = (String)map.get("access_token");
			refreshToken = (String)map.get("refresh_token");
			
		} catch(HttpClientErrorException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonResult.fail(ex.toString()));
		}
		
        ResponseCookie responseCookie = ResponseCookie
        		.from("refreshToken",refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60*5) // 5mins
                .build();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(JsonResult.success(accessToken));
	}	
}