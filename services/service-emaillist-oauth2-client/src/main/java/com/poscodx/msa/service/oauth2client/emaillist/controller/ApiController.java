package com.poscodx.msa.service.oauth2client.emaillist.controller;


import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.poscodx.msa.service.oauth2client.emaillist.dto.JsonResult;

@RestController
public class ApiController {

	private final RestTemplate restTemplate;

	public ApiController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@RequestMapping("/landing")
	public ResponseEntity<JsonResult> landing(@RegisteredOAuth2AuthorizedClient("emaillist-oauth2-client") OAuth2AuthorizedClient authorizedClient) {
		OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
		OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
		
		return ResponseEntity
		.status(HttpStatus.OK)
//		.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
		.body(JsonResult.success(Map.of("accessToken", accessToken.getTokenValue(), "refreshToken", refreshToken.getTokenValue())));
	}
	
//	@PostMapping("/auth")
//	public ResponseEntity<JsonResult> auth(
//			@RequestParam(value="username", required=true, defaultValue="") String username,
//			@RequestParam(value="password", required=true, defaultValue="") String password) {
//
//		String accessToken = "";
//		String refreshToken = "";
//		
//		try {
//			// header
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//			headers.setBasicAuth("ZW1haWxsaXN0OmlQWkp4Z2NqdG1NajRCRExzbWJ5ZjFnV0RBOVBmbTZ5");
//			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//	
//			// body
//			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//			body.add("grant_type", "password");
//			body.add("username", username);
//			body.add("password", password);
//	
//			// send request
//			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//			ResponseEntity<?> response = restTemplate.exchange("http://localhost:5555/realms/mysite-realm/protocol/openid-connect/token", HttpMethod.POST, requestEntity, Map.class);
//			
//			// receive response
//			Map<String, Object> map = (Map<String, Object>)response.getBody();
//			accessToken = (String)map.get("access_token");
//			refreshToken = (String)map.get("refresh_token");
//			
//		} catch(HttpClientErrorException ex) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonResult.fail(ex.toString()));
//		}
//		
//        ResponseCookie responseCookie = ResponseCookie
//        		.from("refreshToken",refreshToken)
//                .httpOnly(true)
//                .secure(false)
//                .path("/")
//                .maxAge(60*5) // 5mins
//                .build();
//		
//		return ResponseEntity
//				.status(HttpStatus.OK)
//				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
//				.body(JsonResult.success(accessToken));
//	}
	
//	@PostMapping("/refresh-token")
//	public ResponseEntity<JsonResult> refresh(@CookieValue(name = "refreshToken", defaultValue = "") String refreshToken) {
//		String accessToken = "";
//		
//		try {
//			// header
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//			headers.setBasicAuth("ZW1haWxsaXN0OmlQWkp4Z2NqdG1NajRCRExzbWJ5ZjFnV0RBOVBmbTZ5");
//			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//	
//			// body
//			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//			body.add("grant_type", "");
//			body.add("username", username);
//			body.add("password", password);
//	
//			// send request
//			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//			ResponseEntity<?> response = restTemplate.exchange("http://localhost:5555/realms/mysite-realm/protocol/openid-connect/token", HttpMethod.POST, requestEntity, Map.class);
//			
//			// receive response
//			Map<String, Object> map = (Map<String, Object>)response.getBody();
//			accessToken = (String)map.get("access_token");
//			refreshToken = (String)map.get("refresh_token");
//			
//		} catch(HttpClientErrorException ex) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonResult.fail(ex.toString()));
//		}
//		
//        ResponseCookie responseCookie = ResponseCookie
//        		.from("refreshToken",refreshToken)
//                .httpOnly(true)
//                .secure(false)
//                .path("/")
//                .maxAge(60*5) // 5mins
//                .build();
//		
//		return ResponseEntity
//				.status(HttpStatus.OK)
//				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
//				.body(JsonResult.success(accessToken));
//	}	
}