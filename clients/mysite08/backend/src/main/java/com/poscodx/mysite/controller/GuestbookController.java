package com.poscodx.mysite.controller;

import java.util.Map;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.poscodx.mysite.vo.GuestbookVo;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/api/guestbook")
public class GuestbookController {
	private final RestTemplate restTemplate;

	public GuestbookController(@LoadBalanced RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping
	public ResponseEntity<?> get(@RequestParam(value="no", required=true, defaultValue="0") Long no) {
		Map<String, Object> response = restTemplate.getForObject(String.format("http://service-guestbook/?no=%d", no), Map.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<?> post(@RequestBody GuestbookVo vo) {
		Map<String, Object> response = restTemplate.postForObject("http://service-guestbook/", vo, Map.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{no}")
	public ResponseEntity<?> delete(@PathVariable("no") Long no, @RequestParam(value="password", required=true, defaultValue="") String password) {
		// Header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// Body
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("password", password);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		ResponseEntity<?> response = restTemplate.exchange(String.format("http://service-guestbook/%d", no), HttpMethod.DELETE, requestEntity, Map.class);
		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
	}
}